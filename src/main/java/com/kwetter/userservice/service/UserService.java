package com.kwetter.userservice.service;

import com.kwetter.userservice.entity.User;
import com.kwetter.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void saveUser(User user){
        userRepository.save(user);
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public List<String> searchUsernames(String search) {
        Iterable<User> users = userRepository.findUsersByUsernameContaining(search);
        List<String> usernames = new ArrayList<>();
        users.forEach( user ->
                usernames.add(user.getUsername())
        );
        return usernames;
    }

    public Integer forgetUser(String username) {
        return userRepository.deleteByUsername(username);
    }
}
