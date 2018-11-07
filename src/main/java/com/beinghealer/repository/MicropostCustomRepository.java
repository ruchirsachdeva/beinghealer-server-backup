package com.beinghealer.repository;

import com.beinghealer.model.Micropost;
import com.beinghealer.model.User;
import com.beinghealer.model.UserStats;
import com.beinghealer.dto.PageParams;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface MicropostCustomRepository extends Repository<Micropost, Long> {

    List<Row> findAsFeed(User user, PageParams pageParams);

    List<Row> findByUser(User user, PageParams pageParams);

    @Value
    @Builder
    class Row {
        private final Micropost micropost;
        private final UserStats userStats;
    }

}
