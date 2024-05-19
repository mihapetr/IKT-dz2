package com.infobip.pmf.course.storage;

import com.infobip.pmf.course.lVersion;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "version", schema = "m_lidb")
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

    @Column(name = "v_release_date")
    ZonedDateTime releaseDate;

    @Column(nullable = false, name = "v_deprecated")
    Boolean deprecated;

    public static lVersionEntity from(lVersion version, sLibraryEntity givenLib) {

        lVersionEntity e = new lVersionEntity();
        return e.setLibrary(givenLib)
                .setSemanticVersion(version.semanticVersion())
                .setDescription(version.description())
                .generateReleaseDate()
                .setDeprecated(version.deprecated());
    }

    public lVersionEntity setLibrary(sLibraryEntity v) {
        this.library = v;
        return this;
    }

    public lVersionEntity setSemanticVersion(String semanticVersion) {
        this.semanticVersion = semanticVersion;
        return this;
    }

    public lVersionEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    private lVersionEntity generateReleaseDate() {
        this.releaseDate = ZonedDateTime.now();
        return this;
    }

    public lVersionEntity setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    public lVersion aslVersion() {
        return new lVersion(id, semanticVersion, description, releaseDate, deprecated);
    }

    public Long getId() {
        return id;
    }
}
