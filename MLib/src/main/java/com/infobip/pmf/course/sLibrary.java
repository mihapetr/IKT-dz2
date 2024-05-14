package com.infobip.pmf.course;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record sLibrary (
        @NotNull Long id,
        @NotNull String groupId,
        @NotNull String artifactId,
        @NotNull List<Long> versions,
        @NotNull String name,
        String description
) {}
