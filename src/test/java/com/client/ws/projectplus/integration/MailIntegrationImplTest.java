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
class MailIntegrationImplTest {

    @Autowired
    private MailIntegration mailIntegration;

//    @Test
//    void sendEmailOK() {
//        mailIntegration.send("siluanaoggionidev@gmail.com", "Acesso Liberado!","Teste de envio de email");
//    }
}
