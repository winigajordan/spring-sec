package co.jordan.usermicroservices.security;

import co.jordan.usermicroservices.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super();
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        User user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        org.springframework.security.core.userdetails.User springUser = ( org.springframework.security.core.userdetails.User ) authResult.getPrincipal();

        List<String> roles = new ArrayList<>();
        springUser.getAuthorities().forEach(
                au -> {
                    roles.add(au.getAuthority());
                }
        );

        String jwt  = JWT.create()
                .withSubject(springUser.getUsername())
                        .withArrayClaim("roles", roles.toArray(new String[roles.size()]))
                                .withExpiresAt(new Date(System.currentTimeMillis()+SecParams.EXP_TIME))
                                        .sign(Algorithm.HMAC256(SecParams.SECRET));


        System.out.println(jwt);
        response.addHeader("Authorization", jwt);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        if (failed instanceof DisabledException){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            Map<String, Object> data = new HashMap<>();
            data.put("errorCause", "disabled");
            data.put("message", "Le compte n'est pas actif");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(data);

            PrintWriter writer = response.getWriter();
            writer.println(json);
            writer.flush();


        } else {
            super.unsuccessfulAuthentication(request, response, failed);
        }

    }

}

