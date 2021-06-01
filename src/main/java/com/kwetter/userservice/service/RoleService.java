package com.kwetter.userservice.service;

import com.kwetter.userservice.entity.Role;
import com.kwetter.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public void saveRole(Role role){
        roleRepository.save(role);
    }

    public Iterable<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public Optional<Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }
}
