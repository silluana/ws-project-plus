package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.dto.UserDto;
import com.client.ws.projectplus.exception.BadRequestException;
import com.client.ws.projectplus.exception.NotFoundException;
import com.client.ws.projectplus.mapper.UserMapper;
import com.client.ws.projectplus.model.jpa.SubscriptionType;
import com.client.ws.projectplus.model.jpa.User;
import com.client.ws.projectplus.model.jpa.UserType;
import com.client.ws.projectplus.repository.jpa.SubscriptionTypeRepository;
import com.client.ws.projectplus.repository.jpa.UserRepository;
import com.client.ws.projectplus.repository.jpa.UserTypeRepository;
import com.client.ws.projectplus.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private static final String PNG = ".png";
    private static final String JPEG = ".jpeg";

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final SubscriptionTypeRepository subscriptionTypeRepository;


    public UserServiceImpl(UserRepository userRepository,
                           UserTypeRepository userTypeRepository,
                           SubscriptionTypeRepository subscriptionTypeRepository) {
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

    @Override
    public User uploadPhoto(Long id, MultipartFile file) throws IOException {
        String imgName = file.getOriginalFilename();
        String formatPNG = imgName.substring(imgName.length() - 4);
        String formatJPEG = imgName.substring(imgName.length() - 5);
        if (!(PNG.equalsIgnoreCase(formatPNG) || JPEG.equalsIgnoreCase(formatJPEG))) {
            throw new BadRequestException("Formato de imagem inválido. Use PNG ou JPEG.");
        }

        User user = getUser(id);
        user.setPhotoName(file.getOriginalFilename());
        user.setPhoto(file.getBytes());
        return userRepository.save(user);
    }

    @Override
    public byte[] downloadPhoto(Long id) {
        User user = getUser(id);
        if (Objects.isNull(user.getPhoto())) {
            throw new BadRequestException("Usuário não possui foto cadastrada");
        }
        return user.getPhoto();
    }

    private User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

//        Optional<User> optionalUser = userRepository.findById(id);
//        if (optionalUser.isEmpty())
//            throw new NotFoundException("User não encontrado");
//        return optionalUser.get();
    }

    private UserType getUserType(Long id) {
        return userTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("Tipo de usuário não encontrado"));

//        var optionalUserType = userTypeRepository.findById(id);
//        if(optionalUserType.isEmpty()){
//            throw new NotFoundException("userType não encontrado");
//        }
//        return optionalUserType.get();
    }

    private SubscriptionType getSubscriptionType(Long id) {
        return subscriptionTypeRepository.findById(id).orElseGet(() -> null);

//        var optionalSubscriptionType = subscriptionTypeRepository.findById(id);
//        if (optionalSubscriptionType.isPresent()) {
//            return optionalSubscriptionType.get();
//        }
//        return null;
    }
}
