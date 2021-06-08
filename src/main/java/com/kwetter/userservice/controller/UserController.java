package com.kwetter.userservice.controller;

import com.kwetter.userservice.entity.Role;
import com.kwetter.userservice.entity.User;
import com.kwetter.userservice.exception.InvalidUserReferenceException;
import com.kwetter.userservice.exception.RabbitConnectionException;
import com.kwetter.userservice.rabbit.RabbitMQSender;
import com.kwetter.userservice.service.RoleService;
import com.kwetter.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:8080", "*"})
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    RabbitMQSender rabbitMQSender;

    @GetMapping("/hello")
    public String helloWorld(){
        return "Hello worlds";
    }

    @PostMapping("/createRole")
    public String createRole(@RequestBody Role role) {
        roleService.saveRole(role);
        return role.getRole();
    }
    @GetMapping("/viewAllRoles")
    public Iterable<Role> getAllRoles(){
        return roleService.getAllRoles();
    }

    @GetMapping("/viewAll")
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/view/{username}")
    public User getUserById(@PathVariable("username") String username) throws InvalidUserReferenceException {
        return userService.findUserByUsername(username)
                .orElseThrow(() -> new InvalidUserReferenceException("User Not Found with username: " + username));
    }

    @GetMapping("/view/me")
    public User getUserByMe() throws InvalidUserReferenceException {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService.findUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new InvalidUserReferenceException("User Not Found with username: " + userDetails.getUsername()));
    }

    @PatchMapping("view/me")
    public User patchMe(@RequestBody User newUser) {
        //Load and save approach
        var user = getUserByMe();

        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());

        userService.saveUser(user);
        return user;
    }

    @GetMapping("/search/{query}")
    public List<String> searchUsernames(@PathVariable("query") String query) {
        return userService.searchUsernames(query);
    }

    @Transactional
    @DeleteMapping("/forget")
    public ResponseEntity<Integer> forgetUser() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            rabbitMQSender.send(userDetails.getUsername());
        } catch (Exception ex) {
            throw new RabbitConnectionException("User will not be forgotten - could not connect with RabbitMQ");
        }

        try {
            return ResponseEntity.ok(userService.forgetUser(userDetails.getUsername()));
        } catch (Exception ex) {
            throw new InvalidUserReferenceException("User not found");
        }
    }
}
