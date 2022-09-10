package dislinkt.authservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentDto {

	private Long id;
	private String username;
	private String email;
	private String role;
	// when username changes
	private String token;

	public AgentDto(Long id, String username, String email, String role) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
	}

}