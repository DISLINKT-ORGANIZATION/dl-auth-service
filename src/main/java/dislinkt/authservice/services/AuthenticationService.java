package dislinkt.authservice.services;

import dislinkt.authservice.dtos.*;
import org.bouncycastle.LICENSE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthenticationService {

	UserJwtToken loginUser(String username, String password);

	PersonDto registerUser(UserRegistrationRequest request);

	PersonDto createAgent(AgentCreateRequest request);
	
	PersonDto updatePerson(PersonDto updateDto);

	PersonDto getPersonById(Long id);

	PersonDto getPersonByUsername(String username);
	
	boolean checkIfUsernameExists(String username);

    List<PersonDto> filter(String query);
}
