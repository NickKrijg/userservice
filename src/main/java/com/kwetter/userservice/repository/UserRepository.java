package com.kwetter.userservice.repository;

import com.kwetter.userservice.entity.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> getUserByUsername(String name);

    boolean existsByUsername(String name);

    Iterable<User> findUsersByUsernameContaining(String username);

    Integer deleteByUsername(String username);
}
