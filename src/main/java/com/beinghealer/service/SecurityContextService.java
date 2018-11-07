package com.beinghealer.service;


import com.beinghealer.model.User;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface SecurityContextService {
    Optional<User> currentUser();

}
