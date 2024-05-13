package com.infobip.pmf.course.storage;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface lVersionEntityRepo extends ListCrudRepository<lVersionEntity, Long> {
}
