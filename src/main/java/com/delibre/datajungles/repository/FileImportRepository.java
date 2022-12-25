package com.delibre.datajungles.repository;

import com.delibre.datajungles.model.FileImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileImportRepository extends JpaRepository<FileImport, Long> {
    FileImport findByFileId(Long fileId);

    @Modifying
    @Query(value = "delete from file_import where file_id = (:id)", nativeQuery = true)
    void myDelete(@Param("id") Long id);
}