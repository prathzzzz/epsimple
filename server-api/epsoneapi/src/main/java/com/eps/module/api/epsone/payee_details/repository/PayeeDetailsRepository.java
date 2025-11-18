package com.eps.module.api.epsone.payee_details.repository;

import com.eps.module.payment.PayeeDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayeeDetailsRepository extends JpaRepository<PayeeDetails, Long> {

    /**
     * Search payee details by payee name, PAN, Aadhaar, beneficiary name, or account number
     */
    @Query("SELECT pd FROM PayeeDetails pd WHERE " +
           "LOWER(pd.payeeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(pd.panNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(pd.aadhaarNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(pd.beneficiaryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(pd.accountNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<PayeeDetails> searchPayeeDetails(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Get all payee details as a list (no pagination) - for dropdowns
     */
    @Query("SELECT pd FROM PayeeDetails pd ORDER BY pd.payeeName ASC")
    List<PayeeDetails> findAllList();

    /**
     * Check if PAN number hash exists (for uniqueness validation during create)
     */
    boolean existsByPanNumberHash(String panNumberHash);

    /**
     * Check if PAN number hash exists for another payee (for uniqueness validation during update)
     */
    @Query("SELECT CASE WHEN COUNT(pd) > 0 THEN true ELSE false END FROM PayeeDetails pd WHERE pd.panNumberHash = :panNumberHash AND pd.id <> :id")
    boolean existsByPanNumberHashAndIdNot(@Param("panNumberHash") String panNumberHash, @Param("id") Long id);

    /**
     * Check if Aadhaar number hash exists (for uniqueness validation during create)
     */
    boolean existsByAadhaarNumberHash(String aadhaarNumberHash);

    /**
     * Check if Aadhaar number hash exists for another payee (for uniqueness validation during update)
     */
    @Query("SELECT CASE WHEN COUNT(pd) > 0 THEN true ELSE false END FROM PayeeDetails pd WHERE pd.aadhaarNumberHash = :aadhaarNumberHash AND pd.id <> :id")
    boolean existsByAadhaarNumberHashAndIdNot(@Param("aadhaarNumberHash") String aadhaarNumberHash, @Param("id") Long id);

    /**
     * Check if account number hash exists for a specific bank (for uniqueness validation during create)
     */
    @Query("SELECT CASE WHEN COUNT(pd) > 0 THEN true ELSE false END FROM PayeeDetails pd WHERE pd.accountNumberHash = :accountNumberHash AND pd.bank.id = :bankId")
    boolean existsByAccountNumberHashAndBankId(@Param("accountNumberHash") String accountNumberHash, @Param("bankId") Long bankId);

    /**
     * Check if account number hash exists for a specific bank for another payee (for uniqueness validation during update)
     */
    @Query("SELECT CASE WHEN COUNT(pd) > 0 THEN true ELSE false END FROM PayeeDetails pd WHERE pd.accountNumberHash = :accountNumberHash AND pd.bank.id = :bankId AND pd.id <> :id")
    boolean existsByAccountNumberHashAndBankIdAndIdNot(@Param("accountNumberHash") String accountNumberHash, @Param("bankId") Long bankId, @Param("id") Long id);

    /**
     * Count payee details by bank (for dependency protection when deleting banks)
     */
    long countByBankId(Long bankId);

    /**
     * Find first few payee details by bank (for showing in error messages)
     */
    @Query("SELECT pd FROM PayeeDetails pd WHERE pd.bank.id = :bankId ORDER BY pd.payeeName ASC")
    Page<PayeeDetails> findByBankId(@Param("bankId") Long bankId, Pageable pageable);

    /**
     * Bulk upload methods
     */
    @Query("SELECT pd FROM PayeeDetails pd LEFT JOIN FETCH pd.bank ORDER BY pd.payeeName ASC")
    List<PayeeDetails> findAllForExport();

    /**
     * Find payee details by payee name (case-insensitive) - for bulk upload
     */
    @Query("SELECT pd FROM PayeeDetails pd WHERE LOWER(pd.payeeName) = LOWER(:payeeName)")
    java.util.Optional<PayeeDetails> findByPayeeNameIgnoreCase(@Param("payeeName") String payeeName);

    /**
     * Check if payee name exists (case-insensitive) - for bulk upload validation
     */
    @Query("SELECT CASE WHEN COUNT(pd) > 0 THEN true ELSE false END FROM PayeeDetails pd WHERE LOWER(pd.payeeName) = LOWER(:payeeName)")
    boolean existsByPayeeNameIgnoreCase(@Param("payeeName") String payeeName);
}
