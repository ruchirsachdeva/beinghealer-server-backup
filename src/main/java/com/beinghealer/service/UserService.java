package com.beinghealer.service;

import com.beinghealer.model.User;
import com.beinghealer.dto.UserDTO;
import com.beinghealer.dto.UserParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService {

    Optional<UserDTO> findOne(Long id);

    Optional<UserDTO> findMe();

    Page<UserDTO> findAll(PageRequest pageable);

    User create(UserParams params);

    User update(User user, UserParams params);

    User updateMe(UserParams params);

    void update(long userId, UserEditForm userEditForm);

    User signup(UserParams signupForm);


    Optional<User> findUserByUsername(String username) throws UsernameNotFoundException;
}
