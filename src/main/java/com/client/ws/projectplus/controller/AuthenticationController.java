package com.client.ws.projectplus.controller;

import com.client.ws.projectplus.dto.LoginDto;
import com.client.ws.projectplus.dto.TokenDto;
import com.client.ws.projectplus.dto.UserDetailsDto;
import com.client.ws.projectplus.model.redis.UserRecoveryCode;
import com.client.ws.projectplus.service.AuthenticationService;
import com.client.ws.projectplus.service.UserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Operações responsáveis pelo acesso e validação do usuário")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;

    public AuthenticationController(AuthenticationService authenticationService, UserDetailsService userDetailsService) {
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
    }

    @Operation(summary = "Realiza a autenticação do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário e senha validados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Usuário e senha inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDto> auth(@RequestBody @Valid LoginDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.auth(dto));
    }

    @Operation(summary = "Envia o código de recuperação para o email do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código enviado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Algum dado não foi encontrado")
    })
    @PostMapping(value = "/recovery-code/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendRecoveryCode(@RequestBody @Valid UserRecoveryCode dto) {
        userDetailsService.sendRecoveryCode(dto.getEmail());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Operation(summary = "Valida o código de recuperação enviado para o email do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código de recuperação validado e enviado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Algum dado não foi encontrado")
    })
    @GetMapping("/recovery-code/")
    public ResponseEntity<Boolean> recoveryCodeIsValid(@RequestParam("recoveryCode") String recoveryCode,
                                                       @RequestParam("email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.recoveryCodeIsValid(recoveryCode, email));
    }

    @Operation(summary = "Atualiza a senha do usuário utilizando o código de recuperação validado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha do usuário atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Algum dado não foi encontrado")
    })
    @PatchMapping(value = "/recovery-code/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePasswordByRecoveryCode(@RequestBody @Valid UserDetailsDto dto) {
        userDetailsService.updatePasswordByRecoveryCode(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
