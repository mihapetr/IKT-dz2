package com.infobip.pmf.course.storage;

import ch.qos.logback.classic.model.LoggerModel;
import com.infobip.pmf.course.sLibrary;
import jakarta.persistence.*;
import org.apache.catalina.Group;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="s_library", schema="m_lidb")
public class sLibraryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "lib_group_id")
    private String groupId;

    @Column(nullable = false, name = "lib_artifact_id")
    private String artifactId;

    // library -- 1:N -- version
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<lVersionEntity> versions;

    @Column(nullable = false, name = "lib_name")
    private String name;

    @Column(name = "description")
    private String description;

    public static sLibraryEntity from(sLibrary lib) {
        sLibraryEntity e = new sLibraryEntity();
        return e.setGroupId(lib.groupId()).
                setArtifactId(lib.artifactId()).
                setVersions().
                setName(lib.name()).
                setDescription(lib.description());
    }

    public sLibraryEntity setVersions() {
        versions = new ArrayList<>();
        return this;
    }

    private sLibraryEntity setDescription(String v) {
        this.description = v;
        return this;
    }

    private sLibraryEntity setName(String v) {
        this.name = v;
        return this;
    }

    private sLibraryEntity setArtifactId(String v) {
        this.artifactId = v;
        return this;
    }

    private sLibraryEntity setGroupId(String v) {
        this.groupId = v;
        return this;
    }

    public sLibrary assLibrary() {
        return new sLibrary(id, groupId, artifactId,
                versionsIdList(),
                name, description);
    }

    // doesn't work on object returned by sLibraryEntity::save !!
    List<Long> versionsIdList() {
        List<Long> ids = new ArrayList<Long>();
        if (versions == null || versions.isEmpty()) return ids;
        versions.forEach(
                (version) -> ids.add(version.getId())
        );
        return ids;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {

        return "id: " + id + "," +
                "groupId: " + groupId + "," +
                "artifactId: " + artifactId + "," +
                "name: " + name + "," +
                "versions: " + description + "," +
                "versions empty: " + versions.isEmpty();
    }
}
