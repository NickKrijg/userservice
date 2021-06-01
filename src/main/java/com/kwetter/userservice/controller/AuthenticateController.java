package com.kwetter.userservice.controller;

import com.kwetter.userservice.entity.Role;
import com.kwetter.userservice.entity.User;
import com.kwetter.userservice.models.AuthenticationRequest;
import com.kwetter.userservice.models.AuthenticationResponse;
import com.kwetter.userservice.models.UserDetailsImpl;
import com.kwetter.userservice.service.MyUserDetailsService;
import com.kwetter.userservice.service.UserService;
import com.kwetter.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = {"http://localhost:8080", "*"})
@RequestMapping(path = "/authenticate")
public class AuthenticateController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticateByUserAndPassword(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        Authentication authentication = null;
        System.out.println("authenticate");
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Incorrect username or password", ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final UserDetailsImpl userDetails = myUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        final String jwt = jwtUtil.generateToken(authentication);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())){
            return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
        }
        //init user
        userService.saveUser(user);

        //set roles
        user.setRoles(getUserRole());

        //update user
        userService.saveUser(user);

        return ResponseEntity.ok(user.getUserId());
    }

    private Set<Role> getUserRole() {
        return Stream.of(new Role("USER"))
                .collect(Collectors.toSet());
    }
}
