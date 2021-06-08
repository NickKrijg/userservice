package com.kwetter.userservice.controller;

import com.kwetter.userservice.entity.Role;
import com.kwetter.userservice.entity.User;
import com.kwetter.userservice.entity.UserDTO;
import com.kwetter.userservice.models.AuthenticationRequest;
import com.kwetter.userservice.models.AuthenticationResponse;
import com.kwetter.userservice.models.UserDetailsImpl;
import com.kwetter.userservice.service.MyUserDetailsService;
import com.kwetter.userservice.service.RoleService;
import com.kwetter.userservice.service.UserService;
import com.kwetter.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticateByUserAndPassword(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
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

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {
        if (userService.existsByUsername(user.getUsername())){
            return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
        }

        var newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());

        //init user
        userService.saveUser(newUser);

        //set roles
        newUser.setRoles(getUserRole());

        //update user
        userService.saveUser(newUser);

        return ResponseEntity.ok(newUser.getUserId());
    }

    private Set<Role> getUserRole() {
        if (roleService.findRoleById("USER").isEmpty()) {
            roleService.saveRole(new Role("USER", "Standard user"));
        }

        return Stream.of(new Role("USER"))
                .collect(Collectors.toSet());
    }
}
