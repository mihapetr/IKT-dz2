package com.infobip.pmf.course;

import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public record lVersion(
        Long id,
        String semanticVersion,
        String description,
        ZonedDateTime releaseDate,
        @NotNull Boolean deprecated
) {
}
