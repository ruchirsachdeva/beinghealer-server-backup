package com.beinghealer.service;

import com.beinghealer.model.Micropost;
import com.beinghealer.dto.PageParams;
import com.beinghealer.dto.PostDTO;
import com.beinghealer.exceptions.NotPermittedException;

import java.util.List;

public interface MicropostService {

    void delete(Long id) throws NotPermittedException;

    List<PostDTO> findAsFeed(PageParams pageParams);

    List<PostDTO> findByUser(Long userId, PageParams pageParams);

    List<PostDTO> findMyPosts(PageParams pageParams);

    Micropost saveMyPost(Micropost post);

}
