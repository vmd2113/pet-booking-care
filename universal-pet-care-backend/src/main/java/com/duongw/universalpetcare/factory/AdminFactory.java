package com.duongw.universalpetcare.factory;

import com.duongw.universalpetcare.dto.request.RegistrationRequest;
import com.duongw.universalpetcare.model.Admin;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.repository.AdminRepository;
import com.duongw.universalpetcare.service.user.UserAttributeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminFactory {
    private final UserAttributeMapper userAttributeMapper;
    private final AdminRepository adminRepository;
    public User createAdmin(RegistrationRequest registrationRequest){

        Admin admin = new Admin();
        userAttributeMapper.setCommonAttributes(registrationRequest, admin);
        adminRepository.save(admin);


        return admin;

    }
}
