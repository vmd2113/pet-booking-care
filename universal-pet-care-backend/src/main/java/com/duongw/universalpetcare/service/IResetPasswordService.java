package com.duongw.universalpetcare.service;

import com.duongw.universalpetcare.model.User;

import java.util.Optional;

public interface IResetPasswordService {
    Optional<User> findUserByPasswordResetToken(String token);
    void requestPasswordReset(String email);
    String resetPassword(String password, User user);
}
