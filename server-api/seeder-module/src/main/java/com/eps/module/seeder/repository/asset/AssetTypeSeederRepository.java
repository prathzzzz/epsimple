package com.eps.module.seeder.repository.asset;

import com.eps.module.asset.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetTypeSeederRepository extends JpaRepository<AssetType, Long> {
}
