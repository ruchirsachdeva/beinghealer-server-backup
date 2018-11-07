package com.beinghealer.service;

import com.beinghealer.model.User;
import com.beinghealer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by rucsac on 20/10/2018.
 */

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Authentication authenticate(Authentication loginToken) {
        Authentication authenticate = authenticationManager.authenticate(loginToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return authenticate;
    }
}
