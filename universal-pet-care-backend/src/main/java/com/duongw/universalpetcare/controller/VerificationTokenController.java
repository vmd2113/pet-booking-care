package com.duongw.universalpetcare.controller;

import com.duongw.universalpetcare.dto.request.VerificationTokenRequest;
import com.duongw.universalpetcare.dto.response.ApiResponse;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.model.VerificationToken;
import com.duongw.universalpetcare.repository.UserRepository;
import com.duongw.universalpetcare.service.IVerificationTokenService;
import com.duongw.universalpetcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(UrlMapping.VERIFICATION)
@RequiredArgsConstructor

public class VerificationTokenController {

    private final IVerificationTokenService verificationTokenService;
    private final UserRepository userRepository;


    @GetMapping(path = "/validate-token")
    public ResponseEntity<ApiResponse> validateToken(String token) {
        String result = verificationTokenService.validateToken(token);
        ApiResponse response = switch (result) {
            case "INVALID" -> new ApiResponse(401, "INVALID_TOKEN", null);
            case "VERIFIED" -> new ApiResponse(401, "VERIFIED_TOKEN", null);
            case "EXPIRED" -> new ApiResponse(401, "EXPIRED_TOKEN", null);
            case "VALID" -> new ApiResponse(200, "VALID_VERIFICATION_TOKEN", null);
            default -> new ApiResponse(401, "ERROR", null);
        };
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/check-token-expiration")
    public ResponseEntity<ApiResponse> checkTokenExpiration(String token) {
        boolean isExpired = verificationTokenService.isTokenExpired(token);
        ApiResponse response;
        if (isExpired) {
            response = new ApiResponse(401, "EXPIRED", null);
        } else {
            response = new ApiResponse(200, "VALID", null);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/user/save-token")
    public ResponseEntity<ApiResponse> saveVerificationTokenForUser(@RequestBody VerificationTokenRequest request) {
        User user = userRepository.findById(request.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        verificationTokenService.saveVerificationTokenForUser(request.getToken(), user);
        return ResponseEntity.ok(new ApiResponse(200, "TOKEN_SAVED", null));
    }

    @PutMapping(path = "/generate-new-token")
    public ResponseEntity<ApiResponse> generateNewVerificationToken(@RequestParam String oldToken) {
        VerificationToken newToken = verificationTokenService.generateNewVerificationToken(oldToken);
        return ResponseEntity.ok(new ApiResponse(200, "NEW_TOKEN_GENERATED", newToken));
    }

    @DeleteMapping(path = "/delete-token")
    public ResponseEntity<ApiResponse> deleteUserToken(@RequestParam Long userId) {
        verificationTokenService.deleteVerificationToken(userId);
        return ResponseEntity.ok(new ApiResponse(200, "USER_TOKEN_DELETED", null));
    }


}
