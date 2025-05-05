package com.client.ws.projectplus.service;

import com.client.ws.projectplus.dto.LoginDto;
import com.client.ws.projectplus.dto.TokenDto;

public interface AuthenticationService {

    TokenDto auth(LoginDto loginDto);
}
