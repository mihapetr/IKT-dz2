package com.infobip.pmf.course;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.List;

public record sLibrary (
        Long id,
        @NotNull String groupId,
        @NotNull String artifactId,
        @NotNull List<Long> versions,
        @NotNull String name,
        String description
) {}
