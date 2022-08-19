package dislinkt.authservice.mappers;

import org.springframework.stereotype.Service;

import dislinkt.authservice.dtos.PersonDto;
import dislinkt.authservice.dtos.UserRegistrationRequest;
import dislinkt.authservice.entities.Gender;
import dislinkt.authservice.entities.User;

@Service
public class UserDtoMapper {

	public PersonDto toDto(User user) {
		return new PersonDto(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(),
				user.getBirthDate(), user.getGender().getValue(), user.getAuthorities().get(0).getAuthority());
	}

	public User toEntity(UserRegistrationRequest request) {
		return new User(request.getEmail(), request.getUsername(), request.getFirstName(), request.getLastName(),
				request.getBirthDate(), Gender.valueOfInt(request.getGender()));
	}

}
