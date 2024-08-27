package com.project.homeListener.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    @PostMapping("/tokenCheck")
    public ResponseEntity<Void> tokenCheck() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
