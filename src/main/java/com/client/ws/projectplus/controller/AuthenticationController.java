package com.client.ws.projectplus.controller;

import com.client.ws.projectplus.dto.LoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @PostMapping
    public ResponseEntity<LoginDto> auth(@RequestBody LoginDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
