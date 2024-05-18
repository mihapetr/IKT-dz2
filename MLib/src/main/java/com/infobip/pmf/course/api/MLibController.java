package com.infobip.pmf.course.api;

import com.infobip.pmf.course.*;
import com.infobip.pmf.course.storage.UserEntity;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/libraries")
@RestController
public class MLibController {

    private final Warehouse wh;

    public MLibController(Warehouse warehouse) {
        wh = warehouse;
    }

    @GetMapping
    MyPage allItems(
            @RequestParam(name = "groupId", required = false) String groupId,
            @RequestParam(name = "artifactId", required = false) String artifactId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size
    ) {
        return wh.allLibItems(groupId, artifactId, page, size);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public sLibrary registerLibrary(
            @RequestBody sLibrary library
    ) {
        return wh.registerLib(library);
    }

    @GetMapping("/{id}")
    public sLibrary libById(
            @PathVariable Long id
    ){
        return wh.libById(id);
    }

    @PatchMapping("/{id}")
    public sLibrary updateLibById(
            @PathVariable Long id, @RequestBody sLibrary lib
    ) {
        return wh.updateLibById(id, lib);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLib(
        @PathVariable Long id
    ){
        wh.deleteLibById(id);
    }

    @GetMapping("/{id}/versions")
    public MyPage getLibVersions(
            @PathVariable Long id
    ) {
        return wh.getLibVersions(id);
    }

    @GetMapping("/{lib_id}/versions/{v_id}")
    public lVersion getLibVersion(
            @PathVariable("lib_id") Long libraryId,
            @PathVariable("v_id") Long versionId
    ) {
        return wh.getLibVersion(libraryId, versionId);
    }

    @PostMapping("{id}/versions")
    @ResponseStatus(HttpStatus.CREATED)
    public lVersion registerVersion(
            @PathVariable Long id,
            @RequestBody lVersion version
    ) {
        return wh.registerVersion(id, version);
    }

    @PatchMapping("/{lib_id}/versions/{v_id}")
    public lVersion updateVErsionById(
            @PathVariable("lib_id") Long libraryId,
            @PathVariable("v_id") Long versionId,
            @RequestBody sLibrary lib
    ) {
        return wh.updateVersionById(libraryId, versionId);
    }
}
