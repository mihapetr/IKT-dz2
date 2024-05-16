package com.infobip.pmf.course;

import jakarta.validation.constraints.NotNull;

public record Account(
        @NotNull Long id,
        @NotNull String username,
        @NotNull String passHash
) {
}
