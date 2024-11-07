package com.duongw.universalpetcare.service.impl;

import com.duongw.universalpetcare.event.PasswordResetEvent;
import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.model.VerificationToken;
import com.duongw.universalpetcare.repository.UserRepository;
import com.duongw.universalpetcare.repository.VerificationTokenRepository;
import com.duongw.universalpetcare.service.IResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor

public class ResetPasswordService implements IResetPasswordService {

    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    @Override
    public Optional<User> findUserByPasswordResetToken(String token) {
        return tokenRepository.findByToken(token).map(VerificationToken::getUser);

    }

    @Override
    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email).ifPresentOrElse(user -> {
            PasswordResetEvent passwordResetEvent = new PasswordResetEvent(this, user);
            eventPublisher.publishEvent(passwordResetEvent);
        }, () -> {throw new ResourceNotFoundException("User", "email", email);});

    }

    @Override
    public String resetPassword(String password, User user) {
        try{
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return "Password reset successfully";
        }   catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
