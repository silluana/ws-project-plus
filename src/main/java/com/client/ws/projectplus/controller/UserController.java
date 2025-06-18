package com.client.ws.projectplus.controller;

import com.client.ws.projectplus.dto.UserDto;
import com.client.ws.projectplus.model.jpa.User;
import com.client.ws.projectplus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "Usuários", description = "Operações relacionadas aos usuários do sistema")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary ="Busca todos os usuários cadastrados")
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @Operation(summary ="Busca um usuário pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @Operation(summary ="Cadastra um novo usuário")
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    @Operation(summary ="Atualiza um usuário existente")
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody UserDto dto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, dto));
    }

    @Operation(summary ="Deleta um usuário pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Operation(summary ="Faz o upload de uma foto para um usuário")
    @PatchMapping(value = "/{id}/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> uploadPhoto(@PathVariable("id") Long id, @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.uploadPhoto(id, file));
    }

    @Operation(summary ="Faz o download da foto de um usuário")
    @GetMapping(value = "/{id}/photo", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> downloadPhoto(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.downloadPhoto(id));
    }
}
