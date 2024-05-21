package com.infobip.pmf.course.api;

import com.infobip.pmf.course.*;
import com.infobip.pmf.course.exception.InvalidParameters;
import com.infobip.pmf.course.exception.UnauthorizedException;
import com.infobip.pmf.course.storage.UserEntity;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/libraries")
@RestController
public class MLibController {

    private final Warehouse wh;

    public MLibController(Warehouse warehouse) {
        wh = warehouse;
    }

    private final List<String> allowedParams = List.of(
            "groupId",
            "artifactId",
            "page",
            "size"
    );

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    MyPage<sLibrary> allItems(
            @RequestHeader(value = "Authorization", required = true) String auth,
            @RequestParam Map<String, String> allParam,
            @RequestParam(name = "groupId", required = false) String groupId,
            @RequestParam(name = "artifactId", required = false) String artifactId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size
    ) {
        allParam.keySet().forEach(
                paramName -> {
                    if (!allowedParams.contains(paramName)) throw new InvalidParameters();
                }
        );
        return wh.allLibItems(groupId, artifactId, page, size);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public sLibrary registerLibrary(
            @RequestHeader(value = "Authorization", required = true) String auth,
            @RequestBody sLibrary library
    ) {
        if (!authorized(auth)) throw new UnauthorizedException();
        return wh.registerLib(library);
    }

    @GetMapping("/{id}")
    public sLibrary libById(
            @RequestHeader(value = "Authorization", required = true) String auth,
            @PathVariable Long id
    ){
        if (!authorized(auth)) throw new UnauthorizedException();
        return wh.libById(id);
    }

    @PatchMapping("/{id}")
    public sLibrary updateLibById(
            @RequestHeader(value = "Authorization", required = true) String auth,
            @PathVariable Long id, @RequestBody sLibrary lib
    ) {
        if (!authorized(auth)) throw new UnauthorizedException();
        return wh.updateLibById(id, lib);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLib(
            @RequestHeader(value = "Authorization", required = true) String auth,
            @PathVariable Long id
    ){
        if (!authorized(auth)) throw new UnauthorizedException();
        wh.deleteLibById(id);
    }

    @GetMapping("/{id}/versions")
    public MyPage getLibVersions(
            @RequestHeader(value = "Authorization", required = true) String auth,
            @PathVariable Long id,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size
    ) {
        if (!authorized(auth)) throw new UnauthorizedException();
        return wh.getLibVersions(id, page, size);
    }

    @GetMapping("/{lib_id}/versions/{v_id}")
    public lVersion getLibVersion(
            @RequestHeader(value = "Authorization", required = true) String auth,
            @PathVariable("lib_id") Long libraryId,
            @PathVariable("v_id") Long versionId
    ) {
        if (!authorized(auth)) throw new UnauthorizedException();
        return wh.getLibVersion(libraryId, versionId);
    }

    @PostMapping("{id}/versions")
    @ResponseStatus(HttpStatus.CREATED)
    public lVersion registerVersion(
            @RequestHeader(value = "Authorization", required = true) String auth,
            @PathVariable Long id,
            @RequestBody lVersion version
    ) {
        if (!authorized(auth)) throw new UnauthorizedException();
        return wh.registerVersion(id, version);
    }

    @PatchMapping("/{lib_id}/versions/{v_id}")
    public lVersion updateVErsionById(
            @RequestHeader(value = "Authorization", required = true) String auth,
            @PathVariable("lib_id") Long libraryId,
            @PathVariable("v_id") Long versionId,
            @RequestBody lVersion version
    ) {
        if (!authorized(auth)) throw new UnauthorizedException();
        return wh.updateVersionById(libraryId, versionId, version);
    }

    private boolean authorized(String auth) {
        return wh.authorized(auth);
    }
}
