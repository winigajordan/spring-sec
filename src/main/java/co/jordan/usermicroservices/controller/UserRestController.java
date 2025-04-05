package co.jordan.usermicroservices.controller;

import co.jordan.usermicroservices.dto.RegistrationDto;
import co.jordan.usermicroservices.entities.User;
import co.jordan.usermicroservices.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserRestController {

    @Autowired
    UserService userService;

    //@RequestMapping(path = "all", method = RequestMethod.GET)
    @GetMapping(path = "/all")
    public List<User> getAllUsers(){
        return userService.findAllUsers();
    }

    @PostMapping(path = "/register")
    public User register(@RequestBody  RegistrationDto user){
        System.out.println(user);
        return userService.registerUser(user);
    }

    @GetMapping(path = "/validate/{token}")
    public User verify(@PathVariable("token") String token){
        return userService.validateToken(token);
    }

}
