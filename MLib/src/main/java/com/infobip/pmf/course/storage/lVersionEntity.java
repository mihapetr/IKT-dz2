package com.infobip.pmf.course.storage;

import com.infobip.pmf.course.lVersion;
import jakarta.persistence.*;

@Entity
public class lVersionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // library -- 1:N -- version
    @ManyToOne
    @JoinColumn(name = "v_lib_id")
    private sLibraryEntity library;

    @Column(nullable = false, name = "v_semantic_version")
    String semanticVersion;

    @Column(name = "v_description")
    String description;

    @Column(nullable = false, name = "v_deprecated")
    Boolean deprecated;

    public lVersion aslVersion() {
        return new lVersion(id, semanticVersion, description, deprecated);
    }

    public Long getId() {
        return id;
    }
}
