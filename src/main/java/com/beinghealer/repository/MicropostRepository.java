package com.beinghealer.repository;

import com.beinghealer.model.Micropost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MicropostRepository extends JpaRepository<Micropost, Long> {
}
