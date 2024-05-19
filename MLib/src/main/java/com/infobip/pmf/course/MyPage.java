package com.infobip.pmf.course;

import java.util.List;

public record MyPage<T> (
        List<T> results,
        int page,
        int size,
        int totalPages,
        int totalResults
) {
}
