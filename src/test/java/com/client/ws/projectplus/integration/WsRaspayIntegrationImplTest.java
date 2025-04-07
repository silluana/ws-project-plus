package com.client.ws.projectplus.integration;

import com.client.ws.projectplus.dto.wsraspay.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WsRaspayIntegrationImplTest {

    @Autowired
    private WsRaspayIntegration wsRaspayIntegration;

    @Test
    void createCustomerWhenDtoOK() {
        CustomerDto dto = new CustomerDto(null, "600.011.170-33", "mariavitoria@test.email.com","Maria","Vitória");
        wsRaspayIntegration.createCustomer(dto);
    }
}
