package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.dto.UserDto;
import com.client.ws.projectplus.exception.BadRequestException;
import com.client.ws.projectplus.exception.NotFoundException;
import com.client.ws.projectplus.mapper.UserMapper;
import com.client.ws.projectplus.model.User;
import com.client.ws.projectplus.repository.UserRepository;
import com.client.ws.projectplus.repository.UserTypeRepository;
import com.client.ws.projectplus.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;

    public UserServiceImpl(UserRepository userRepository, UserTypeRepository userTypeRepository) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public User create(UserDto dto) {

        if (Objects.nonNull(dto.getId())) {
            throw new BadRequestException("id deve ser nulo");
        }

        var userTypeOpt = userTypeRepository.findById(dto.getUserTypeId());
        if(userTypeOpt.isEmpty()){
            throw new NotFoundException("userType não encontrado");
        }

        User user = UserMapper.fromDtoToEntity(dto, userTypeOpt.get(), null);

        return userRepository.save(user);
    }
}
