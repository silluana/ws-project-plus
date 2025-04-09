package com.client.ws.projectplus.mapper.wsraspay;

import com.client.ws.projectplus.dto.PaymentProcessDto;
import com.client.ws.projectplus.dto.wsraspay.OrderDto;

public class OrderMapper {

    public static OrderDto build(String customerId, PaymentProcessDto paymentProcessDto){
        return OrderDto.builder()
                .customerId(customerId)
                .productAcronym(paymentProcessDto.getProductKey())
                .discount(paymentProcessDto.getDiscount())
                .build();
    }
}
