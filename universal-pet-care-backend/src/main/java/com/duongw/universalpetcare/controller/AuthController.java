package com.duongw.universalpetcare.controller;

import com.duongw.universalpetcare.dto.request.LoginRequest;
import com.duongw.universalpetcare.dto.request.PasswordResetRequest;
import com.duongw.universalpetcare.dto.response.ApiResponse;
import com.duongw.universalpetcare.dto.response.JwtResponse;
import com.duongw.universalpetcare.event.RegistrationCompleteEvent;
import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.model.VerificationToken;
import com.duongw.universalpetcare.security.jwt.JwtUtils;
import com.duongw.universalpetcare.security.user.UPCUserDetail;
import com.duongw.universalpetcare.service.impl.ResetPasswordService;
import com.duongw.universalpetcare.service.impl.VerificationTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")

public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    private final VerificationTokenService tokenService;
    private final ResetPasswordService resetPasswordService;
    private final ApplicationEventPublisher publisher;


    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            UPCUserDetail upcUserDetail = (UPCUserDetail) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(upcUserDetail.getId(), jwt);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.CREATED.value(), "Login success", jwtResponse));


        } catch (DisabledException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Sorry, your account is disabled", null));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Authentication failed", null));
        }

    }

    @GetMapping(path = "/auth/verify-your-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
        String result = tokenService.validateToken(token);
        return switch (result) {
            case "VALID" -> ResponseEntity.ok(new ApiResponse(200, "VALID_TOKEN", null));
            case "VERIFIED" -> ResponseEntity.ok(new ApiResponse(200, "VERIFIED", null));
            case "EXPIRED" -> ResponseEntity.status(HttpStatus.GONE).body(new ApiResponse(400, "EXPIRED", null));
            case "INVALID" -> ResponseEntity.status(HttpStatus.GONE).body(new ApiResponse(400, "INVALID", null));
            default -> ResponseEntity.internalServerError().body(new ApiResponse(500, "ERROR", null));

        };
    }




    @PutMapping(path = "/auth/resend-verification-token")
    public ResponseEntity<ApiResponse> resendVerificationToken(@RequestParam("token") String oldToken) {
        try {
            VerificationToken verificationToken = tokenService.generateNewVerificationToken(oldToken);
            User theUser = verificationToken.getUser();
            publisher.publishEvent(new RegistrationCompleteEvent(theUser));
            return ResponseEntity.ok(new ApiResponse(200, "Verification token resend successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(500, e.getMessage(), null));
        }
    }

    @PostMapping(path = "/auth/request-password-reset")
    public ResponseEntity<ApiResponse> requestPasswordReset(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, "Email is required", null));
        }
        try {
            resetPasswordService.requestPasswordReset(email);
            return ResponseEntity.
                    ok(new ApiResponse(200, "Password reset request sent to your email successfully, please check and confirm this link", null));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(404, ex.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(500, e.getMessage(), null));
        }
    }

    @PostMapping(path = "/auth/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody PasswordResetRequest request) {
        String token = request.getToken();
        String newPassword = request.getNewPassword();
        if (token == null || token.trim().isEmpty() || newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "All fields are required", null));
        }
        Optional<User> theUser = resetPasswordService.findUserByPasswordResetToken(token);
        if (theUser.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Invalid token", null));
        }
        User user = theUser.get();
        String message = resetPasswordService.resetPassword(newPassword, user);
        return ResponseEntity.ok(new ApiResponse(200, message, null));
    }


}
