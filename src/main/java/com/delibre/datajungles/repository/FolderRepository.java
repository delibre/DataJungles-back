package com.delibre.datajungles.repository;

import com.delibre.datajungles.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Modifying
    @Query(value = "insert into folder (name) values (:name)", nativeQuery = true)
    void createRootFolder(@Param("name") String name);

    Folder findByName(String name);

    List<Folder> findAllByRootFolderId(Long id);
}