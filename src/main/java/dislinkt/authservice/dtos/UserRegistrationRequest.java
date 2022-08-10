package dislinkt.authservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String birthDate;
	private String gender;
}