package com.client.ws.projectplus.integration;

import com.client.ws.projectplus.dto.wsraspay.CreditCardDto;
import com.client.ws.projectplus.dto.wsraspay.CustomerDto;
import com.client.ws.projectplus.dto.wsraspay.OrderDto;
import com.client.ws.projectplus.dto.wsraspay.PaymentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class WsRaspayIntegrationImplTest {

    @Autowired
    private WsRaspayIntegration wsRaspayIntegration;

    @Test
    void createCustomerWhenDtoOK() {
        CustomerDto dto = new CustomerDto(null, "09558875007", "josefina@test.email.com","Josefina","Maria");
        wsRaspayIntegration.createCustomer(dto);
    }

    @Test
    void createOrderWhenDtoOK() {
        OrderDto dto = new OrderDto(null, "67f595d272877352d8f37aef", BigDecimal.ZERO, "MONTH22");
        wsRaspayIntegration.createOrder(dto);
    }

    @Test
    void processPaymentWhenDtoOK() {
        CreditCardDto creditCardDto = new CreditCardDto(123L, "09558875007", 0L, 6L, "1111222233334444", 2026L);
        PaymentDto paymentDto = new PaymentDto(creditCardDto, "67f595d272877352d8f37aef","67f5962f72877352d8f37af0");

        wsRaspayIntegration.processPayment(paymentDto);
    }
}
