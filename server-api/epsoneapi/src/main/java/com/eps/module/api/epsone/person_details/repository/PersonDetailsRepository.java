package com.eps.module.api.epsone.person_details.repository;

import com.eps.module.person.PersonDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonDetailsRepository extends JpaRepository<PersonDetails, Long> {

    @Query("SELECT pd FROM PersonDetails pd LEFT JOIN FETCH pd.personType WHERE pd.id = :id")
    Optional<PersonDetails> findByIdWithPersonType(@Param("id") Long id);

    @Query("SELECT pd FROM PersonDetails pd LEFT JOIN FETCH pd.personType")
    Page<PersonDetails> findAllWithPersonType(Pageable pageable);

    @Query("SELECT pd FROM PersonDetails pd LEFT JOIN FETCH pd.personType " +
           "WHERE LOWER(pd.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(pd.middleName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(pd.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(pd.contactNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<PersonDetails> searchPersonDetails(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT pd FROM PersonDetails pd LEFT JOIN FETCH pd.personType WHERE pd.personType.id = :personTypeId")
    Page<PersonDetails> findByPersonTypeId(@Param("personTypeId") Long personTypeId, Pageable pageable);

    @Query("SELECT pd FROM PersonDetails pd LEFT JOIN FETCH pd.personType ORDER BY pd.firstName ASC, pd.lastName ASC")
    List<PersonDetails> findAllPersonDetailsList();

    @Query("SELECT pd FROM PersonDetails pd LEFT JOIN FETCH pd.personType")
    List<PersonDetails> findAllForExport();

    // Bulk upload helper methods
    boolean existsByContactNumber(String contactNumber);
    
    Optional<PersonDetails> findByContactNumber(String contactNumber);
    
    @Query("SELECT pd FROM PersonDetails pd WHERE " +
           "LOWER(CONCAT(COALESCE(pd.firstName, ''), ' ', COALESCE(pd.middleName, ''), ' ', COALESCE(pd.lastName, ''))) " +
           "LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<PersonDetails> findByFullNameContaining(@Param("fullName") String fullName);
    
    @Query("SELECT pd FROM PersonDetails pd WHERE " +
           "LOWER(TRIM(CONCAT(COALESCE(pd.firstName, ''), ' ', COALESCE(pd.middleName, ''), ' ', COALESCE(pd.lastName, '')))) " +
           "= LOWER(TRIM(:fullName))")
    Optional<PersonDetails> findByFullNameExact(@Param("fullName") String fullName);
}
