package com.client.ws.projectplus.service;

import com.client.ws.projectplus.dto.UserDetailsDto;
import com.client.ws.projectplus.model.jpa.UserCredentials;

public interface UserDetailsService {

    UserCredentials loadUserByUsernameAndPass(String username, String pass);

    void sendRecoveryCode(String email);

    boolean recoveryCodeIsValid(String recoveryCode, String email);

    void updatePasswordByRecoveryCode(UserDetailsDto userDetailsDto);
}
