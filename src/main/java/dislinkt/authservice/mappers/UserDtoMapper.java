package dislinkt.authservice.mappers;

import org.springframework.stereotype.Service;

import dislinkt.authservice.dtos.UserDto;
import dislinkt.authservice.dtos.UserRegistrationRequest;
import dislinkt.authservice.entities.Gender;
import dislinkt.authservice.entities.User;

@Service
public class UserDtoMapper {

	public UserDto toDto(User user) {
		return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(),
				user.getBirthDate(), user.getGender().name());
	}

	public User toEntity(UserRegistrationRequest request) {
		return new User(request.getEmail(), request.getUsername(), request.getFirstName(), request.getLastName(),
				request.getBirthDate(), Gender.valueOf(request.getGender()));
	}

}
