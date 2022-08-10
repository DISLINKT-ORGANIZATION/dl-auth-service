package dislinkt.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dislinkt.authservice.entities.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

	Agent findByUsername(String username);
	
	Agent findByEmailOrUsername(String email, String username);

}