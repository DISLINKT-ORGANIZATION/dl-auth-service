package dislinkt.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dislinkt.authservice.entities.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	Authority findByName(String name);

}