package com.client.ws.projectplus.mapper;

import com.client.ws.projectplus.dto.UserPaymentInfoDto;
import com.client.ws.projectplus.model.jpa.User;
import com.client.ws.projectplus.model.jpa.UserPaymentInfo;

public class UserPaymentInfoMapper {

    public static UserPaymentInfo fromDtoToEntity(UserPaymentInfoDto dto, User user) {
        return UserPaymentInfo.builder()
                .id(dto.getId())
                .cardNumber(dto.getCardNumber())
                .cardExpirationMonth(dto.getCardExpirationMonth())
                .cardExpirationYear(dto.getCardExpirationYear())
                .cardSecurityCode(dto.getCardSecurityCode())
                .price(dto.getPrice())
                .dtPayment(dto.getDtPayment())
                .installments(dto.getInstallments())
                .user(user)
                .build();
    }
}
