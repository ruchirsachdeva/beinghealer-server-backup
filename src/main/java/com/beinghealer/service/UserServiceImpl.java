package com.beinghealer.service;

import com.beinghealer.util.Utils;
import com.beinghealer.model.Role;
import com.beinghealer.model.User;
import com.beinghealer.dto.UserDTO;
import com.beinghealer.dto.UserParams;
import com.beinghealer.repository.RelationshipRepository;
import com.beinghealer.repository.RoleRepository;
import com.beinghealer.repository.UserCustomRepository;
import com.beinghealer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("userService")
@Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final UserCustomRepository userCustomRepository;
    private final RelationshipRepository relationshipRepository;
    private final SecurityContextService securityContextService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserCustomRepository userCustomRepository,
                           RelationshipRepository relationshipRepository,
                           SecurityContextService securityContextService, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userCustomRepository = userCustomRepository;
        this.relationshipRepository = relationshipRepository;
        this.securityContextService = securityContextService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<UserDTO> findOne(Long id) {
        return userCustomRepository.findOne(id).map(r -> {
            final Optional<User> currentUser = securityContextService.currentUser();
            final Boolean isFollowedByMe = currentUser
                    .map(u -> relationshipRepository
                            .findOneByFollowerAndFollowed(u, r.getUser())
                            .isPresent()
                    )
                    .orElse(null);
            // Set email only if it equals with currentUser.
            final String email = currentUser
                    .filter(u -> u.equals(r.getUser()))
                    .map(User::getUsername)
                    .orElse(null);
            final String role = currentUser.map(u->{
                Role ur = u.getRole();
                return ur.getRole();
            })
                    .orElse("ROLE_BUYER");

            return UserDTO.builder()
                    .id(r.getUser().getId())
                    .email(email)
                    .avatarHash(Utils.md5(r.getUser().getUsername()))
                    .name(r.getUser().getName())
                    .userStats(r.getUserStats())
                    .isFollowedByMe(isFollowedByMe)
                    .role(role)
                    .build();
        });
    }

    @Override
    public Optional<UserDTO> findMe() {
        return securityContextService.currentUser().flatMap(u -> findOne(u.getId()));
    }

    @Override
    public Page<UserDTO> findAll(PageRequest pageable) {
        return userRepository.findAll(pageable).map(u -> {
          return  UserDTO.builder()
                    .id(u.getId())
                    .name(u.getName())
                    .avatarHash(Utils.md5(u.getUsername()))
                    .email(u.getUsername())
                    .role(u.getRole().getRole())
                    .build();
                }
        );
    }

    @Override
    public User create(UserParams params) {
        User user = params.toUser();
        attachRoleToUser(params.getRole(), user);
        return userRepository.save(user);
    }

    private void attachRoleToUser(Optional<String> roleParam, User user) {
        roleParam.ifPresent(
                role ->
                        roleRepository.findOneByRole(role).ifPresent(user::setRole)
        );
    }

    @Override
    public User update(User user, UserParams params) {
        params.getEmail().ifPresent(user::setUsername);
        params.getEncodedPassword().ifPresent(user::setPassword);
        params.getName().ifPresent(user::setName);
        attachRoleToUser(params.getRole(), user);
        return userRepository.save(user);
    }

    @Override
    public User updateMe(UserParams params) {
        return securityContextService.currentUser()
                .map(u -> update(u, params))
                .orElseThrow(() -> new AccessDeniedException(""));
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public User signup(UserParams signupForm) {
        final User user = new User();
        user.setUsername(signupForm.getEmail().get());
        user.setName(signupForm.getName().get());
        user.setPassword(signupForm.getEncodedPassword().get());
        attachRoleToUser(signupForm.getRole(), user);
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public void update(long userId, UserEditForm userEditForm) {

        User user = userRepository.findOne(userId);
        user.setName(userEditForm.getName());
        userRepository.save(user);

    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> user = userRepository.findByUsername(username);
        final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
        user.ifPresent(detailsChecker::check);
        return user.orElseThrow(() -> new UsernameNotFoundException("user not found."));
    }

    @Override
    public Optional<User> findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

}
