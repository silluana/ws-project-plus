package com.client.ws.projectplus.service;

import com.client.ws.projectplus.dto.PaymentProcessDto;

public interface PaymentInfoService {

    Boolean process(PaymentProcessDto dto);
}
