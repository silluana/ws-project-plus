package com.client.ws.projectplus.dto.wsraspay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String id;

    private String customerId;

    private BigDecimal discount;

    private String productAcronym;
}
