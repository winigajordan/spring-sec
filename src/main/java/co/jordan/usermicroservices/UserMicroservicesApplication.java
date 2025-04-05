package co.jordan.usermicroservices;

import co.jordan.usermicroservices.entities.Role;
import co.jordan.usermicroservices.entities.User;
import co.jordan.usermicroservices.services.impl.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class UserMicroservicesApplication {

	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(UserMicroservicesApplication.class, args);
	}


	@PostConstruct
	void initusers(){
		/*
		userService.addRole(new Role(null, "ADMIN"));
		userService.addRole(new Role(null, "USER"));

		userService.saveUser(new User(null, "admin", "1234", true, null, null));
		userService.saveUser(new User(null, "winiga", "1234", true, null, null));
		userService.saveUser(new User(null, "jordan", "1234", true, null, null));
		userService.saveUser(new User(null, "stephane", "1234", true, null, null));

		userService.addRoleToUser("admin", "ADMIN");
		userService.addRoleToUser("admin", "USER");
		userService.addRoleToUser("winiga", "USER");
		userService.addRoleToUser("jordan", "USER");

		 */

	}



}
