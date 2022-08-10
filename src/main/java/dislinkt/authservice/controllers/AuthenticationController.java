package dislinkt.authservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dislinkt.authservice.dtos.AgentCreateRequest;
import dislinkt.authservice.dtos.AgentDto;
import dislinkt.authservice.dtos.JwtToken;
import dislinkt.authservice.dtos.LoginRequest;
import dislinkt.authservice.dtos.UserDto;
import dislinkt.authservice.dtos.UserRegistrationRequest;
import dislinkt.authservice.services.AuthenticationService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		JwtToken token = authenticationService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
		return ResponseEntity.ok(token);
	}

	@PostMapping("/register-user")
	public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
		UserDto userDto = authenticationService.registerUser(userRegistrationRequest);
		return ResponseEntity.ok(userDto);
	}

	@PostMapping("/create-agent")
	public ResponseEntity<?> createAgent(@RequestBody AgentCreateRequest agentCreateRequest) {
		AgentDto agentDto = authenticationService.createAgent(agentCreateRequest);
		return ResponseEntity.ok(agentDto);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		UserDto userDto = authenticationService.getUserById(id);
		return ResponseEntity.ok(userDto);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/users/username/{username}")
	public ResponseEntity<?> getUserById(@PathVariable String username) {
		UserDto userDto = authenticationService.getUserByUsername(username);
		return ResponseEntity.ok(userDto);
	}
}
