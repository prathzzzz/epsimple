package com.eps.module.api.epsone.asset_tag_code.repository;

import com.eps.module.asset.AssetTagCodeGenerator;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetTagCodeGeneratorRepository extends JpaRepository<AssetTagCodeGenerator, Long> {

    @Query("SELECT atg FROM AssetTagCodeGenerator atg " +
           "JOIN FETCH atg.assetCategory ac " +
           "JOIN FETCH atg.vendor v " +
           "JOIN FETCH v.vendorDetails vd " +
           "JOIN FETCH atg.bank b " +
           "WHERE LOWER(ac.categoryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(vd.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(vd.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(vd.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(b.bankName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<AssetTagCodeGenerator> search(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT atg FROM AssetTagCodeGenerator atg " +
           "JOIN FETCH atg.assetCategory " +
           "JOIN FETCH atg.vendor v " +
           "JOIN FETCH v.vendorDetails " +
           "JOIN FETCH atg.bank")
    List<AssetTagCodeGenerator> findAllWithDetails();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT atg FROM AssetTagCodeGenerator atg " +
           "JOIN FETCH atg.vendor v " +
           "JOIN FETCH v.vendorDetails " +
           "WHERE atg.assetCategory.id = :assetCategoryId " +
           "AND atg.vendor.id = :vendorId " +
           "AND atg.bank.id = :bankId")
    Optional<AssetTagCodeGenerator> findByCategoryVendorBankWithLock(
        @Param("assetCategoryId") Long assetCategoryId,
        @Param("vendorId") Long vendorId,
        @Param("bankId") Long bankId
    );

    @Query("SELECT atg FROM AssetTagCodeGenerator atg " +
           "JOIN FETCH atg.assetCategory " +
           "JOIN FETCH atg.vendor " +
           "JOIN FETCH atg.bank " +
           "WHERE atg.assetCategory.id = :assetCategoryId " +
           "AND atg.vendor.id = :vendorId " +
           "AND atg.bank.id = :bankId")
    Optional<AssetTagCodeGenerator> findByCategoryVendorBank(
        @Param("assetCategoryId") Long assetCategoryId,
        @Param("vendorId") Long vendorId,
        @Param("bankId") Long bankId
    );

    boolean existsByAssetCategoryIdAndVendorIdAndBankId(
        Long assetCategoryId,
        Long vendorId,
        Long bankId
    );
}
