package com.eps.module.seeder;

import com.eps.module.asset.AssetMovementType;
import com.eps.module.seeder.repository.asset.AssetMovementTypeSeederRepository;
import com.eps.module.common.seeder.base.AbstractSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(109)
@RequiredArgsConstructor
@Slf4j
public class AssetMovementTypeSeeder extends AbstractSeeder {

    private final AssetMovementTypeSeederRepository assetMovementTypeRepository;

    @Override
    public String getSeederName() {
        return "AssetMovementType";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return assetMovementTypeRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        List<AssetMovementType> movementTypes = Arrays.asList(
                AssetMovementType.builder()
                        .movementType("Factory to Site")
                        .description("Asset moved directly from manufacturing facility to operational site location")
                        .build(),

                AssetMovementType.builder()
                        .movementType("Factory to Warehouse")
                        .description("Asset transferred from factory to warehouse storage for inventory management")
                        .build(),

                AssetMovementType.builder()
                        .movementType("Site to Site")
                        .description("Asset relocated between two different site locations")
                        .build(),

                AssetMovementType.builder()
                        .movementType("Site to Warehouse")
                        .description("Asset moved from operational site to warehouse for storage or maintenance")
                        .build(),

                AssetMovementType.builder()
                        .movementType("Warehouse to Site")
                        .description("Asset deployed from warehouse storage to operational site")
                        .build(),

                AssetMovementType.builder()
                        .movementType("Warehouse to Warehouse")
                        .description("Asset transferred between different warehouse locations")
                        .build(),

                AssetMovementType.builder()
                        .movementType("Datacenter to Datacenter")
                        .description("Asset moved between datacenter facilities")
                        .build(),

                AssetMovementType.builder()
                        .movementType("Datacenter to Warehouse")
                        .description("Asset transferred from datacenter to warehouse storage")
                        .build(),

                AssetMovementType.builder()
                        .movementType("Warehouse to Datacenter")
                        .description("Asset moved from warehouse to datacenter facility")
                        .build(),

                AssetMovementType.builder()
                        .movementType("Managed project to Managed Project")
                        .description("Asset transferred between different managed projects")
                        .build()
        );

        assetMovementTypeRepository.saveAll(movementTypes);
        log.info("Seeded {} asset movement types", movementTypes.size());
    }
}
