package dislinkt.authservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import dislinkt.authservice.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	
	Optional<User> findById(Long id);

	User findByUsername(String username);
	
	User findByEmail(String email);
}