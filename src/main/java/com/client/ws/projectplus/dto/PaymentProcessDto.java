package com.client.ws.projectplus.dto;

import com.client.ws.projectplus.model.UserPaymentInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProcessDto {

    @NotBlank(message = "deve ser informado")
    private String productKey;

    private BigDecimal discount;

    @NotNull(message = "dados do pagamento devem ser informados")
    @JsonProperty("userPaymentInfo")
    private UserPaymentInfoDto userPaymentInfoDto;
}
