package dislinkt.authservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dislinkt.authservice.dtos.AgentCreateRequest;
import dislinkt.authservice.dtos.AgentDto;
import dislinkt.authservice.dtos.JwtToken;
import dislinkt.authservice.dtos.UserDto;
import dislinkt.authservice.dtos.UserRegistrationRequest;
import dislinkt.authservice.entities.Agent;
import dislinkt.authservice.entities.User;
import dislinkt.authservice.exceptions.UsernameOrEmailAlreadyExists;
import dislinkt.authservice.mappers.AgentDtoMapper;
import dislinkt.authservice.mappers.UserDtoMapper;
import dislinkt.authservice.repositories.AdministratorRepository;
import dislinkt.authservice.repositories.AgentRepository;
import dislinkt.authservice.repositories.AuthorityRepository;
import dislinkt.authservice.repositories.UserRepository;
import dislinkt.authservice.services.AuthenticationService;

import java.util.ArrayList;

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
	private AuthorityRepository authorityRepository;

	@Autowired
	private UserDtoMapper userMapper;

	@Autowired
	private AgentDtoMapper agentMapper;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	public JwtToken loginUser(String username, String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		return new JwtToken(tokenProvider.generateToken(authentication));
	}

	public UserDto registerUser(UserRegistrationRequest request) {

		checkEmailAndUsername(request.getEmail(), request.getUsername());

		User user = userMapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setAuthorities(new ArrayList<>() {
			{
				add(authorityRepository.findByName("ROLE_USER"));
			}
		});

		return userMapper.toDto(userRepository.save(user));
	}

	public AgentDto createAgent(AgentCreateRequest request) {

		checkEmailAndUsername(request.getEmail(), request.getUsername());

		Agent agent = agentMapper.toEntity(request);
		agent.setPassword(passwordEncoder.encode(request.getPassword()));
		agent.setAuthorities(new ArrayList<>() {
			{
				add(authorityRepository.findByName("ROLE_AGENT"));
			}
		});

		return agentMapper.toDto(agentRepository.save(agent));
	}

	public UserDto getUserById(Long id) {
		User user = userRepository.findById(id).orElseGet(null);
		if (user != null) {
			return userMapper.toDto(user);
		} else {
			return null;
		}
	}

	public UserDto getUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			return userMapper.toDto(user);
		} else {
			return null;
		}
	}

	private void checkEmailAndUsername(String email, String username) {
		UserDetails person = administratorRepository.findByEmailOrUsername(email, username);
		if (person != null) {
			throw new UsernameOrEmailAlreadyExists("Username or email already exists.");
		}
		person = agentRepository.findByEmailOrUsername(email, username);
		if (person != null) {
			throw new UsernameOrEmailAlreadyExists("Username or email already exists.");
		}
		person = userRepository.findByEmailOrUsername(email, username);
		if (person != null) {
			throw new UsernameOrEmailAlreadyExists("Username or email already exists.");
		}
	}
}