package com.eps.module.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eps.module.auth.entity.Permission;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByName(String name);

    Boolean existsByName(String name);

    List<Permission> findByIsActive(Boolean isActive);

    List<Permission> findByScope(String scope);

    List<Permission> findByAction(String action);

    List<Permission> findByCategory(String category);

    List<Permission> findByIsSystemPermission(Boolean isSystemPermission);
}
