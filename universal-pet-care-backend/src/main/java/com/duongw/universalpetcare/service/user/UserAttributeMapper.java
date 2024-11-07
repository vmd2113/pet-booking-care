package com.duongw.universalpetcare.service.user;

import com.duongw.universalpetcare.dto.request.RegistrationRequest;
import com.duongw.universalpetcare.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserAttributeMapper {
    public void setCommonAttributes(RegistrationRequest registrationRequest, User user){
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setPhoneNumber(registrationRequest.getPhoneNumber());
        user.setId(registrationRequest.getId());
        user.setGender(registrationRequest.getGender());
        user.setEnabled(registrationRequest.isEnabled());
        user.setPassword(registrationRequest.getPassword());
        user.setUserType(registrationRequest.getUserType());
        user.setEmail(registrationRequest.getEmail());

    }
}
