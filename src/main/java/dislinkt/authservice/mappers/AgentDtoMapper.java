package dislinkt.authservice.mappers;

import org.springframework.stereotype.Service;

import dislinkt.authservice.dtos.AgentCreateRequest;
import dislinkt.authservice.dtos.AgentDto;
import dislinkt.authservice.entities.Agent;

@Service
public class AgentDtoMapper {

	public AgentDto toDto(Agent agent) {
		return new AgentDto(agent.getId(), agent.getUsername(), agent.getEmail());
	}

	public Agent toEntity(AgentCreateRequest request) {
		return new Agent(request.getEmail(), request.getUsername());
	}

}
