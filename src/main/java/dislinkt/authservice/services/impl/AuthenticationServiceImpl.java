package dislinkt.authservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dislinkt.authservice.dtos.AgentCreateRequest;
import dislinkt.authservice.dtos.JwtToken;
import dislinkt.authservice.dtos.KafkaNotification;
import dislinkt.authservice.dtos.KafkaNotificationType;
import dislinkt.authservice.dtos.PersonDto;
import dislinkt.authservice.dtos.UserRegistrationRequest;
import dislinkt.authservice.entities.Administrator;
import dislinkt.authservice.entities.Agent;
import dislinkt.authservice.entities.Gender;
import dislinkt.authservice.entities.Notification;
import dislinkt.authservice.entities.Person;
import dislinkt.authservice.entities.User;
import dislinkt.authservice.exceptions.EmailAlreadyExists;
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
import java.util.Optional;

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
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private KafkaTemplate<String, KafkaNotification> kafkaTemplate;

	public JwtToken loginUser(String username, String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		return new JwtToken(tokenProvider.generateToken(authentication));
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

		KafkaNotification kafkaNotification = new KafkaNotification((long)newUser.getId(), KafkaNotificationType.REGISTERED_USER);
		kafkaTemplate.send("dislinkt-notifications", kafkaNotification);
		
		return userMapper.toDto(newUser);
	}

	public PersonDto createAgent(AgentCreateRequest request) {

		checkUsername(request.getUsername());
		checkEmail(request.getEmail());

		Agent agent = agentMapper.toEntity(request);
		agent.setPassword(passwordEncoder.encode(request.getPassword()));
		agent.setAuthorities(new ArrayList<>() {
			{
				add(authorityRepository.findByName("ROLE_AGENT"));
			}
		});

		return agentMapper.toDto(agentRepository.save(agent));
	}

	public PersonDto updatePerson(PersonDto updateDto) {

		Optional<Administrator> adminOptional = administratorRepository.findById(updateDto.getId());
		if (adminOptional.isPresent()) {
			Administrator admin = adminOptional.get();
			if (!admin.getUsername().equals(updateDto.getUsername())) {
				checkUsername(updateDto.getUsername());
			}
			if (!admin.getEmail().equals(updateDto.getEmail())) {
				checkEmail(updateDto.getEmail());
			}
			admin.setUsername(updateDto.getUsername());
			admin.setEmail(updateDto.getEmail());
			return administratorMapper.toDto(administratorRepository.save(admin));
		}
		Optional<Agent> agentOptional = agentRepository.findById(updateDto.getId());
		if (agentOptional.isPresent()) {
			Agent agent = agentOptional.get();
			if (!agent.getUsername().equals(updateDto.getUsername())) {
				checkUsername(updateDto.getUsername());
			}
			if (!agent.getEmail().equals(updateDto.getEmail())) {
				checkEmail(updateDto.getEmail());
			}
			agent.setUsername(updateDto.getUsername());
			agent.setEmail(updateDto.getEmail());
			agent.setCompany(updateDto.getCompany());
			return agentMapper.toDto(agentRepository.save(agent));
		}
		Optional<User> userOptional = userRepository.findById(updateDto.getId());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (!user.getUsername().equals(updateDto.getUsername())) {
				checkUsername(updateDto.getUsername());
			}
			if (!user.getEmail().equals(updateDto.getEmail())) {
				checkEmail(updateDto.getEmail());
			}
			user.setUsername(updateDto.getUsername());
			user.setEmail(updateDto.getEmail());
			user.setFirstName(updateDto.getFirstName());
			user.setLastName(updateDto.getLastName());
			user.setBirthDate(updateDto.getBirthDate());
			user.setGender(Gender.valueOfInt(updateDto.getGender()));
			return userMapper.toDto(userRepository.save(user));
		}
		return null;
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
		Person person = personRepository.findByUsername(username);
		if (person != null) {
			return true;
		}
		return false;
	}

	private boolean checkIfEmailExists(String email) {
		Person person = personRepository.findByEmail(email);
		if (person != null) {
			return true;
		}
		return false;
	}
}