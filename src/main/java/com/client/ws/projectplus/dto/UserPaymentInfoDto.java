package com.client.ws.projectplus.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentInfoDto {

    private Long Id;

    @Size(min=16, max=16, message = "deve ter 16 digitos")
    private String cardNumber;

    @Min(value = 1)
    @Max(value = 12)
    private Long cardExpirationMonth;

    private Long cardExpirationYear;

    @Size(min=3, max=3, message = "deve ter 3 digitos")
    private String cardSecurityCode;

    private BigDecimal price;

    private Long installments;

    private LocalDate dtPayment = LocalDate.now();

    @NotNull(message = "deve ser informado")
    private Long userId;
}
