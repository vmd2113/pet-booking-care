package com.duongw.universalpetcare.service.impl;

import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.model.Role;
import com.duongw.universalpetcare.repository.RoleRepository;
import com.duongw.universalpetcare.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;


    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Role", "Name", name));
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "Id", id.toString()));
    }

    @Override
    public Set<Role> setUserRole(String userType) {
        Set<Role> userRoles = new HashSet<>();
        roleRepository.findByName("ROLE_"+userType)
                .ifPresentOrElse(userRoles::add, () -> {
                    throw new ResourceNotFoundException("Role", "Name", "ROLE_"+userType);
                });
        return userRoles;
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);

    }
}
