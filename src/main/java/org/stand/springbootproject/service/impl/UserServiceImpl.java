package org.stand.springbootproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.stand.springbootproject.dto.response.BaseResponseBody;
import org.stand.springbootproject.entity.role.Role;
import org.stand.springbootproject.entity.role.RoleName;
import org.stand.springbootproject.entity.user.User;
import org.stand.springbootproject.entity.user.VerificationToken;
import org.stand.springbootproject.error.BaseException;
import org.stand.springbootproject.error.RoleNotFoundException;
import org.stand.springbootproject.error.UserNotFoundException;
import org.stand.springbootproject.repository.RoleRepository;
import org.stand.springbootproject.repository.UserRepository;
import org.stand.springbootproject.repository.VerificationTokenRepository;
import org.stand.springbootproject.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;


    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken =
                new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public List<User> getAllUsers(Integer page, Integer size) {
        return userRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public BaseResponseBody makeUserAdmin(String username) throws BaseException {
        // Get user from repository
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        // Update and save user to repository
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(RoleNotFoundException::new));
        roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(RoleNotFoundException::new));

        user.setRoles(roles);
        userRepository.save(user);

        // Create and return success response
        return new BaseResponseBody(
                messageSource.getMessage(
                        "admin.make.user.admin.success",
                        new Object[]{username},
                        Locale.getDefault()
                )
        );
    }

    @Override
    public BaseResponseBody deleteUser(String username) throws BaseException {
        // Get user from repository
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        // Delete user from repository
        userRepository.deleteByUsername(username);

        // Create and return success response
        return new BaseResponseBody(
                messageSource.getMessage(
                        "admin.delete.user.success",
                        new Object[]{username},
                        Locale.getDefault()
                )
        );
    }
}
