package com.delibre.datajungles.repository;

import com.delibre.datajungles.model.user.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> getByName(String name);
}
