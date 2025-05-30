package com.client.ws.projectplus.mapper;

import com.client.ws.projectplus.dto.UserDto;
import com.client.ws.projectplus.model.jpa.SubscriptionType;
import com.client.ws.projectplus.model.jpa.User;
import com.client.ws.projectplus.model.jpa.UserType;

public class UserMapper {

    public static User fromDtoToEntity(UserDto dto, UserType userType, SubscriptionType subscriptionType) {
        return User.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .cpf(dto.getCpf())
                        .email(dto.getEmail())
                        .phone(dto.getPhone())
                        .dtSubscription(dto.getDtSubscription())
                        .dtExpiration(dto.getDtExpiration())
                        .userType(userType)
                        .subscriptionType(subscriptionType)
                .build();
    }
}
