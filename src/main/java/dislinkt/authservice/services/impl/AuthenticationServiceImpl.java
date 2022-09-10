package dislinkt.authservice.services.impl;

import dislinkt.authservice.dtos.*;
import dislinkt.authservice.repositories.specification.UserSpecification;
import dislinkt.authservice.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dislinkt.authservice.entities.Administrator;
import dislinkt.authservice.entities.Agent;
import dislinkt.authservice.entities.Gender;
import dislinkt.authservice.entities.Person;
import dislinkt.authservice.entities.User;
import dislinkt.authservice.exceptions.EmailAlreadyExists;
import dislinkt.authservice.exceptions.InvalidUsername;
import dislinkt.authservice.exceptions.UsernameAlreadyExists;
import dislinkt.authservice.mappers.AdministratorDtoMapper;
import dislinkt.authservice.mappers.AgentDtoMapper;
import dislinkt.authservice.mappers.UserDtoMapper;
import dislinkt.authservice.repositories.AdministratorRepository;
import dislinkt.authservice.repositories.AgentRepository;
import dislinkt.authservice.repositories.AuthorityRepository;
import dislinkt.authservice.repositories.PersonRepository;
import dislinkt.authservice.repositories.UserRepository;
import dislinkt.authservice.services.AuthenticationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserDtoMapper userMapper;

    @Autowired
    private AgentDtoMapper agentMapper;

    @Autowired
    private AdministratorDtoMapper administratorMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private KafkaTemplate<String, KafkaNotification> kafkaTemplate;

    public UserJwtToken loginUser(String username, String password) {
        PersonDto person = getPersonByUsername(username);
        if (person == null) {
            throw new InvalidUsername("Invalid credentials");
        }
        return generateTokenResponse(username, password);
    }

    public PersonDto registerUser(UserRegistrationRequest request) {

        checkUsername(request.getUsername());
        checkEmail(request.getEmail());

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAuthorities(new ArrayList<>() {
            {
                add(authorityRepository.findByName("ROLE_USER"));
            }
        });

        User newUser = userRepository.save(user);

        KafkaNotification kafkaNotification = new KafkaNotification(newUser.getId(), KafkaNotificationType.REGISTERED_USER);
        kafkaTemplate.send("dislinkt-notifications", kafkaNotification);

        return userMapper.toDto(newUser);
    }

    public AgentDto createAgent(AgentCreateRequest request) {

        checkUsername(request.getUsername());
        checkEmail(request.getEmail());

        Agent agent = agentMapper.toEntity(request);
        agent.setPassword(passwordEncoder.encode(request.getPassword()));
        agent.setAuthorities(new ArrayList<>() {
            {
                add(authorityRepository.findByName("ROLE_AGENT"));
            }
        });

        return agentMapper.toAgentDto(agentRepository.save(agent));
    }

    public PersonDto updatePerson(PersonDto updateDto) {
        Optional<Administrator> adminOptional = administratorRepository.findById(updateDto.getId());
        if (adminOptional.isPresent()) {
            Administrator admin = adminOptional.get();
            HashMap<String, Object> params = updatePerson(admin, updateDto);
            Administrator updatedAdmin = (Administrator) params.get("updatedPerson");
            String token = (String) params.get("token");
            updatedAdmin = administratorRepository.save(updatedAdmin);
            return administratorMapper.toDtoWithToken(updatedAdmin, token);
        }
        Optional<Agent> agentOptional = agentRepository.findById(updateDto.getId());
        if (agentOptional.isPresent()) {
            Agent agent = agentOptional.get();
            HashMap<String, Object> params = updatePerson(agent, updateDto);
            Agent updatedAgent = (Agent) params.get("updatedPerson");
            updatedAgent.setCompany(updateDto.getCompany());
            String token = (String) params.get("token");
            updatedAgent = agentRepository.save(updatedAgent);
            return agentMapper.toDtoWithToken(updatedAgent, token);
        }
        Optional<User> userOptional = userRepository.findById(updateDto.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            HashMap<String, Object> params = updatePerson(user, updateDto);
            User updatedUser = (User) params.get("updatedPerson");
            String token = (String) params.get("token");
            updatedUser.setFirstName(updateDto.getFirstName());
            updatedUser.setLastName(updateDto.getLastName());
            updatedUser.setBirthDate(updateDto.getBirthDate());
            updatedUser.setGender(Gender.valueOfInt(updateDto.getGender()));
            updatedUser = userRepository.save(updatedUser);
            return userMapper.toDtoWithToken(updatedUser, token);
        }
        return null;
    }

    private HashMap<String, Object> updatePerson(Person person, PersonDto updateDto) {
        String token = "";
        if (!person.getUsername().equalsIgnoreCase(updateDto.getUsername())) {
            checkUsername(updateDto.getUsername());
            token = tokenUtils.generateToken(
                    updateDto.getUsername(), person.getId() + "",
                    person.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        }
        if (!person.getEmail().equalsIgnoreCase(updateDto.getEmail())) {
            checkEmail(updateDto.getEmail());
        }
        person.setUsername(updateDto.getUsername());
        person.setEmail(updateDto.getEmail());
        HashMap<String, Object> response = new HashMap<>();
        response.put("updatedPerson", person);
        response.put("token", token);
        return response;
    }

    public PersonDto getPersonById(Long id) {
        Optional<Administrator> admin = administratorRepository.findById(id);
        if (admin.isPresent()) {
            return administratorMapper.toDto(admin.get());
        }
        Optional<Agent> agent = agentRepository.findById(id);
        if (agent.isPresent()) {
            return agentMapper.toDto(agent.get());
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userMapper.toDto(user.get());
        }
        return null;
    }

    public PersonDto getPersonByUsername(String username) {
        Administrator admin = administratorRepository.findByUsername(username);
        if (admin != null) {
            return administratorMapper.toDto(admin);
        }
        Agent agent = agentRepository.findByUsername(username);
        if (agent != null) {
            return agentMapper.toDto(agent);
        }
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return userMapper.toDto(user);
        }
        return null;
    }

    public void checkUsername(String username) {
        boolean usernameExists = checkIfUsernameExists(username);
        if (usernameExists) {
            throw new UsernameAlreadyExists("Username already exists.");
        }
    }

    public void checkEmail(String email) {
        boolean emailExists = checkIfEmailExists(email);
        if (emailExists) {
            throw new EmailAlreadyExists("Email already exists.");
        }
    }

    public boolean checkIfUsernameExists(String username) {
        Person person = personRepository.findByUsernameIgnoreCase(username);
        return person != null;
    }

    public List<PersonDto> filter(String query) {
        UserSpecification spec = new UserSpecification(query);
        return userRepository.findAll(spec).stream().map(u -> userMapper.toDto(u)).collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> findUsersByIds(UserIds userIds) {
        List<PersonDto> users = new ArrayList<>();
        for (Long id: userIds.getIds()) {
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()) {
                PersonDto dto = userMapper.toDto(user.get());
                users.add(dto);
            }
        }
        return users;
    }

    public boolean checkIfEmailExists(String email) {
        Person person = personRepository.findByEmailIgnoreCase(email);
        return person != null;
    }

    private UserJwtToken generateTokenResponse(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // create token
        Person user = (Person) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(
                user.getUsername(), user.getId() + "",
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        PersonDto person = getPersonByUsername(user.getUsername());

        return new UserJwtToken(jwt, person.getId(), person.getRole());
    }
}