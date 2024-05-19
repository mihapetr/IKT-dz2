package com.infobip.pmf.course.api;

import com.infobip.pmf.course.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class Scolder {

    @ExceptionHandler(LibIncorrect.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Scolding handleIncorrectLib() {
        return new Scolding(
                "Library format incorrect.",
                "Please respect new library specification. Versions cannot be specified here."
        );
    }

    @ExceptionHandler(ArtifactAlreadyInGroup.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Scolding handleArtifactInGroupPresent() {
        return new Scolding(
                "Artifact already in given group.",
                "Check groupId and artifactId combination for mistakes. They have to make a unique pair"
        );
    }

    @ExceptionHandler(LibNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Scolding handleLibNotFound() {
        return new Scolding(
                "No library with given id.",
                "Check id of the library you are trying to access."
        );
    }

    @ExceptionHandler(BadLibPatchData.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Scolding handleBadLibPatch() {
        return new Scolding(
                "Incorrect library update data.",
                "Please respect library patch specification. Only description and deprecated fields can be updated."
        );
    }

    @ExceptionHandler(VersionNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Scolding handleVersionNotFound() {
        return new Scolding(
                "Requested version not found.",
                "Check the id-s of requested library and version."
        );
    }

    @ExceptionHandler(VersionIncorrect.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Scolding handleVersionIncorrect() {
        return new Scolding(
                "Version format incorrect.",
                "Please respect new version specification."
        );
    }

    @ExceptionHandler(VersionSemanticCOnflict.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Scolding handeSemanticConflict() {
        return new Scolding(
                "Version with the same semantic version already exists.",
                "Enter a different version."
        );
    }

    @ExceptionHandler(BadVersionPatchData.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Scolding handeBadVersionPatchData() {
        return new Scolding(
                "Only description and deprecated fields can be modified >:(",
                "Frick you."
        );
    }

    @ExceptionHandler(VersionDeprecated.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Scolding handeDeprecatedConflict() {
        return new Scolding(
                "Only description and deprecated fields can be modified >:(",
                "Frick you."
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Scolding handeUnauthorized() {
        return new Scolding(
                "No place for you here.",
                "Frick you."
        );
    }

    public record Scolding(
       String message,
       String action
    ) {}
}
