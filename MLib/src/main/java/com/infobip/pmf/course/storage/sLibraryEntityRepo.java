package com.infobip.pmf.course.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface sLibraryEntityRepo extends JpaRepository<sLibraryEntity, Long> {

    @Query("""
            select e from sLibraryEntity e
            where
            (:groupId is null or e.groupId = :groupId)
            and
            (:artifactId is null or e.artifactId = :artifactId)
            """)
    List<sLibraryEntity> findAllFilter(
            @Param("groupId") String groupId,
            @Param("artifactId") String artifactId
    );

    @Modifying
    @Query("""
            update sLibraryEntity
            set name = :name
            where id = :id
            """)
    void updateNameById(
            @Param("id") Long id,
            @Param("name") String name
    );

    @Modifying
    @Query("""
            update sLibraryEntity
            set description = :description
            where id = :id
            """)
    void updateDescriptionById(
            @Param("id") Long id,
            @Param("description") String description
    );
}
