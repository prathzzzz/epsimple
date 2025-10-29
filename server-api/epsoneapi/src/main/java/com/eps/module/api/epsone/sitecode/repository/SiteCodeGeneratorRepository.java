package com.eps.module.api.epsone.sitecode.repository;

import com.eps.module.site.SiteCodeGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface SiteCodeGeneratorRepository extends JpaRepository<SiteCodeGenerator, Long> {

    @Query("SELECT scg FROM SiteCodeGenerator scg " +
           "LEFT JOIN FETCH scg.project p " +
           "LEFT JOIN FETCH scg.state s " +
           "WHERE LOWER(p.projectName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.projectCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(s.stateName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(s.stateCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<SiteCodeGenerator> searchSiteCodeGenerators(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT scg FROM SiteCodeGenerator scg " +
           "LEFT JOIN FETCH scg.project " +
           "LEFT JOIN FETCH scg.state " +
           "ORDER BY scg.id ASC")
    Page<SiteCodeGenerator> findAllWithDetails(Pageable pageable);

    @Query("SELECT scg FROM SiteCodeGenerator scg " +
           "LEFT JOIN FETCH scg.project " +
           "LEFT JOIN FETCH scg.state " +
           "WHERE scg.id = :id")
    Optional<SiteCodeGenerator> findByIdWithDetails(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT scg FROM SiteCodeGenerator scg " +
           "WHERE scg.project.id = :projectId " +
           "AND scg.state.id = :stateId")
    Optional<SiteCodeGenerator> findByProjectAndStateForUpdate(
            @Param("projectId") Long projectId,
            @Param("stateId") Long stateId);

    @Query("SELECT scg FROM SiteCodeGenerator scg " +
           "WHERE scg.project.id = :projectId " +
           "AND scg.state.id = :stateId")
    Optional<SiteCodeGenerator> findByProjectAndState(
            @Param("projectId") Long projectId,
            @Param("stateId") Long stateId);

    boolean existsByProjectIdAndStateId(Long projectId, Long stateId);

    boolean existsByProjectIdAndStateIdAndIdNot(Long projectId, Long stateId, Long id);
}
