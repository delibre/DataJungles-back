package com.delibre.datajungles.repository;

import com.delibre.datajungles.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> getAllByFolderId(Long folderId);
}
