package org.stand.springbootproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stand.springbootproject.entity.role.Role;
import org.stand.springbootproject.entity.role.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
