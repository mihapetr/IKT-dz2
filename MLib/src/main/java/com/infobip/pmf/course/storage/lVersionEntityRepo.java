package com.infobip.pmf.course.storage;

import com.infobip.pmf.course.lVersion;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface lVersionEntityRepo extends ListCrudRepository<lVersionEntity, Long> {

    @Query("""
            select v from lVersionEntity v
            where v.library.id = :libId
            """)
    List<lVersionEntity> findLibVersions(
            @Param("libId") Long libId
    );

    @Query("""
            select v from lVersionEntity v
            where v.library.id = :libId and v.id = :versionId
            """)
    Optional<lVersionEntity> findLibVersion(
            @Param("libId") Long libraryId,
            @Param("versionId") Long versionId
    );

    @Modifying
    @Query("""
            update lVersionEntity
            set description = :description, deprecated = :deprecated
            where library.id = :libId and id = :versionId
            """)
    void updateVersionById(
            @Param("libId") Long libId,
            @Param("versionId") Long versionId,
            @Param("description") String description,
            @Param("deprecated") Boolean deprecated
    );

    @Query("""
            select v from lVersionEntity v
            where v.semanticVersion = :semVer
            """)
    List<lVersionEntity> findBySemVer(
            @Param("semVer") String semVer
    );
}
