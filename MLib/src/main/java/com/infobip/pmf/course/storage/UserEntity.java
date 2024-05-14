package com.infobip.pmf.course.storage;

import jakarta.persistence.*;

@Entity
@Table(name = "User", schema = "Mlidb")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "passHash")
    private String passHash;
}
