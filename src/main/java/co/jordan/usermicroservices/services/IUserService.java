package co.jordan.usermicroservices.services;

import co.jordan.usermicroservices.dto.RegistrationDto;
import co.jordan.usermicroservices.entities.Role;
import co.jordan.usermicroservices.entities.User;

import java.util.List;

public interface IUserService {
    User saveUser(User user);
    User findByUsername(String username);
    Role addRole(Role role);
    User addRoleToUser(String username, String rolename);
    List<User> findAllUsers();
    User registerUser(RegistrationDto user);

    String generateCode();
    User validateToken(String token);

    void sendEmailToUser(User u, String code);
}
