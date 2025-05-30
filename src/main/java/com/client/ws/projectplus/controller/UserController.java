package com.client.ws.projectplus.controller;

import com.client.ws.projectplus.dto.UserDto;
import com.client.ws.projectplus.model.jpa.User;
import com.client.ws.projectplus.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody UserDto dto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
