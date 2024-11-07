package com.duongw.universalpetcare.controller;

import com.duongw.universalpetcare.dto.EntityConverter;
import com.duongw.universalpetcare.dto.request.ChangePasswordRequest;
import com.duongw.universalpetcare.dto.request.RegistrationRequest;
import com.duongw.universalpetcare.dto.request.UserUpdateRequest;
import com.duongw.universalpetcare.dto.response.ApiResponse;
import com.duongw.universalpetcare.dto.response.UserDTO;
import com.duongw.universalpetcare.event.RegistrationCompleteEvent;
import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.service.impl.UserService;
import com.duongw.universalpetcare.service.user.IChangePasswordService;
import com.duongw.universalpetcare.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationPushBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

;import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(path = UrlMapping.USERS_URL)
@RequiredArgsConstructor
@Slf4j

@CrossOrigin("http://localhost:5173")

@Tag(name = "user controller")
public class UserController {

    private final UserService userService;
    private final EntityConverter<User, UserDTO> entityConverter;

    private final IChangePasswordService changePasswordService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable(name = "id") Long userId) {
        try {

            UserDTO user = userService.getUserWithDetails(userId);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "User have created successful", user));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), "User not found"));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }


    @GetMapping(path = "/")
    public ResponseEntity<ApiResponse> getAllUser() {
        try {
            List<UserDTO> userDTOS = userService.getAllUsers();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(), "Users have gotten successful", userDTOS));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable(name = "id") Long userId) {
        try {
            User user = userService.getUserById(userId);

            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "User have deleted successful"));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), "User not found"));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }


    @Operation(method = "POST", summary = "Add new user", description = "Send a request via this API to create new user")
    @PostMapping(path = "/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        try {
            User userRegister = userService.createUser(registrationRequest);
            //TODO: send email
            applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(userRegister));


            UserDTO userDTORegister = entityConverter.mapEntityToDTO(userRegister, UserDTO.class);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "User have created successful", userDTORegister));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable(name = "id") Long userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            User userUpdate = userService.updateUser(userId, userUpdateRequest);
            UserDTO userDTOUpdate = entityConverter.mapEntityToDTO(userUpdate, UserDTO.class);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "User have created successful", userDTOUpdate));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PutMapping(path = "/change-password/{id}")
    public ResponseEntity<ApiResponse> changePassword(@PathVariable(name = "id") Long userId, @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            changePasswordService.changePassword(userId, changePasswordRequest);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Change password successful"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping(path = "/count-patients")
    public ResponseEntity<ApiResponse> countPatientByUserType() {
        try {
            Long count = userService.countPatientByUserType();
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Count patient successful", count));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping(path = "/count-veterinarians")
    public ResponseEntity<ApiResponse> countVeterinarianByUserType() {
        try {
            Long count = userService.countVeterinarianByUserType();
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Count  successful", count));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping(path = "/count-users")

    public ResponseEntity<ApiResponse> countAllUser() {
        try {
            Long count = userService.countAllUsers();
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Count user successful", count));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }


    @GetMapping(path = "/aggregated-users")
    public ResponseEntity<ApiResponse> aggregateUsersByMonthAndType() {
        try {
            Map<String, Map<String, Long>> aggregatedUsers = userService.aggregateUsersByMonthAndType();
            return ResponseEntity.ok(new ApiResponse(200, "aggregated users", aggregatedUsers));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(500, e.getMessage(), null));
        }
    }

    @GetMapping("/account/aggregated-by-status")
    public ResponseEntity<ApiResponse> getAggregatedUsersByEnabledStatus() {
        try {
            Map<String, Map<String, Long>> aggregatedData = userService.aggregateUsersByEnabledStatusAndType();
            return ResponseEntity.ok(new ApiResponse(200, "aggregated users", aggregatedData));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(500, e.getMessage(), null));
        }
    }

    @PutMapping("/account/{userId}/lock-user-account")
    public ResponseEntity<ApiResponse> lockUserAccount(@PathVariable Long userId) {
        try {
            userService.lockUserAccount(userId);
            return ResponseEntity.ok(new ApiResponse(200, "User account locked successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(500, e.getMessage(), null));
        }
    }

    @PutMapping("/account/{userId}/unlock-user-account")
    public ResponseEntity<ApiResponse> unLockUserAccount(@PathVariable Long userId) {
        try {
            userService.unLockUserAccount(userId);
            return ResponseEntity.ok(new ApiResponse(200, "User account unlocked successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(500, e.getMessage(), null));
        }
    }


}
