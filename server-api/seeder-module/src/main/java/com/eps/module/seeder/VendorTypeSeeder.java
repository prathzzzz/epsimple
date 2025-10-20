package com.eps.module.seeder;

import com.eps.module.seeder.repository.vendor.VendorTypeSeederRepository;
import com.eps.module.common.seeder.base.AbstractSeeder;
import com.eps.module.vendor.VendorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(103)
@RequiredArgsConstructor
@Slf4j
public class VendorTypeSeeder extends AbstractSeeder {

    private final VendorTypeSeederRepository vendorTypeRepository;

    @Override
    public String getSeederName() {
        return "VendorType";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return vendorTypeRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        List<VendorType> vendorTypes = Arrays.asList(
                VendorType.builder()
                        .typeName("TIS")
                        .description("Vendors who construct and set up the site for functioning, including building infrastructure, foundations, and structural work")
                        .build(),

                VendorType.builder()
                        .typeName("UPS")
                        .description("Vendors providing uninterruptible power supply systems to ensure continuous power during outages, protecting sensitive equipment")
                        .build(),

                VendorType.builder()
                        .typeName("VSAT")
                        .description("Vendors offering satellite-based communication services for internet and data connectivity, especially in remote or difficult-to-wire locations")
                        .build(),

                VendorType.builder()
                        .typeName("AC")
                        .description("Vendors supplying and maintaining air conditioning and ventilation systems to ensure a comfortable environment")
                        .build(),

                VendorType.builder()
                        .typeName("E-Surveillance")
                        .description("Vendors providing electronic surveillance equipment and services including CCTV cameras, alarms, and monitoring systems for site security")
                        .build(),

                VendorType.builder()
                        .typeName("Sign Board")
                        .description("Vendors responsible for producing and installing signage and boards for branding and wayfinding on the site")
                        .build(),

                VendorType.builder()
                        .typeName("Branding")
                        .description("Vendors specializing in site branding, including logos, banners, and promotional displays to represent the site's identity")
                        .build(),

                VendorType.builder()
                        .typeName("ATM")
                        .description("Vendors supplying and servicing Automated Teller Machines, including installation, maintenance, and upgrades")
                        .build()
        );

        vendorTypeRepository.saveAll(vendorTypes);
        log.info("Seeded {} vendor types", vendorTypes.size());
    }
}
