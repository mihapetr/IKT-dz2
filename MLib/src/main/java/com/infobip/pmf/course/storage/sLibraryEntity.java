package com.infobip.pmf.course.storage;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="Library", schema="MLidb")
public class sLibraryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "lib_group_id")
    private String groupId;

    @Column(nullable = false, name = "lib_artifact_id")
    private String artifactId;

    @Column()
    private List<Long> versions;

    @Column(nullable = false, name = "lib_name")
    private String name;

    @Column
    private String description;
}
