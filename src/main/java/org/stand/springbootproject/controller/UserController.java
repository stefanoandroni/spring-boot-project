package org.stand.springbootproject.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stand.springbootproject.dto.UserDTO;
import org.stand.springbootproject.dto.UserListDTO;
import org.stand.springbootproject.dto.response.BaseResponseBody;
import org.stand.springbootproject.entity.user.User;
import org.stand.springbootproject.error.BaseException;
import org.stand.springbootproject.error.UserNotFoundException;
import org.stand.springbootproject.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
    private static final String DEFAULT_PAGE_SIZE = "5";
    private static final String DEFAULT_PAGE_NUMBER = "0";
    private final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<UserListDTO>> getAllUsers(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size
    ) {
        List<User> userList = userService.getAllUsers(page, size);
        List<UserListDTO> userListDTOList = userList.stream()
                .map(user -> modelMapper.map(user, UserListDTO.class))
                .toList();
        // LOG.info("userListDTOList:" + userListDTOList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userListDTOList);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(
            @PathVariable(name = "username") String username
    ) throws UserNotFoundException {
        User user = userService.getUserByUsername(username);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        // LOG.info("userDTO:" + userDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTO);
    }

    @PutMapping("/{username}/make-admin")
    public ResponseEntity<BaseResponseBody> makeUserAdmin(
            @PathVariable(name = "username") String username
    ) throws BaseException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.makeUserAdmin(username));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<BaseResponseBody> deleteUser(
            @PathVariable(name = "username") String username
    ) throws BaseException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.deleteUser(username));
    }

}
