package org.stand.springbootproject.config;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.stand.springbootproject.dto.UserDTO;
import org.stand.springbootproject.dto.UserListDTO;
import org.stand.springbootproject.entity.role.Role;
import org.stand.springbootproject.entity.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        /*
            Converter
        */
        Converter<List<Role>, List<String>> roleListToStringList = context -> {
            List<Role> roles = context.getSource();
            return roles.stream()
                    .map(role -> role.getName().name())
                    .collect(Collectors.toList());
        };

        Converter<LocalDateTime, String> localDateTimeToString = context -> {
            LocalDateTime localDateTime = context.getSource();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return localDateTime.format(formatter);
        };

        Converter<String, String> userUrlConverter = new AbstractConverter<String, String>() {
            @Override
            protected String convert(String username) {
                return "http://localhost:8080/api/v1/admin/users/" + username; // TODO: bad implementation (and hardcoded)
            }
        };

        /*
            TypeMap
        */
        TypeMap<User, UserDTO> userToUserDTO = modelMapper.createTypeMap(User.class, UserDTO.class);
        userToUserDTO.addMappings(
                mapper -> mapper.using(roleListToStringList).map(User::getRoles, UserDTO::setRoles)
        );
        userToUserDTO.addMappings(
                mapper -> mapper.using(localDateTimeToString).map(User::getTimestamp, UserDTO::setTimestamp)
        );

        TypeMap<User, UserListDTO> userToUserListDTO = modelMapper.createTypeMap(User.class, UserListDTO.class);
        userToUserListDTO.addMappings(
                mapper -> mapper.using(userUrlConverter).map(User::getUsername, UserListDTO::setUri)
        );

        return modelMapper;
    }
}