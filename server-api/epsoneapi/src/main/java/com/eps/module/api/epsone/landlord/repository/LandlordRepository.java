package com.eps.module.api.epsone.landlord.repository;

import com.eps.module.vendor.Landlord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LandlordRepository extends JpaRepository<Landlord, Long> {

    /**
     * Search landlords by person details (name, contact number)
     */
    @Query("SELECT l FROM Landlord l " +
            "LEFT JOIN FETCH l.landlordDetails ld " +
            "WHERE LOWER(ld.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ld.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ld.contactNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Landlord> searchLandlords(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Get all landlords with eager loading
     */
    @Query("SELECT l FROM Landlord l " +
            "LEFT JOIN FETCH l.landlordDetails ld")
    List<Landlord> findAllList();

    /**
     * Find by landlord details (PersonDetails)
     */
    Optional<Landlord> findByLandlordDetailsId(Long landlordDetailsId);

    /**
     * Count landlords by landlord details
     */
    long countByLandlordDetailsId(Long landlordDetailsId);

    /**
     * Get all landlords for export with eager loading to avoid LazyInitializationException
     */
    @Query("SELECT l FROM Landlord l " +
            "LEFT JOIN FETCH l.landlordDetails ld " +
            "LEFT JOIN FETCH ld.personType pt")
    List<Landlord> findAllForExport();
}
