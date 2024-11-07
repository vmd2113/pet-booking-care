package com.duongw.universalpetcare.factory;

import com.duongw.universalpetcare.dto.request.RegistrationRequest;
import com.duongw.universalpetcare.exception.UserAlreadyExistedException;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.repository.UserRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SimpleUserFactory implements UserFactory {
    private final UserRepository userRepository;
    private final VeterinarianFactory veterinarianFactory;
    private final AdminFactory adminFactory;
    private final PatientFactory patientFactory;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(RegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistedException("User " + registrationRequest.getEmail() + "already existed");
        }
        registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        switch (registrationRequest.getUserType()) {
            case "VET": {
                return veterinarianFactory.createVeterinarian(registrationRequest);
            }
            case "ADMIN": {
                return adminFactory.createAdmin(registrationRequest);
            }
            case "PATIENT": {
                return patientFactory.createPatient(registrationRequest);
            }
            default: {
                return null;

            }
        }


    }
}
