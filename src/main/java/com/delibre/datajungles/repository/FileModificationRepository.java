package com.delibre.datajungles.repository;

import com.delibre.datajungles.model.FileModification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileModificationRepository extends JpaRepository<FileModification, Long> {
   FileModification findByFileId(Long fileId);

   @Modifying
   @Query(value = "delete from file_modification where file_id = (:id)", nativeQuery = true)
   void myDelete(@Param("id") Long id);
}
