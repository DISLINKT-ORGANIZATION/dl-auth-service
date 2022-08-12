package dislinkt.authservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dislinkt.authservice.dtos.AgentCreateRequest;
import dislinkt.authservice.dtos.JwtToken;
import dislinkt.authservice.dtos.LoginRequest;
import dislinkt.authservice.dtos.PersonDto;
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
		PersonDto userDto = authenticationService.registerUser(userRegistrationRequest);
		return ResponseEntity.ok(userDto);
	}

	@PostMapping("/create-agent")
	public ResponseEntity<?> createAgent(@RequestBody AgentCreateRequest agentCreateRequest) {
		PersonDto agentDto = authenticationService.createAgent(agentCreateRequest);
		return ResponseEntity.ok(agentDto);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_AGENT', 'ROLE_ADMINISTRATOR')")
	@PutMapping("/users/update-person")
	public ResponseEntity<?> updatePerson(@RequestBody PersonDto updateDto) {
		PersonDto userDto = authenticationService.updatePerson(updateDto);
		return ResponseEntity.ok(userDto);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_AGENT', 'ROLE_ADMINISTRATOR')")
	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		PersonDto userDto = authenticationService.getPersonById(id);
		return ResponseEntity.ok(userDto);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_AGENT', 'ROLE_ADMINISTRATOR')")
	@GetMapping("/users/username/{username}")
	public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
		PersonDto userDto = authenticationService.getPersonByUsername(username);
		return ResponseEntity.ok(userDto);
	}

	@GetMapping("/users/check-username/{username}")
	public ResponseEntity<Boolean> checkIfUsernameExists(@PathVariable String username) {
		Boolean userExists = authenticationService.checkIfUsernameExists(username);
		return ResponseEntity.ok(userExists);
	}
}
