package com.eps.module.api.epsone.managedproject.repository;

import com.eps.module.bank.ManagedProject;
import com.eps.module.site.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagedProjectRepository extends JpaRepository<ManagedProject, Long> {

    @Query("SELECT mp FROM ManagedProject mp LEFT JOIN FETCH mp.bank WHERE mp.id = :id")
    Optional<ManagedProject> findByIdWithBank(@Param("id") Long id);

    @Query("SELECT mp FROM ManagedProject mp LEFT JOIN FETCH mp.bank")
    Page<ManagedProject> findAllWithBank(Pageable pageable);

    @Query("SELECT mp FROM ManagedProject mp LEFT JOIN FETCH mp.bank " +
           "WHERE LOWER(mp.projectName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(mp.projectCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(mp.projectType) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<ManagedProject> searchManagedProjects(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Simple query without JOIN FETCH for dependency checking
    Page<ManagedProject> findByBankId(Long bankId, Pageable pageable);

    @Query("SELECT mp FROM ManagedProject mp LEFT JOIN FETCH mp.bank ORDER BY mp.projectName ASC")
    List<ManagedProject> findAllManagedProjectsList();

    Optional<ManagedProject> findByProjectCode(String projectCode);

    boolean existsByProjectCode(String projectCode);

    boolean existsByProjectCodeAndIdNot(String projectCode, Long id);

    @Query("SELECT s FROM Site s WHERE s.project.id = :projectId")
    Page<Site> findSitesByProjectId(@Param("projectId") Long projectId, Pageable pageable);
}
