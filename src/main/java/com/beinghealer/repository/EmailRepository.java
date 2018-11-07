package com.beinghealer.repository;

import com.beinghealer.model.Email;
import com.beinghealer.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {

}
