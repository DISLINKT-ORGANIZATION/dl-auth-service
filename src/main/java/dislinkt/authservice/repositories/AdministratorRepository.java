package dislinkt.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dislinkt.authservice.entities.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

	Administrator findByUsername(String username);
	
	Administrator findByEmailOrUsername(String email, String username);

}