package com.beinghealer.service;

import com.beinghealer.util.Utils;
import com.beinghealer.model.Relationship;
import com.beinghealer.model.User;
import com.beinghealer.dto.PageParams;
import com.beinghealer.dto.RelatedUserDTO;
import com.beinghealer.repository.RelatedUserCustomRepository;
import com.beinghealer.repository.RelationshipRepository;
import com.beinghealer.repository.UserRepository;
import com.beinghealer.exceptions.RelationshipNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RelationshipServiceImpl implements RelationshipService {

    private final RelationshipRepository relationshipRepository;
    private final RelatedUserCustomRepository relatedUserCustomRepository;
    private final UserRepository userRepository;
    private final SecurityContextService securityContextService;

    public RelationshipServiceImpl(RelationshipRepository relationshipRepository,
                                   RelatedUserCustomRepository relatedUserCustomRepository,
                                   UserRepository userRepository,
                                   SecurityContextService securityContextService) {
        this.relationshipRepository = relationshipRepository;
        this.relatedUserCustomRepository = relatedUserCustomRepository;
        this.userRepository = userRepository;
        this.securityContextService = securityContextService;
    }

    @Override
    public List<RelatedUserDTO> findFollowings(Long userId, PageParams pageParams) {
        final User user = userRepository.findOne(userId);
        final List<RelatedUserCustomRepository.Row> rows = relatedUserCustomRepository.findFollowings(user, pageParams);

        return rowsToRelatedUsers(rows);
    }

    @Override
    public List<RelatedUserDTO> findFollowers(Long userId, PageParams pageParams) {
        final User user = userRepository.findOne(userId);
        final List<RelatedUserCustomRepository.Row> rows = relatedUserCustomRepository.findFollowers(user, pageParams);

        return rowsToRelatedUsers(rows);
    }

    @Override
    public void follow(Long userId) {
        final User user = userRepository.findOne(userId);
        securityContextService.currentUser().ifPresent(currentUser -> {
            final Relationship relationship = new Relationship(currentUser, user);
            relationshipRepository.save(relationship);
        });
    }

    @Override
    public void unfollow(Long userId) throws RelationshipNotFoundException {
        final User followed = userRepository.findOne(userId);
        final Optional<Relationship> relationship = securityContextService.currentUser()
                .flatMap(currentUser -> relationshipRepository.findOneByFollowerAndFollowed(currentUser, followed));

        relationship.ifPresent(relationshipRepository::delete);
        relationship.orElseThrow(RelationshipNotFoundException::new);
    }

    private List<RelatedUserDTO> rowsToRelatedUsers(List<RelatedUserCustomRepository.Row> rows) {
        final Optional<User> currentUser = securityContextService.currentUser();

        final List<User> relatedUsers = rows.stream()
                .map(RelatedUserCustomRepository.Row::getUser)
                .collect(Collectors.toList());

        final List<User> followedByMe = currentUser.map(u -> relationshipRepository
                .findAllByFollowerAndFollowedIn(u, relatedUsers)
                .map(Relationship::getFollowed)
                .collect(Collectors.toList())
        ).orElse(Collections.emptyList());

        return rows.stream().map(r -> {
            final Boolean isFollowedByMe = currentUser
                    .map(u -> followedByMe.contains(r.getUser()))
                    .orElse(null);
            return RelatedUserDTO.builder()
                    .id(r.getUser().getId())
                    .avatarHash(Utils.md5(r.getUser().getUsername()))
                    .name(r.getUser().getName())
                    .userStats(r.getUserStats())
                    .relationshipId(r.getRelationship().getId())
                    .isFollowedByMe(isFollowedByMe)
                    .build();
        }).collect(Collectors.toList());
    }

}
