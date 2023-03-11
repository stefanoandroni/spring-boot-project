package org.stand.springbootproject.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.stand.springbootproject.dto.request.AuthenticationRequest;
import org.stand.springbootproject.dto.request.RegisterRequest;
import org.stand.springbootproject.dto.response.AuthenticationResponse;
import org.stand.springbootproject.dto.response.BaseResponseBody;
import org.stand.springbootproject.error.BaseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request) throws BadCredentialsException, BaseException;

    BaseResponseBody register(RegisterRequest request) throws BaseException;

    BaseResponseBody validate(String token) throws BaseException;
}
