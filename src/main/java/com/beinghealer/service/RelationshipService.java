package com.beinghealer.service;

import com.beinghealer.dto.PageParams;
import com.beinghealer.dto.RelatedUserDTO;
import com.beinghealer.exceptions.RelationshipNotFoundException;

import java.util.List;

public interface RelationshipService {

    List<RelatedUserDTO> findFollowings(Long userId, PageParams pageParams);

    List<RelatedUserDTO> findFollowers(Long userId, PageParams pageParams);

    void follow(Long userId);

    void unfollow(Long userId) throws RelationshipNotFoundException;

}
