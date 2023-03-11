package org.stand.springbootproject.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.stand.springbootproject.dto.request.AuthenticationRequest;
import org.stand.springbootproject.dto.request.RegisterRequest;
import org.stand.springbootproject.dto.response.AuthenticationResponse;
import org.stand.springbootproject.dto.response.BaseResponseBody;
import org.stand.springbootproject.error.BaseException;
import org.stand.springbootproject.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponseBody> register(
            @Valid @RequestBody RegisterRequest request
    ) throws BaseException {
        // LOG.info(String.format("IN register(request) [request=%s]", request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authenticationService.register(request));
    }

    @GetMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) throws BadCredentialsException, BaseException {
        // LOG.info(String.format("IN authenticate(request) [request=%s]", request));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.authenticate(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<BaseResponseBody> validate(
            @RequestParam("token") @NotBlank String token
    ) throws BaseException {
        // LOG.info(String.format("IN validate(token) [token=%s]", token));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.validate(token));
    }

}
