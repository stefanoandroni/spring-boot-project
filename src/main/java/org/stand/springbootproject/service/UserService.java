package org.stand.springbootproject.service;

import org.stand.springbootproject.dto.response.BaseResponseBody;
import org.stand.springbootproject.entity.user.User;
import org.stand.springbootproject.error.BaseException;
import org.stand.springbootproject.error.UserNotFoundException;

import java.util.List;

public interface UserService {
    BaseResponseBody makeUserAdmin(String username) throws BaseException;

    BaseResponseBody deleteUser(String username) throws BaseException;

    void saveVerificationTokenForUser(String token, User user);

    List<User> getAllUsers(Integer page, Integer size);

    User getUserByUsername(String username) throws UserNotFoundException;
}
