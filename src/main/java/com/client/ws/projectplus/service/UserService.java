package com.client.ws.projectplus.service;

import com.client.ws.projectplus.dto.UserDto;
import com.client.ws.projectplus.model.jpa.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    User create(UserDto dto);

    User update(Long id, UserDto dto);

    void delete(Long id);
}
