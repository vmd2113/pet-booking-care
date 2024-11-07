package com.duongw.universalpetcare.factory;

import com.duongw.universalpetcare.dto.request.RegistrationRequest;
import com.duongw.universalpetcare.model.User;

public interface UserFactory {
    public User createUser(RegistrationRequest registrationRequest);
}
