package com.client.ws.projectplus.controller;

import com.client.ws.projectplus.dto.LoginDto;
import com.client.ws.projectplus.dto.TokenDto;
import com.client.ws.projectplus.dto.UserDetailsDto;
import com.client.ws.projectplus.model.redis.UserRecoveryCode;
import com.client.ws.projectplus.service.AuthenticationService;
import com.client.ws.projectplus.service.UserDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;

    public AuthenticationController(AuthenticationService authenticationService, UserDetailsService userDetailsService) {
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<TokenDto> auth(@RequestBody @Valid LoginDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.auth(dto));
    }

    @PostMapping("/recovery-code/send")
    public ResponseEntity<Void> sendRecoveryCode(@RequestBody @Valid UserRecoveryCode dto) {
        userDetailsService.sendRecoveryCode(dto.getEmail());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/recovery-code/")
    public ResponseEntity<Boolean> recoveryCodeIsValid(@RequestParam("recoveryCode") String recoveryCode,
                                                       @RequestParam("email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.recoveryCodeIsValid(recoveryCode, email));
    }

    @PatchMapping("/recovery-code/password")
    public ResponseEntity<Void> sendRecoveryCode(@RequestBody @Valid UserDetailsDto dto) {
        userDetailsService.updatePasswordByRecoveryCode(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
