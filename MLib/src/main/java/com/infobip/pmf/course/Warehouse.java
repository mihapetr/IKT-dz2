package com.infobip.pmf.course;

import com.infobip.pmf.course.storage.UserEntity;
import com.infobip.pmf.course.storage.UserEntityRepo;
import com.infobip.pmf.course.storage.sLibraryEntity;
import com.infobip.pmf.course.storage.sLibraryEntityRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Warehouse {

    private final UserEntityRepo userRepo;
    private final sLibraryEntityRepo libRepo;

    public Warehouse(
            UserEntityRepo userRepo,
            sLibraryEntityRepo libRepo
    ) {
        this.userRepo = userRepo;
        this.libRepo = libRepo;
    }

    public String test(String s) {
        return s;
    }

    public List<Account> allUsers() {
        List<UserEntity> entitties = userRepo.findAll();
        List<Account> users = new ArrayList<>();
        entitties.forEach(
                (ue) -> {
                    users.add(ue.asAccount());
                }
        );
        return users;
    }

    public List<sLibrary> allLibItems(String groupId, String artifactId, int page, int size) {
        List<sLibraryEntity> entitties = libRepo.findAll();
        List<sLibrary> libs= new ArrayList<>();
        entitties.forEach(
                (le) -> {
                    libs.add(le.assLibrary());
                }
        );
        return libs;
    }
}
