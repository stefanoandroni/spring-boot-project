package org.stand.springbootproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.stand.springbootproject.entity.role.Role;
import org.stand.springbootproject.entity.role.RoleName;
import org.stand.springbootproject.entity.user.User;
import org.stand.springbootproject.repository.RoleRepository;
import org.stand.springbootproject.repository.UserRepository;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Save roles to repository
        Role userRole = new Role(RoleName.ROLE_USER);
        Role adminRole = new Role(RoleName.ROLE_ADMIN);
        roleRepository.saveAll(Arrays.asList(userRole, adminRole));

        // Save admin user to repository
        User adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin")) // TODO: from file (?)
                .email("admin@stand.org")
                .firstName("admin")
                .lastName("admin")
                .roles(Arrays.asList(userRole, adminRole))
                .enabled(true) // no need for email verification
                .build();
        userRepository.save(adminUser);
    }
}