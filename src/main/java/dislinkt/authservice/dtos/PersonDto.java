package dislinkt.authservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

	private Long id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private Long birthDate;
	private int gender;
	private String company;
	private String role;
	// when username changes
	private String token;

	public PersonDto(Long id, String username, String email, String role) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
	}

	public PersonDto(Long id, String username, String email, String firstName, String lastName,
			Long birthDate, int gender, String role) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.gender = gender;
	}

	public PersonDto(Long id, String username, String email, String company, String role) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
		this.company = company;
	}

	public PersonDto(Long id, String username, String email, String firstName, String lastName,
					 Long birthDate, int gender, String role, String token) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.gender = gender;
		this.token = token;
	}


	public PersonDto(Long id, String username, String email, String company, String role, String token) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.company = company;
		this.role = role;
		this.token = token;
	}
}