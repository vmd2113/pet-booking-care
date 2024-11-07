package com.duongw.universalpetcare.service;

import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.model.VerificationToken;

import java.util.Optional;

public interface IVerificationTokenService {

    String validateToken(String token);
    void saveVerificationTokenForUser(String token, User user );
    VerificationToken generateNewVerificationToken(String oldToken);
    Optional<VerificationToken> findByToken(String token);
    void deleteVerificationToken(Long tokenId);
    boolean isTokenExpired(String token);
}
