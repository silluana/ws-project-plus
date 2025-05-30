package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.exception.BadRequestException;
import com.client.ws.projectplus.integration.MailIntegration;
import com.client.ws.projectplus.model.jpa.UserCredentials;
import com.client.ws.projectplus.model.redis.UserRecoveryCode;
import com.client.ws.projectplus.repository.jpa.UserDetailsRepository;
import com.client.ws.projectplus.repository.redis.UserRecoveryCodeRepository;
import com.client.ws.projectplus.service.UserDetailsService;
import com.client.ws.projectplus.exception.NotFoundException;
import com.client.ws.projectplus.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${webservice.projectplus.redis.recoverycode.timeout}")
    private String recoveryCodeTimeout;

    private final UserDetailsRepository userDetailsRepository;
    private final UserRecoveryCodeRepository userRecoveryCodeRepository;
    private final MailIntegration mailIntegration;

    public UserDetailsServiceImpl(UserDetailsRepository userDetailsRepository, UserRecoveryCodeRepository userRecoveryCodeRepository, MailIntegration mailIntegration) {
        this.userDetailsRepository = userDetailsRepository;
        this.userRecoveryCodeRepository = userRecoveryCodeRepository;
        this.mailIntegration = mailIntegration;
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

    @Override
    public void sendRecoveryCode(String email) {
        
        UserRecoveryCode userRecoveryCode;
        String code = String.format("%04d", new Random().nextInt(10000));

        var userRecoveryCodeOpt = userRecoveryCodeRepository.findByEmail(email);

        if (userRecoveryCodeOpt.isEmpty()) {
            var user = userDetailsRepository.findByUsername(email);

            if (user.isEmpty()) {
                throw new NotFoundException("Usuário não encontrado");
            }

            userRecoveryCode = new UserRecoveryCode();
            userRecoveryCode.setEmail(email);

        } else {
            userRecoveryCode = userRecoveryCodeOpt.get();
        }

        userRecoveryCode.setCode(code);
        userRecoveryCode.setCreationDate(LocalDateTime.now());

        userRecoveryCodeRepository.save(userRecoveryCode);

        mailIntegration.send(email, "Recuperação de senha",
                "Seu código de recuperação é: " + code + ". O código é válido por 5 minutos.");
    }

    @Override
    public boolean recoveryCodeIsValid(String recoveryCode, String email) {

        var userRecoveryCodeOpt = userRecoveryCodeRepository.findByEmail(email);

        if (userRecoveryCodeOpt.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado");
        }

        UserRecoveryCode userRecoveryCode = userRecoveryCodeOpt.get();

        LocalDateTime timeout = userRecoveryCode.getCreationDate().plusMinutes(Long.parseLong(recoveryCodeTimeout));
        LocalDateTime now = LocalDateTime.now();

        return recoveryCode.equals(userRecoveryCode.getCode()) && now.isBefore(timeout);
    }
}
