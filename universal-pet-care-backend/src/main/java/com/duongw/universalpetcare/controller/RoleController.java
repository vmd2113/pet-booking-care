package com.duongw.universalpetcare.controller;

import com.duongw.universalpetcare.model.Role;
import com.duongw.universalpetcare.service.IRoleService;
import com.duongw.universalpetcare.utils.UrlMapping;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlMapping.ROLE_URL)
@Tag(name = "role controller")
public class RoleController {

    private final IRoleService roleService;

    @GetMapping(path = "/")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping(path = "/name")

    public Role getRoleByName(String name) {
        return roleService.getRoleByName(name);
    }


    @GetMapping(path = "/id")
    public Role getRoleById(Long id) {
        return roleService.getRoleById(id);
    }


}
