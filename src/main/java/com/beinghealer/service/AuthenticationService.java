package com.beinghealer.service;


import com.beinghealer.model.User;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthenticationService {
Authentication authenticate(Authentication loginToken);
}
