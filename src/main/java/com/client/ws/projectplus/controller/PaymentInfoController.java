package com.client.ws.projectplus.controller;

import com.client.ws.projectplus.dto.PaymentProcessDto;
import com.client.ws.projectplus.service.PaymentInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@Tag(name = "Pagamento", description = "Operações relacionadas ao processamento de pagamentos")
public class PaymentInfoController {

    private final PaymentInfoService paymentInfoService;

    public PaymentInfoController(PaymentInfoService paymentInfoService) {
        this.paymentInfoService = paymentInfoService;
    }

    @Operation(summary = "Processa o pagamento da assinatura do usuário na plataforma de estudos Rasmoo")
    @PostMapping("/process")
    public ResponseEntity<Boolean> process(@RequestBody PaymentProcessDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentInfoService.process(dto));
    }
}
