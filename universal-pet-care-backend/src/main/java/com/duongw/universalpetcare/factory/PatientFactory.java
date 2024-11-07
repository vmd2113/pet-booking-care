package com.duongw.universalpetcare.factory;

import com.duongw.universalpetcare.dto.request.RegistrationRequest;
import com.duongw.universalpetcare.model.Patient;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.repository.PatientRepository;
import com.duongw.universalpetcare.service.IRoleService;
import com.duongw.universalpetcare.service.user.UserAttributeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientFactory {
    private final UserAttributeMapper userAttributeMapper;
    private final PatientRepository patientRepository;
    private final IRoleService roleService;

    public User createPatient(RegistrationRequest registrationRequest){
        Patient patient = new Patient();
        patient.setRoles(roleService.setUserRole("PATIENT"));
        userAttributeMapper.setCommonAttributes(registrationRequest, patient);
        patientRepository.save(patient);
        return patient;
    }
}
