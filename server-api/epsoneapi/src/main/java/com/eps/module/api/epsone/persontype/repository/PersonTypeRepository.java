package com.eps.module.api.epsone.persontype.repository;

import com.eps.module.person.PersonType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonTypeRepository extends JpaRepository<PersonType, Long> {
    
    @Query("SELECT pt FROM PersonType pt WHERE " +
           "LOWER(pt.typeName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<PersonType> searchPersonTypes(@Param("search") String search, Pageable pageable);
    
    boolean existsByTypeNameIgnoreCase(String typeName);
    
    Optional<PersonType> findByTypeNameIgnoreCase(String typeName);
    
    @Query("SELECT CASE WHEN COUNT(pt) > 0 THEN true ELSE false END FROM PersonType pt WHERE " +
           "LOWER(pt.typeName) = LOWER(:typeName) AND pt.id <> :id")
    boolean existsByTypeNameAndIdNot(@Param("typeName") String typeName, @Param("id") Long id);
}
