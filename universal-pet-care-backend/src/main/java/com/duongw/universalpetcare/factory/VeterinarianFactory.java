package com.duongw.universalpetcare.factory;

import com.duongw.universalpetcare.dto.request.RegistrationRequest;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.model.Veterinarian;
import com.duongw.universalpetcare.repository.VeterinarianRepository;
import com.duongw.universalpetcare.service.impl.RoleService;
import com.duongw.universalpetcare.service.user.UserAttributeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VeterinarianFactory {
    private final VeterinarianRepository veterinarianRepository;
    private final UserAttributeMapper userAttributeMapper;
    private final RoleService roleService;

    public User createVeterinarian(RegistrationRequest registrationRequest) {
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setRoles(roleService.setUserRole("VET"));
        userAttributeMapper.setCommonAttributes(registrationRequest, veterinarian);
        veterinarian.setSpecialization(registrationRequest.getSpecialization());
        veterinarianRepository.save(veterinarian);
        return veterinarian;
    }
}
