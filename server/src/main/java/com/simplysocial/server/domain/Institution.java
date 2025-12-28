package com.simplysocial.server.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "institutions")
public class Institution {
    @Id
    @GeneratedValue
    private UUID id;
    private String domain;
    private String name;

    public UUID getId() {
        return id;
    }

    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }
}
