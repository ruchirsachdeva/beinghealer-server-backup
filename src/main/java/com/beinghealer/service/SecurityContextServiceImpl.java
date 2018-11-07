package com.beinghealer.service;

import com.beinghealer.model.User;
import com.beinghealer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by rucsac on 20/10/2018.
 */

@Service
public class SecurityContextServiceImpl  implements SecurityContextService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> currentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }
}
