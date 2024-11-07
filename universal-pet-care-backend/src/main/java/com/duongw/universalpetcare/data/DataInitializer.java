package com.duongw.universalpetcare.data;


import com.duongw.universalpetcare.model.Admin;
import com.duongw.universalpetcare.model.Patient;
import com.duongw.universalpetcare.model.Role;
import com.duongw.universalpetcare.model.Veterinarian;
import com.duongw.universalpetcare.repository.*;
import com.duongw.universalpetcare.service.impl.RoleService;
import com.duongw.universalpetcare.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



@Component
@Transactional
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final RoleService roleService;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_PATIENT", "ROLE_VET");
//        createDefaultRoleIfNotExits(defaultRoles);
//        createDefaultAdminIfNotExists();
//        createDefaultVetIfNotExits();
//        createDefaultPatientIfNotExits();
    }

    private void createDefaultVetIfNotExits() {
        Role vetRole = roleService.getRoleByName("ROLE_VET");
        for (int i = 1; i <= 10; i++) {
            String defaultEmail = RandomUtils.generateRandomEmail("vet");
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            Veterinarian vet = new Veterinarian();
            vet.setFirstName("Vet");
            vet.setLastName("Number" + i);
            vet.setGender("Not Specified");
            vet.setPhoneNumber(RandomUtils.generateRandomPhoneNumber());
            vet.setEmail(defaultEmail);
            vet.setPassword(passwordEncoder.encode("password123" ));
            vet.setUserType("VET");
            vet.setRoles(new HashSet<>(Collections.singletonList(vetRole)));
            vet.setSpecialization("Dermatologist");
            Veterinarian theVet = veterinarianRepository.save(vet);
            theVet.setEnabled(true);
            System.out.println("Default vet user " + i + " created successfully.");
        }
    }


    private void createDefaultPatientIfNotExits() {
        Role patientRole = roleService.getRoleByName("ROLE_PATIENT");
        for (int i = 1; i <= 10; i++) {
            String defaultEmail = RandomUtils.generateRandomEmail("pat");
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            Patient pat = new Patient();
            pat.setFirstName("Pat");
            pat.setLastName("Patient" + i);
            pat.setGender("Not Specified");
            pat.setPhoneNumber(RandomUtils.generateRandomPhoneNumber());
            pat.setEmail(defaultEmail);
            pat.setPassword(passwordEncoder.encode("password123" ));
            pat.setUserType("PATIENT");
            pat.setRoles(new HashSet<>(Collections.singletonList(patientRole)));
            Patient thePatient = patientRepository.save(pat);
            thePatient.setEnabled(true);
            System.out.println("Default vet user " + i + " created successfully.");
        }
    }


    private void createDefaultAdminIfNotExists() {
        Role adminRole = roleService.getRoleByName("ROLE_ADMIN");
        final String defaultAdminEmail = "admin@email.com";
        if (userRepository.findByEmail(defaultAdminEmail).isPresent()) {
            return;
        }

        Admin admin = new Admin();
        admin.setFirstName("UPC");
        admin.setLastName("Admin 2");
        admin.setGender("Female");
        admin.setPhoneNumber("22222222");
        admin.setEmail(defaultAdminEmail);
        admin.setPassword(passwordEncoder.encode("123456@admin"));
        admin.setUserType("ADMIN");
        admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
        Admin theAdmin = adminRepository.save(admin);
        theAdmin.setEnabled(true);
        System.out.println("Default admin user created successfully.");
    }


    private void createDefaultRoleIfNotExits(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);

    }
}
