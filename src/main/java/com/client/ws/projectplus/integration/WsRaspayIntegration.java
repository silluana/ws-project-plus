package com.client.ws.projectplus.integration;

import com.client.ws.projectplus.dto.wsraspay.CustomerDto;
import com.client.ws.projectplus.dto.wsraspay.OrderDto;
import com.client.ws.projectplus.dto.wsraspay.PaymentDto;

public interface WsRaspayIntegration {

    CustomerDto createCustomer(CustomerDto dto);

    OrderDto createOrder(OrderDto dto);

    Boolean processPayment(PaymentDto dto);
}
