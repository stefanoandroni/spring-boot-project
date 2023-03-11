package org.stand.springbootproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stand.springbootproject.entity.user.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
