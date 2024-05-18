package com.infobip.pmf.course;

import java.util.List;

public record MyPage (
        List<sLibrary> results,
        int page,
        int size,
        int totalPages,
        int totalResults
) {
}
