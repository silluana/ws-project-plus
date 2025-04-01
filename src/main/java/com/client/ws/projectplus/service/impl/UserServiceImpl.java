package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.dto.UserDto;
import com.client.ws.projectplus.exception.BadRequestException;
import com.client.ws.projectplus.exception.NotFoundException;
import com.client.ws.projectplus.mapper.UserMapper;
import com.client.ws.projectplus.model.SubscriptionType;
import com.client.ws.projectplus.model.User;
import com.client.ws.projectplus.model.UserType;
import com.client.ws.projectplus.repository.SubscriptionTypeRepository;
import com.client.ws.projectplus.repository.UserRepository;
import com.client.ws.projectplus.repository.UserTypeRepository;
import com.client.ws.projectplus.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final SubscriptionTypeRepository subscriptionTypeRepository;

    public UserServiceImpl(UserRepository userRepository, UserTypeRepository userTypeRepository, SubscriptionTypeRepository subscriptionTypeRepository) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.subscriptionTypeRepository = subscriptionTypeRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return getUser(id);
    }

    @Override
    public User create(UserDto dto) {

        if (Objects.nonNull(dto.getId())) {
            throw new BadRequestException("id deve ser nulo");
        }

        UserType userType = getUserType(dto.getUserTypeId());

        User user = UserMapper.fromDtoToEntity(dto, userType, null);

        return userRepository.save(user);
    }

    @Override
    public User update(Long id, UserDto dto) {
        getUser(id);
        dto.setId(id);

        UserType userType = getUserType(dto.getUserTypeId());
        SubscriptionType subscriptionType = getSubscriptionType(dto.getSubscriptionTypeId());

        return userRepository.save(UserMapper.fromDtoToEntity(dto, userType, subscriptionType));
    }

    @Override
    public void delete(Long id) {
        getUser(id);
        userRepository.deleteById(id);
    }

    private User getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new NotFoundException("User não encontrado");
        return optionalUser.get();
    }

    private UserType getUserType(Long id) {
        var optionalUserType = userTypeRepository.findById(id);
        if(optionalUserType.isEmpty()){
            throw new NotFoundException("userType não encontrado");
        }
        return optionalUserType.get();
    }

    private SubscriptionType getSubscriptionType(Long id) {
        var optionalSubscriptionType = subscriptionTypeRepository.findById(id);
        if(optionalSubscriptionType.isEmpty()){
            return null;
        }
        return optionalSubscriptionType.get();
    }
}
