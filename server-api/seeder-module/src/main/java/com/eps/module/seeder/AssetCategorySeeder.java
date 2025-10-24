package com.eps.module.seeder;

import com.eps.module.seeder.repository.asset.AssetCategorySeederRepository;
import com.eps.module.asset.AssetCategory;
import com.eps.module.common.seeder.base.AbstractSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(108)
@RequiredArgsConstructor
@Slf4j
public class AssetCategorySeeder extends AbstractSeeder {

    private final AssetCategorySeederRepository assetCategorySeederRepository;

    @Override
    public String getSeederName() {
        return "AssetCategory";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return assetCategorySeederRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        List<AssetCategory> assetCategories = Arrays.asList(
                AssetCategory.builder()
                        .categoryName("Automated Teller Machine")
                        .categoryCode("ATM")
                        .assetCodeAlt("ATM")
                        .description("Cash Dispensing Machine")
                        .build(),

                AssetCategory.builder()
                        .categoryName("Uninterruptible Power Supply")
                        .categoryCode("UPS")
                        .assetCodeAlt("UPRS")
                        .description("Power Backup System")
                        .build(),

                AssetCategory.builder()
                        .categoryName("Air Conditioner")
                        .categoryCode("AC")
                        .assetCodeAlt("AIR")
                        .description("Ventilation system")
                        .build(),

                AssetCategory.builder()
                        .categoryName("Very Small Aperture Terminal / Virtual Satellite")
                        .categoryCode("VSAT")
                        .assetCodeAlt("VST")
                        .description("A small-sized earth station used in the transmit/receive of data over a satellite communication network")
                        .build(),

                AssetCategory.builder()
                        .categoryName("Total Implementation Service")
                        .categoryCode("TIS")
                        .assetCodeAlt("TIS")
                        .description("")
                        .build(),

                AssetCategory.builder()
                        .categoryName("Signage")
                        .categoryCode("SIGN")
                        .assetCodeAlt("SIG")
                        .description("Branding and Signage equipments")
                        .build(),

                AssetCategory.builder()
                        .categoryName("Network Firewall")
                        .categoryCode("FIREWALL")
                        .assetCodeAlt("FIR")
                        .description("Monitors, filters, and blocks unauthorized traffic between trusted internal networks and untrusted external networks")
                        .build(),

                AssetCategory.builder()
                        .categoryName("Network Router")
                        .categoryCode("ROUTER")
                        .assetCodeAlt("ROU")
                        .description("Connects devices and data between different networks using optimal transport devices in a LAN")
                        .build(),

                AssetCategory.builder()
                        .categoryName("Network Switch")
                        .categoryCode("SWITCH")
                        .assetCodeAlt("SWI")
                        .description("Connects devices within a network, forwarding data based on MAC")
                        .build(),

                AssetCategory.builder()
                        .categoryName("Network Modem")
                        .categoryCode("MODEM")
                        .assetCodeAlt("MOD")
                        .description("Converts digital and analog signals for internet connectivity access")
                        .build(),

                AssetCategory.builder()
                        .categoryName("Visual Merchandising")
                        .categoryCode("VM")
                        .assetCodeAlt("VM")
                        .description("")
                        .build(),

                AssetCategory.builder()
                        .categoryName("E-surveillance")
                        .categoryCode("Eserv")
                        .assetCodeAlt("Eserv")
                        .description("")
                        .build()
        );

        assetCategorySeederRepository.saveAll(assetCategories);
        log.info("Seeded {} asset categories", assetCategories.size());
    }
}
