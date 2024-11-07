package com.duongw.universalpetcare.service.user;

import com.duongw.universalpetcare.dto.request.ChangePasswordRequest;

public interface IChangePasswordService {
     void changePassword(Long userId, ChangePasswordRequest changePasswordRequest);


}
