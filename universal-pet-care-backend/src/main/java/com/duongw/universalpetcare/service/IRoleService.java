package com.duongw.universalpetcare.service;

import com.duongw.universalpetcare.model.Role;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IRoleService {
    List<Role> getAllRoles();
    Role getRoleByName(String name);
    Role getRoleById(Long id);

    Set<Role> setUserRole(String userType);
    void saveRole(Role role);


}
