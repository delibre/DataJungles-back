package com.delibre.datajungles.repository;

import com.delibre.datajungles.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByFolderId(Long id);

    @Modifying
    @Query(value = "delete from file where id = (:id)", nativeQuery = true)
    void myDelete(@Param("id") Long id);
}
