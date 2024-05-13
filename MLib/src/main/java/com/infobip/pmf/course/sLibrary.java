package com.infobip.pmf.course;

import java.util.List;

public record sLibrary (
        String groupId,
        String artifactId,
        List<String> versions,
        String name,
        String description
) {}
