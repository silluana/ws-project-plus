package com.client.ws.projectplus.service;

import com.client.ws.projectplus.model.jpa.UserType;

import java.util.List;

public interface UserTypeService {

    List<UserType> findAll();
}
