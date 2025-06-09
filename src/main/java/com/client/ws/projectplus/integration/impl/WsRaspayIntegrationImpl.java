package com.client.ws.projectplus.integration.impl;

import com.client.ws.projectplus.dto.wsraspay.CustomerDto;
import com.client.ws.projectplus.dto.wsraspay.OrderDto;
import com.client.ws.projectplus.dto.wsraspay.PaymentDto;
import com.client.ws.projectplus.exception.HttpClientException;
import com.client.ws.projectplus.integration.WsRaspayIntegration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
public class WsRaspayIntegrationImpl implements WsRaspayIntegration {

    @Value("${webservice.raspay.host}")
    private String raspayHost;
    @Value("${webservice.raspay.v1.customer}")
    private String customerUrl;
    @Value("${webservice.raspay.v1.order}")
    private String orderUrl;
    @Value("${webservice.raspay.v1.payment}")
    private String paymentUrl;


    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public WsRaspayIntegrationImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        headers = getHttpHeaders();
    }

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        try {
            HttpEntity<CustomerDto> request = new HttpEntity<>(dto, this.headers);
            ResponseEntity<CustomerDto> response = restTemplate.exchange(raspayHost+customerUrl, HttpMethod.POST, request, CustomerDto.class);

            return response.getBody();
        } catch (Exception e) {
            throw new HttpClientException(e.getLocalizedMessage());
        }
    }

    @Override
    public OrderDto createOrder(OrderDto dto) {
        try {
            HttpEntity<OrderDto> request = new HttpEntity<>(dto, this.headers);
            ResponseEntity<OrderDto> response = restTemplate.exchange(raspayHost+orderUrl, HttpMethod.POST, request, OrderDto.class);

            return response.getBody();
        } catch (Exception e) {
            throw new HttpClientException(e.getLocalizedMessage());
        }
    }

    @Override
    public Boolean processPayment(PaymentDto dto) {
        try {
            HttpEntity<PaymentDto> request = new HttpEntity<>(dto, this.headers);
            ResponseEntity<Boolean> response = restTemplate.exchange(raspayHost+paymentUrl, HttpMethod.POST, request, Boolean.class);

            return response.getBody();
        } catch (Exception e) {
            throw new HttpClientException(e.getLocalizedMessage());
        }
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String credentials = "rasmooplus:r@sm00";
        String base64 = Base64.getEncoder().encodeToString(credentials.getBytes());
        headers.add("Authorization", "Basic " + base64);
        return headers;
    }
}
