package com.beinghealer.dto;

import com.beinghealer.model.Micropost;
import lombok.Value;

@Value
public class MicropostParams {

    private String content;

    public Micropost toPost() {
       return new Micropost(content);
    }
}
