package co.jordan.usermicroservices.repository;

import co.jordan.usermicroservices.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsernameOrEmail(String username, String email);
    //Optional<User> findByUsernameOrEmail(String username, String email);
}
