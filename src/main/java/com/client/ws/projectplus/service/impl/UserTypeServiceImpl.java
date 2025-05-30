package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.model.jpa.UserType;
import com.client.ws.projectplus.repository.jpa.UserTypeRepository;
import com.client.ws.projectplus.service.UserTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeServiceImpl implements UserTypeService {

    private final UserTypeRepository userTypeRepository;

    public UserTypeServiceImpl(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public List<UserType> findAll() {
        return userTypeRepository.findAll();
    }
}
