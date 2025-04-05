package co.jordan.usermicroservices.security;

import co.jordan.usermicroservices.entities.User;
import co.jordan.usermicroservices.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsServices implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user==null)
            throw new UsernameNotFoundException("Utilisateur introuvable !");

        List<GrantedAuthority> auths = new ArrayList<>();

        user.getRoles().forEach(role -> {
            GrantedAuthority auhority = new SimpleGrantedAuthority(role.getRole());
            auths.add(auhority);
        });

        return new org.springframework.security.core.
                userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        user.getEnabled(),
                        true,
                        true,
                        true,
                        auths
        );
    }
}