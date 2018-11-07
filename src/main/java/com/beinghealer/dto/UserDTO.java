package com.beinghealer.dto;

import com.beinghealer.model.UserStats;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class UserDTO {

    private final long id;
    private final String email;
    @NonNull
    private final String name;
    @NonNull
    private final String avatarHash;
    private final UserStats userStats;
    private final Boolean isFollowedByMe;
    @NonNull
    private final String role;

}
