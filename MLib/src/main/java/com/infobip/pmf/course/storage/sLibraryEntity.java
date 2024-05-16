package com.infobip.pmf.course.storage;

import com.infobip.pmf.course.sLibrary;
import jakarta.persistence.*;

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

    @Column
    private String description;

    public sLibrary assLibrary() {
        return new sLibrary(id, groupId, artifactId,
                versionsIdList(),
                name, description);
    }

    List<Long> versionsIdList() {
        List<Long> ids = new ArrayList<Long>();
        versions.forEach(
                (version) -> ids.add(version.getId())
        );
        return ids;
    }
}
