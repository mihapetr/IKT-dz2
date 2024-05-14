package com.infobip.pmf.course;

import jakarta.validation.constraints.NotNull;

public record lVersion(
        @NotNull Long id,
        @NotNull String semanticVersion,
        String description,
        @NotNull Boolean deprecated
) {
}
