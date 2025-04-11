package com.client.ws.projectplus.mapper.wsraspay;

import com.client.ws.projectplus.dto.UserPaymentInfoDto;
import com.client.ws.projectplus.dto.wsraspay.CreditCardDto;

public class CreditCardMapper {

    public static CreditCardDto build(UserPaymentInfoDto dto, String documentNumber){
        return CreditCardDto.builder()
                .documentNumber(documentNumber)
                .cvv(Long.parseLong(dto.getCardSecurityCode()))
                .number(dto.getCardNumber())
                .month(dto.getCardExpirationMonth())
                .year(dto.getCardExpirationYear())
                .installments(dto.getInstallments())
                .build();
    }
}
