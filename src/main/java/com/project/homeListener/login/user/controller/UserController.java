package com.project.homeListener.login.user.controller;

import com.project.homeListener.login.user.dto.request.RegisterRequestDTO;
import com.project.homeListener.login.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        userService.register(registerRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
