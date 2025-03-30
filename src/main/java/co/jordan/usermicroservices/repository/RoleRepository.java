package co.jordan.usermicroservices.repository;

import co.jordan.usermicroservices.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
