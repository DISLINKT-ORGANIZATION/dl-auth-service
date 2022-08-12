package dislinkt.authservice.mappers;

import org.springframework.stereotype.Service;

import dislinkt.authservice.dtos.AgentCreateRequest;
import dislinkt.authservice.dtos.PersonDto;
import dislinkt.authservice.entities.Agent;

@Service
public class AgentDtoMapper {

	public PersonDto toDto(Agent agent) {
		return new PersonDto(agent.getId(), agent.getUsername(), agent.getEmail(), agent.getCompany(),
				agent.getAuthorities().get(0).getAuthority());
	}

	public Agent toEntity(AgentCreateRequest request) {
		return new Agent(request.getEmail(), request.getUsername(), request.getCompany());
	}

}
