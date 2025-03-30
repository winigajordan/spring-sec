package co.jordan.usermicroservices.controller;

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

}
