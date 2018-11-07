package com.beinghealer.controller;


import com.beinghealer.auth.TokenHandler;
import com.beinghealer.dto.UserParams;
import com.beinghealer.model.SignupForm;
import com.beinghealer.model.User;
import com.beinghealer.service.AuthenticationService;
import com.beinghealer.service.SecurityContextService;
import com.beinghealer.service.UserService;
import lombok.Value;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@RestController
public class LoginController {

    Logger log = Logger.getLogger(LoginController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private TokenHandler tokenHandler;
    @Autowired
    private SecurityContextService securityContextService;
    @Autowired
    private AuthenticationService authenticationService;

    @CrossOrigin(origins = {"http://localhost:4200", "https://lit-beach-29911.herokuapp.com"})
    @RequestMapping(value = {"/api/auth/social"}, method = RequestMethod.POST)
    public AuthResponse loginToSocial(@RequestBody(required = false) UserParams params) throws AuthenticationException {
        User user = userService.findUserByUsername(params.getEmail().orElseThrow(()->new RuntimeException("Email is null")))
         .orElseGet(registerUser(params));
        final String token = tokenHandler.createTokenForUser(user);
        return new AuthResponse(token);
    }

    private Supplier<? extends User> registerUser(UserParams params) {

        return () -> userService.signup(params);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://lit-beach-29911.herokuapp.com"})
    @RequestMapping(value = {"/api/auth"}, method = RequestMethod.POST)
    public AuthResponse login(@RequestBody(required = false) AuthParams params) throws AuthenticationException {

        if (params != null) {
            final UsernamePasswordAuthenticationToken loginToken = params.toAuthenticationToken();
            authenticationService.authenticate(loginToken);
        }
        return securityContextService.currentUser().map(u -> {
            String token = tokenHandler.createTokenForUser(u);
            return new AuthResponse(token);
        }).orElseThrow(RuntimeException::new); // it does not happen.
    }


    @RequestMapping(value = {"/", "/login"})
    public AuthResponse auth() {
        return securityContextService.currentUser().map(u -> {
            final String token = tokenHandler.createTokenForUser(u);
            return new AuthResponse(token);
        }).orElseThrow(RuntimeException::new); // it does not happen.
    }


    @CrossOrigin(origins = {"http://localhost:4200", "https://lit-beach-29911.herokuapp.com"})
    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.OPTIONS)
    public ResponseEntity authOption() {
        return ResponseEntity.ok().build();
    }


    @Value
    private static final class AuthParams {
        private final String username;
        private final String password;
        private final String provider;
        private final String token;


        UsernamePasswordAuthenticationToken toAuthenticationToken() {
            return new UsernamePasswordAuthenticationToken(username, password);
        }

        public SignupForm toSignupForm() {
            return new SignupForm(username, username, username, password, provider, token);
        }
    }

    @Value
    private static final class AuthResponse {
        private final String token;
    }

}
