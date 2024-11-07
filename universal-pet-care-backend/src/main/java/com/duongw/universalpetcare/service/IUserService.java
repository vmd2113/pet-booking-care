package com.duongw.universalpetcare.service;

import com.duongw.universalpetcare.dto.request.UserUpdateRequest;
import com.duongw.universalpetcare.dto.response.PatientDTO;
import com.duongw.universalpetcare.dto.request.RegistrationRequest;
import com.duongw.universalpetcare.dto.response.UserDTO;
import com.duongw.universalpetcare.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IUserService {
    User createUser(RegistrationRequest registrationRequest);
    User updateUser(Long id, UserUpdateRequest userUpdateRequest);

    List<UserDTO> getAllUsers();

    User getUserById(Long id);

    void deleteUser(Long id);


    UserDTO getUserWithDetails(Long userId) throws SQLException;

    long countUserByUserType(String userType);

    long countVeterinarianByUserType();

    long countPatientByUserType();

    long countAllUsers();

    Map<String, Map<String,Long>> aggregateUsersByMonthAndType();

    Map<String, Map<String, Long>> aggregateUsersByEnabledStatusAndType();
}
