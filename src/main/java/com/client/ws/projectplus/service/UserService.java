package com.client.ws.projectplus.service;

import com.client.ws.projectplus.dto.UserDto;
import com.client.ws.projectplus.model.User;

public interface UserService {

    User create(UserDto dto);
}
