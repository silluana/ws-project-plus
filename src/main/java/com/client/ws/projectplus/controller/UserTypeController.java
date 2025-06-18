package com.client.ws.projectplus.controller;

import com.client.ws.projectplus.model.jpa.UserType;
import com.client.ws.projectplus.service.UserTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-type")
@Tag(name = "Tipos de Usuário", description = "Operações relacionadas aos tipos de usuário do sistema")
public class UserTypeController {

    private final UserTypeService userTypeService;

    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @Operation(summary = "Busca todos os tipos de usuário disponíveis")
    @GetMapping
    public ResponseEntity<List<UserType>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userTypeService.findAll());
    }
}
