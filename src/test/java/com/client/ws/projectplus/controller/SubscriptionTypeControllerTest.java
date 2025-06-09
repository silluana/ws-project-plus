package com.client.ws.projectplus.controller;

import com.client.ws.projectplus.dto.SubscriptionTypeDto;
import com.client.ws.projectplus.model.jpa.SubscriptionType;
import com.client.ws.projectplus.service.SubscriptionTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(SubscriptionTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class SubscriptionTypeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubscriptionTypeService subscriptionTypeService;

    @Test
    void given_findAll_then_returnAllSubscriptionType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/subscription-type"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void given_findById_whenGetId2_then_returnOneSubscriptionType() throws Exception {
        SubscriptionType subscriptionType = new SubscriptionType(2L, "VITALICIO", null, BigDecimal.valueOf(997.00),
                "FOREVER2025");
        Mockito.when(subscriptionTypeService.findById(2L)).thenReturn(subscriptionType);

        mockMvc.perform(MockMvcRequestBuilders.get("/subscription-type/2"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(2)))
                .andExpect(jsonPath("$.name", Matchers.is("VITALICIO")))
                .andExpect(jsonPath("$.price", Matchers.is(997.00)))
        ;
    }

    @Test
    void given_delete_whenGetId2_then_noReturnAndNoContent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/subscription-type/{id}", 2))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(subscriptionTypeService, Mockito.times(1)).delete(2L);
    }

    @Test
    void given_create_whenDtoIsOk_then_returnSubscriptionTypeCreated() throws Exception {
        SubscriptionTypeDto dto = new SubscriptionTypeDto(null, "VITALICIO", null, BigDecimal.valueOf(997.00),
                "FOREVER2025");

        SubscriptionType subscriptionType = new SubscriptionType(2L, "VITALICIO", null, BigDecimal.valueOf(997.00),
                "FOREVER2025");
        Mockito.when(subscriptionTypeService.create(dto)).thenReturn(subscriptionType);

        mockMvc.perform(MockMvcRequestBuilders.post("/subscription-type")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(2)))
        ;
    }

//    @Test
//    void given_create_whenDtoIsOk_then_returnSubscriptionTypeCreated() throws Exception {
//        SubscriptionTypeDto dto = new SubscriptionTypeDto(null, "VITALICIO", null, BigDecimal.valueOf(997.00),
//                "FOREVER2025");
//
//        SubscriptionType subscriptionType = new SubscriptionType(2L, "VITALICIO", null, BigDecimal.valueOf(997.00),
//                "FOREVER2025");
//        Mockito.when(subscriptionTypeService.create(dto)).thenReturn(subscriptionType);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/subscription-type")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(jsonPath("$.id", Matchers.is(2)))
//        ;
//    }
}