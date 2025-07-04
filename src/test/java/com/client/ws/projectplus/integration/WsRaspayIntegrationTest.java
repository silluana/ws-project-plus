package com.client.ws.projectplus.integration;

import com.client.ws.projectplus.dto.wsraspay.CustomerDto;
import com.client.ws.projectplus.exception.HttpClientException;
import com.client.ws.projectplus.integration.impl.WsRaspayIntegrationImpl;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WsRaspayIntegrationTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WsRaspayIntegrationImpl wsRaspayIntegration;

    private static HttpHeaders headers;

    @BeforeAll
    static void loadHeaders() {
        headers = getHttpHeaders();
    }

    @Test
    void given_createCustomer_when_apiResponseIs201Created_then_returnCustomerDto() {
        ReflectionTestUtils.setField(wsRaspayIntegration,"raspayHost","http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration,"customerUrl","/customer");

        CustomerDto dto = new CustomerDto();
        dto.setCpf("11111111111");

        HttpEntity<CustomerDto> request = new HttpEntity<>(dto,this.headers);
        when(restTemplate.exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class))
                .thenReturn(ResponseEntity.of(Optional.of(dto)));
        assertEquals(dto, wsRaspayIntegration.createCustomer(dto));
        verify(restTemplate, times(1)).exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class);
    }

    @Test
    void given_createCustomer_when_apiResponseIs400BadRequest_then_returnNull() {
        ReflectionTestUtils.setField(wsRaspayIntegration,"raspayHost","http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration,"customerUrl","/customer");

        CustomerDto dto = new CustomerDto();
        dto.setCpf("11111111111");

        HttpEntity<CustomerDto> request = new HttpEntity<>(dto,this.headers);
        when(restTemplate.exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class))
                .thenReturn(ResponseEntity.badRequest().build());
        assertNull(wsRaspayIntegration.createCustomer(dto));
        verify(restTemplate, times(1)).exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class);
    }

    @Test
    void given_createCustomer_when_apiResponseGetThrows_then_throwHttpClientException() {
        ReflectionTestUtils.setField(wsRaspayIntegration,"raspayHost","http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration,"customerUrl","/customer");

        CustomerDto dto = new CustomerDto();
        dto.setCpf("11111111111");

        HttpEntity<CustomerDto> request = new HttpEntity<>(dto,this.headers);
        when(restTemplate.exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class))
                .thenThrow(HttpClientException.class);
        assertThrows(HttpClientException.class, () -> wsRaspayIntegration.createCustomer(dto));
        verify(restTemplate, times(1)).exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class);
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String credential = "rasmooplus:r@sm00";
        String base64 = new String (Base64.encodeBase64(credential.getBytes(),false));
        headers.add("Authorization","Basic "+base64);
        return headers;
    }
}