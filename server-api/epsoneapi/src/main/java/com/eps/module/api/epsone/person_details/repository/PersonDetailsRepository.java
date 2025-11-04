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
           "OR LOWER(pd.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(pd.contactNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<PersonDetails> searchPersonDetails(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT pd FROM PersonDetails pd LEFT JOIN FETCH pd.personType WHERE pd.personType.id = :personTypeId")
    Page<PersonDetails> findByPersonTypeId(@Param("personTypeId") Long personTypeId, Pageable pageable);

    @Query("SELECT pd FROM PersonDetails pd LEFT JOIN FETCH pd.personType ORDER BY pd.firstName ASC, pd.lastName ASC")
    List<PersonDetails> findAllPersonDetailsList();

    Optional<PersonDetails> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
