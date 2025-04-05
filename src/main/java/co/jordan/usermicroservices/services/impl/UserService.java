package co.jordan.usermicroservices.services.impl;

import co.jordan.usermicroservices.dto.RegistrationDto;
import co.jordan.usermicroservices.entities.Role;
import co.jordan.usermicroservices.entities.User;
import co.jordan.usermicroservices.entities.VerificationToken;
import co.jordan.usermicroservices.exceptions.EmailAlreadyExistsException;
import co.jordan.usermicroservices.exceptions.ExpiredTokenException;
import co.jordan.usermicroservices.exceptions.InvalidTokenException;
import co.jordan.usermicroservices.repository.RoleRepository;
import co.jordan.usermicroservices.repository.UserRepository;
import co.jordan.usermicroservices.repository.VerificationTokenRepository;
import co.jordan.usermicroservices.services.IUserService;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmailSender emailSender;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;



    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User addRoleToUser(String username, String rolename) {

        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByRole(rolename);

        user.getRoles().add(role);
       // userRepository.save(user);

        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationDto user) {
        //Optional<User> userOptional = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());

        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new EmailAlreadyExistsException("User already exists");
        }
        User newUser = User.builder()
                .username(user.getUsername())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .enabled(false)
                .build();

        // Set the default role for the new user
        Role r = roleRepository.findByRole("USER");
        List<Role> roles = new ArrayList<>();
        roles.add(r);
        newUser.setRoles(roles);

        String code = this.generateCode();

        VerificationToken token = new VerificationToken(code, newUser);
        verificationTokenRepository.save(token);

        // Send the verification email
        this.sendEmailToUser(newUser, code);

        // Save the new user to the database
        return userRepository.save(newUser);
    }

    @Override
    public String generateCode() {
        Random random = new Random();
        Integer code = 100000 + random.nextInt(900000);

        return code.toString();
    }


    @Override
    public void sendEmailToUser(User u, String code){
        String emailBody = "Bonjour "+"<h1>"+u.getUsername()+"</h1>" +
                "Votre code de vérification est : <h1> " + code + "</h1>";

        emailSender.sendEmail(u.getEmail(), emailBody);

    }


    @Override
    public User validateToken(String code) {
        VerificationToken token = verificationTokenRepository.findByToken(code);
        if (token == null)
            throw new InvalidTokenException("Invalid token");

        User user = token.getUser();
        Calendar c = Calendar.getInstance();
        if (token.getExpirationTime().getTime() - c.getTime().getTime() <= 0) {
            throw new ExpiredTokenException("Token expired");
        }
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(token);
        return user;
    }


}
