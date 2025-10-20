package com.eps.module.seeder;

import com.eps.module.asset.AssetType;
import com.eps.module.common.seeder.base.AbstractSeeder;
import com.eps.module.seeder.repository.asset.AssetTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Order(105)
@RequiredArgsConstructor
public class AssetTypeSeeder extends AbstractSeeder {

    private final AssetTypeRepository assetTypeRepository;

    @Override
    public String getSeederName() {
        return "AssetType";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return assetTypeRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        List<AssetType> assetTypes = Arrays.asList(
                createAssetType("ATM", "ATM", "Automated Teller Machine"),
                createAssetType("Power Backup", "PWR", "Power Backup Systems"),
                createAssetType("Connectivity", "CON", "Connectivity Equipment"),
                createAssetType("Environmental Control", "ENV", "Environmental Control Systems"),
                createAssetType("Security", "SEC", "Security and Surveillance Equipment"),
                createAssetType("Site Infrastructure", "INF", "Site Infrastructure Components"),
                createAssetType("Network Equipment", "NET", "Network and Communication Devices"),
                createAssetType("Monitoring", "MON", "Monitoring and Display Systems")
        );

        assetTypeRepository.saveAll(assetTypes);
        log.info("Successfully seeded {} asset types", assetTypes.size());
    }

    private AssetType createAssetType(String typeName, String typeCode, String description) {
        AssetType assetType = new AssetType();
        assetType.setTypeName(typeName);
        assetType.setTypeCode(typeCode);
        assetType.setDescription(description);
        return assetType;
    }
}
