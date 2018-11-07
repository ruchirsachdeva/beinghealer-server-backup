package com.beinghealer.controller;

import com.beinghealer.dto.EmailParams;
import com.beinghealer.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

/**
 * Created by SBI on 4/2/2018.
 */

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    EmailService service;

    @RequestMapping(method = RequestMethod.POST)
    @CrossOrigin(origins = {"http://localhost:4200", "http://www.beinghealer.com", "*"})
    public ResponseEntity create(@Valid @RequestBody EmailParams params, WebRequest request) {

        service.send(params.toEmail());
        //   Optional<String> token = MyUtil.logInUser(user);
        return null;
    }
}
