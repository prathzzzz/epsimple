package com.eps.module.seeder.repository.asset;

import com.eps.module.asset.AssetMovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetMovementTypeSeederRepository extends JpaRepository<AssetMovementType, Long> {
}
