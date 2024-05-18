package com.infobip.pmf.course;

import com.infobip.pmf.course.storage.UserEntity;
import com.infobip.pmf.course.storage.UserEntityRepo;
import com.infobip.pmf.course.storage.sLibraryEntity;
import com.infobip.pmf.course.storage.sLibraryEntityRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public MyPage allLibItems(String groupId, String artifactId, int page, int size) {

        List<sLibraryEntity> entitties = libRepo.findAllFilter(groupId, artifactId);
        List<sLibrary> libs = new ArrayList<>();
        entitties.forEach(
                (le) -> {
                    libs.add(le.assLibrary());
                }
        );

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), entitties.size());

        List<sLibrary> content = libs.subList(start, end);

        return new MyPage(
                content,
                page,
                size,
                (int) Math.ceil((double)(entitties.size()) / size),
                entitties.size());
    }

    public sLibrary registerLib(sLibrary lib) {
        return new sLibrary(1L,"","",new ArrayList<>(),"","");
    }

    public sLibrary libById(Long id) {
        return new sLibrary(1L,"","",new ArrayList<>(),"","");
    }

    public sLibrary updateLibById(Long id, sLibrary lib) {
        return new sLibrary(1L,"","",new ArrayList<>(),"","");
    }

    public void deleteLibById(Long id) {

    }

    public MyPage getLibVersions(Long id) {
        return new MyPage(null, 1, 1,1,1);
    }

    public lVersion getLibVersion(Long libraryId, Long versionId) {
        return new lVersion(1L,"","",true);
    }

    public lVersion registerVersion(Long id, lVersion version) {
        return new lVersion(1L,"","",true);
    }

    public lVersion updateVersionById(Long libraryId, Long versionId) {
        return new lVersion(1L,"","",true);
    }
}
