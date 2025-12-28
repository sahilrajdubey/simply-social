package com.simplysocial.server.service;

import com.simplysocial.server.domain.Institution;
import com.simplysocial.server.domain.User;
import com.simplysocial.server.repository.InstitutionRepository;
import com.simplysocial.server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final InstitutionRepository intitutionRepo;

    public AuthService(UserRepository userRepo, InstitutionRepository institutionRepo) {
        this.userRepo = userRepo;
        this.intitutionRepo = institutionRepo;
    }

    @Transactional
    public User syncUser(String authId, String email) {
        UUID uuid = UUID.fromString(authId);

        // If user exists, return them
        return userRepo.findById(uuid).orElseGet(() -> {
            // New User: Determine institution
            String domain = email.substring(email.indexOf("@") + 1);

            Institution institution = intitutionRepo.findByDomain(domain)
                    .orElseThrow(() -> new RuntimeException("Institution Not Enrolled Yet: " + domain));

            User newUser = new User(uuid, email, institution.getId());
            return userRepo.save(newUser);
        });
    }
}
