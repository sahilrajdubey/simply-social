package com.simplysocial.server.repository;

import com.simplysocial.server.domain.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InstitutionRepository extends JpaRepository<Institution, UUID> {
    Optional<Institution> findByDomain(String domain);
}
