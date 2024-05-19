package com.infobip.pmf.course;

import com.infobip.pmf.course.storage.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class Warehouse {

    private final UserEntityRepo userRepo;
    private final sLibraryEntityRepo libRepo;
    private final lVersionEntityRepo vRepo;

    public Warehouse(
            UserEntityRepo userRepo,
            sLibraryEntityRepo libRepo,
            lVersionEntityRepo vRepo
    ) {
        this.userRepo = userRepo;
        this.libRepo = libRepo;
        this.vRepo = vRepo;
    }

    public String test(String s) {
        return s;
    }

    // ----------------------- libraries ---------------------------

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

    public MyPage<sLibrary> allLibItems(String groupId, String artifactId, int page, int size) {

        List<sLibraryEntity> entitties = libRepo.findAllFilter(groupId, artifactId);
        List<sLibrary> libs = new ArrayList<>();
        entitties.forEach(
                (le) -> {
                    libs.add(le.assLibrary());
                }
        );

        int end;
        List<sLibrary> content;
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        if (start > entitties.size()) {
           // return last possible page
            start = Math.max(entitties.size() - size, 0);
        }
        end = Math.min(start + pageable.getPageSize(), entitties.size());
        content = libs.subList(start, end);


        return new MyPage(
                content,
                page,
                size,
                (int) Math.ceil((double)(entitties.size()) / size),
                entitties.size());
    }

    public sLibrary registerLib(sLibrary lib) {

        if (lib.versions() != null) {
            // definition conflict
        }
        try {
            return libRepo.save(sLibraryEntity.from(lib)).assLibrary();
        } catch (Exception e) {
            throw new RuntimeException(e);
            // definition conflict 2
        }
    }

    public sLibrary libById(Long id) {

        return libRepo.findById(id).map(sLibraryEntity::assLibrary)
                .orElseThrow(RuntimeException::new);
    }

    public sLibrary updateLibById(Long id, sLibrary lib) {

        libRepo.updateById(
                id,
                lib.groupId(),
                lib.groupId(),
                lib.name(),
                lib.description()
        );

        return libRepo.findById(id).map(sLibraryEntity::assLibrary).orElseThrow(RuntimeException::new);
    }

    public void deleteLibById(Long id) {
        libRepo.deleteById(id);
    }

    // ------------------ versions ----------------------

    public MyPage getLibVersions(Long id, int page, int size) {

        List<lVersion> entitties = vRepo.findLibVersions(id)
                .stream()
                .map(lVersionEntity::aslVersion)
                .collect(Collectors.toList());

        int start = page * size;
        int end = Math.min(start + size, entitties.size());
        if (start > entitties.size()) {
            start = Math.max(0, entitties.size() - size);
        }
        List<lVersion> content = entitties.subList(start, end);

        return new MyPage(
                content,
                page,
                size,
                (int)Math.ceil((double)entitties.size() / size),
                entitties.size());
    }

    public lVersion getLibVersion(Long libraryId, Long versionId) {

        return vRepo.findLibVersion(libraryId, versionId)
                .map(lVersionEntity::aslVersion)
                .orElseThrow(RuntimeException::new);
    }

    public lVersion registerVersion(Long libId, lVersion version) {

        return vRepo.save(lVersionEntity.from(
                version,
                libRepo.findById(libId).orElseThrow(RuntimeException::new)
        )).aslVersion();
    }

    public lVersion updateVersionById(Long libraryId, Long versionId, lVersion givenVErsion) {

        vRepo.updateVersionById(
                libraryId,
                versionId,
                givenVErsion.description(),
                givenVErsion.deprecated()
        );

        return vRepo.findById(versionId)
                .map(lVersionEntity::aslVersion).orElseThrow(RuntimeException::new);
    }

    public boolean authorized(String auth) {
        String[] parts = auth.split(" ");
        if (parts.length != 2) return false;
        if(parts[0].compareTo("App") != 0) return false;
        String key = parts[1];
        List<UserEntity> users = userRepo.findAll();
        return users.stream().anyMatch(
                user -> user.getAppKey().compareTo(key) == 0
        );
    }
}
