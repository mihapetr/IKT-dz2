package com.infobip.pmf.course.storage;

import jakarta.persistence.*;

@Entity
public class lVersionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, name = "v_semantic_version")
    String semanticVersion;

    @Column(name = "v_description")
    String description;

    @Column(nullable = false, name = "v_deprecated")
    Boolean deprecated;
}
