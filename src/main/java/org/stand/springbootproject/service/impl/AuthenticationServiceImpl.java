package org.stand.springbootproject.service.impl;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.stand.springbootproject.dto.request.AuthenticationRequest;
import org.stand.springbootproject.dto.request.RegisterRequest;
import org.stand.springbootproject.dto.response.AuthenticationResponse;
import org.stand.springbootproject.dto.response.BaseResponseBody;
import org.stand.springbootproject.entity.role.Role;
import org.stand.springbootproject.entity.role.RoleName;
import org.stand.springbootproject.entity.user.User;
import org.stand.springbootproject.entity.user.VerificationToken;
import org.stand.springbootproject.error.*;
import org.stand.springbootproject.event.RegistrationCompleteEvent;
import org.stand.springbootproject.repository.RoleRepository;
import org.stand.springbootproject.repository.UserRepository;
import org.stand.springbootproject.repository.VerificationTokenRepository;
import org.stand.springbootproject.service.AuthenticationService;
import org.stand.springbootproject.service.JwtService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final Validator validator;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MessageSource messageSource;


    public BaseResponseBody register(RegisterRequest request) throws BaseException {

        // Check repository persistence constraints
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserEmailAlreadyTakenException();
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserUsernameAlreadyTakenException();
        }

        // Save user to repository
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(RoleNotFoundException::new));
        User user = User.builder()
                .firstName(request.getFirstname().trim().toLowerCase())
                .lastName(request.getLastname().trim().toLowerCase())
                .username(request.getUsername().trim().toLowerCase())
                .email(request.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();
        userRepository.save(user);

        // Publish a RegistrationCompleteEvent (email verification mechanism with token)
        applicationEventPublisher.publishEvent(
                new RegistrationCompleteEvent(
                        user,
                        "http://localhost:8080/api/v1/auth/" // TODO: not hardcoded
                )
        );

        // Create and return success response
        return new BaseResponseBody(
                messageSource.getMessage("user.register.success", null, Locale.getDefault())
        );
    }

    public AuthenticationResponse authenticate(
            AuthenticationRequest request
    ) throws BadCredentialsException, BaseException {

        // Authenticate the user with the provided credentials
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(null);
        } catch (DisabledException e) {
            throw new UserNotEnabledException();
        } // TODO: catch LockedException
        // (user authentication successful at this point)

        // Get user from repository
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                UserNotFoundException::new
        );

        /*
        // Check if the user is enabled
        if (!user.isEnabled()) {
            throw new UserNotEnabledException();
        }
        */

        // Generate JWT token
        String jwt = jwtService.generateToken(user);

        // Create and return success response
        return new AuthenticationResponse(jwt);
    }

    public BaseResponseBody validate(String token) throws BaseException {
        // Get verification token from repository
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token).orElseThrow(VerificationTokenNotFoundException::new);

        // Check if verification token is not expired
        if (!verificationToken.isTokenNonExpired()) {
            throw new VerificationTokenExpiredException();
        }

        // Set user as enabled
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        // Delete verification token from repository
        verificationTokenRepository.delete(verificationToken);

        // Create and return success response
        return new BaseResponseBody(
                messageSource.getMessage("user.email.confirmation.success", null, Locale.getDefault())
        );
    }
}
