package com.infobip.pmf.course;

import com.infobip.pmf.course.exception.*;
import com.infobip.pmf.course.storage.*;
import jakarta.transaction.Transactional;
import org.apache.tomcat.jni.LibraryNotFoundError;
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

        // if requested page too big, give last possible page
        page = Math.max(0, (int)Math.ceil((double)end / size) - 1);

        return new MyPage(
                content,
                page,
                size,
                (int) Math.ceil((double)(entitties.size()) / size),
                entitties.size());
    }

    public sLibrary registerLib(sLibrary lib) {

        // check user's format
        if (!legalLibFormat(lib)) throw new LibIncorrect();

        // check group artifact combo
        if (
                !libRepo.findAllFilter(lib.groupId(), lib.artifactId()).isEmpty()   // there is already an entry with given combo
        ) throw new ArtifactAlreadyInGroup();

        //System.out.println("got to saving when register lib " + lib.name());

        sLibraryEntity partial;

        partial = libRepo.save(
                sLibraryEntity.from(lib)
        );

        return partial.assLibrary();
    }

    private boolean legalLibFormat(sLibrary lib) {
        if (lib.id() != null ||
                lib.groupId() == null ||
                lib.name() == null ||
                lib.artifactId() == null ||
                lib.versions() != null

        ) return false;
        return true;
    }

    public sLibrary libById(Long id) {

        return libRepo.findById(id).map(sLibraryEntity::assLibrary)
                .orElseThrow(LibNotFound::new);
    }

    public sLibrary updateLibById(Long id, sLibrary lib) {

        if (!libUpdateLegal(lib)) throw new BadLibPatchData();

        if(
                libRepo.findById(id).isEmpty()
        ) throw new LibNotFound();

        if (lib.name() != null)
        libRepo.updateNameById(
                id,
                lib.name()
        );

        if (lib.description() != null)
        libRepo.updateDescriptionById(
                id,
                lib.description()
        );

        return libRepo.findById(id).map(sLibraryEntity::assLibrary).orElseThrow(RuntimeException::new);
    }

    private boolean libUpdateLegal(sLibrary lib) {
        if (lib.id() != null ||
                lib.artifactId() != null ||
                lib.groupId() != null
        ) return false;
        return true;
    }

    public void deleteLibById(Long id) {

        libRepo.findById(id).orElseThrow(LibNotFound::new);
        libRepo.deleteById(id);
    }

    // ------------------ versions ----------------------

    public MyPage getLibVersions(Long id, int page, int size) {

        libRepo.findById(id).orElseThrow(LibNotFound::new);

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

        // if requested page too big
        page = Math.max(0, (int)Math.ceil((double)end / size) - 1);

        return new MyPage(
                content,
                page,
                size,
                (int)Math.ceil((double)entitties.size() / size),
                entitties.size());
    }

    public lVersion getLibVersion(Long libraryId, Long versionId) {

        vRepo.findById(versionId).orElseThrow(VersionNotFound::new);
        libRepo.findById(libraryId).orElseThrow(LibNotFound::new);

        return vRepo.findLibVersion(libraryId, versionId)
                .map(lVersionEntity::aslVersion)
                .orElseThrow(RuntimeException::new);
    }

    public lVersion registerVersion(Long libId, lVersion version) {

        // check if format legal
        if (!legalVersionFormat(version)) throw new VersionIncorrect();

        // check if library present
        libRepo.findById(libId).orElseThrow(LibNotFound::new);

        // sem ver rules
        String semVer = version.semanticVersion();
        String regex = "^[1-9]\\d*\\.[1-9]\\d*\\.[1-9]\\d*$";
        if (!semVer.matches(regex)) throw new VersionIncorrect();

        // check sem ver conflict
        if (
                !vRepo.findBySemVer(semVer).isEmpty()
        ) throw new VersionSemanticConflict();

        return vRepo.save(lVersionEntity.from(
                version,
                libRepo.findById(libId).orElseThrow(RuntimeException::new)
        )).aslVersion();
    }

    private boolean legalVersionFormat(lVersion version) {
        if(version.id() != null ||
                version.semanticVersion() == null ||
                version.description() == null ||
                version.releaseDate() != null
        ) return false;
        return true;
    }

    public lVersion updateVersionById(Long libraryId, Long versionId, lVersion givenVersion) {

        // check if version present or ( deprecated and restored )
        libRepo.findById(libraryId).orElseThrow(LibNotFound::new);
        if(
                vRepo.findById(versionId).map(version -> version.getDeprecated()).orElseThrow(LibNotFound::new) // returns deprecated
                && givenVersion.deprecated() == false
        ) throw new VersionIncorrect();


        if (!legalVersionPatchFormat(givenVersion)) throw new BadVersionPatchData();

        vRepo.updateVersionById(
                libraryId,
                versionId,
                givenVersion.description(),
                givenVersion.deprecated()
        );

        return vRepo.findById(versionId)
                .map(lVersionEntity::aslVersion).orElseThrow(RuntimeException::new);
    }

    private boolean legalVersionPatchFormat(lVersion givenVersion) {
        if(givenVersion.id() != null ||
                givenVersion.releaseDate() != null ||
                givenVersion.semanticVersion() != null
        ) return false;
        return  true;
    }

    public boolean authorized(String auth) {

        String[] parts = auth.split(" ");
        if (parts.length != 2) return false;
        if(parts[0].compareTo("App") != 0) return false;
        String key = parts[1];
        List<UserEntity> users = userRepo.findAll();

        boolean match = users.stream().anyMatch(
                user -> user.getAppKey().compareTo(key) == 0
        );

        System.out.println(
                "auth:" + parts[0] + "|" + parts[1] + "\nmatch:" + match
        );

        return match;
    }
}
