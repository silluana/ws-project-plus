package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.exception.BadRequestException;
import com.client.ws.projectplus.model.UserCredentials;
import com.client.ws.projectplus.repository.UserDetailsRepository;
import com.client.ws.projectplus.service.UserDetailsService;
import com.client.ws.projectplus.exception.NotFoundException;
import com.client.ws.projectplus.utils.PasswordUtils;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDetailsRepository userDetailsRepository;

    public UserDetailsServiceImpl(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserCredentials loadUserByUsernameAndPass(String username, String pass) {

        var userCredentialsOpt = userDetailsRepository.findByUsername(username);

        if (userCredentialsOpt.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado");
        }

        UserCredentials userCredentials = userCredentialsOpt.get();

        if (PasswordUtils.matches(pass, userCredentials.getPassword())) {
            return userCredentials;
        }

        throw new BadRequestException("Usuário ou senha inválido");
    }

    /*@Override
    public void sendRecoveryCode(String email) {

    }*/

    /*@Override
    public boolean recoveryCodeIsValid(String recoveryCode, String email) {
        return false;
    }*/
}
