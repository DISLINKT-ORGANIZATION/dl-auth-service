package dislinkt.authservice.services;

import dislinkt.authservice.dtos.AgentCreateRequest;
import dislinkt.authservice.dtos.AgentDto;
import dislinkt.authservice.dtos.JwtToken;
import dislinkt.authservice.dtos.UserDto;
import dislinkt.authservice.dtos.UserRegistrationRequest;

public interface AuthenticationService {

	JwtToken loginUser(String username, String password);

	UserDto registerUser(UserRegistrationRequest request);

	AgentDto createAgent(AgentCreateRequest request);

	UserDto getUserById(Long id);

	UserDto getUserByUsername(String username);

}
