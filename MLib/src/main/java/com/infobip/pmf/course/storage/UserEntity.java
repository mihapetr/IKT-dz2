package com.infobip.pmf.course.storage;

import com.infobip.pmf.course.Account;
import jakarta.persistence.*;

@Entity
@Table(name = "l_user", schema = "m_lidb")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "app_key")
    private String appKey;

    public Account asAccount() {
        return new Account(id, username, appKey);
    }

    public String getAppKey() {
        return appKey;
    }
}
