package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.dto.LoginDto;
import com.client.ws.projectplus.dto.TokenDto;
import com.client.ws.projectplus.exception.BadRequestException;
import com.client.ws.projectplus.model.jpa.UserCredentials;
import com.client.ws.projectplus.service.AuthenticationService;
import com.client.ws.projectplus.service.TokenService;
import com.client.ws.projectplus.service.UserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDetailsService userDetailsService;

    private final TokenService tokenService;

    public AuthenticationServiceImpl(UserDetailsService userDetailsService, TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    public TokenDto auth(LoginDto dto) {
        try {
            UserCredentials userCredentials = userDetailsService.loadUserByUsernameAndPass(dto.getUsername(), dto.getPassword());
            String token = tokenService.getToken(userCredentials.getId());
            return TokenDto.builder().token(token).type("Bearer").build();
        } catch (Exception e) {
            throw new BadRequestException("Erro ao formatar token - "+e.getMessage());
        }
    }
}
