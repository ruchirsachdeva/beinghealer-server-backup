package com.beinghealer.repository;

import com.beinghealer.model.Relationship;
import com.beinghealer.model.User;
import com.beinghealer.model.UserStats;
import com.beinghealer.dto.PageParams;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RelatedUserCustomRepository extends Repository<User, Long> {

    List<Row> findFollowings(User user, PageParams pageParams);

    List<Row> findFollowers(User user, PageParams pageParams);

    @Value
    @Builder
    class Row {
        private final User user;
        private final Relationship relationship;
        private final UserStats userStats;
    }
}
