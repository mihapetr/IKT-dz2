package com.infobip.pmf.course.storage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface sLibraryEntityRepo extends ListCrudRepository<sLibraryEntity, Long> {

    @Query("")
    List<sLibraryEntity> findSth(int input);
}
