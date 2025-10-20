package com.eps.module.seeder;

import com.eps.module.seeder.repository.status.GenericStatusTypeRepository;
import com.eps.module.common.seeder.base.AbstractSeeder;
import com.eps.module.status.GenericStatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(106)
@RequiredArgsConstructor
@Slf4j
public class GenericStatusTypeSeeder extends AbstractSeeder {

    private final GenericStatusTypeRepository genericStatusTypeRepository;

    @Override
    public String getSeederName() {
        return "GenericStatusType";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return genericStatusTypeRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        List<GenericStatusType> statusTypes = Arrays.asList(
                GenericStatusType.builder()
                        .statusName("Required")
                        .statusCode("REQ")
                        .description("The area or environment has been requested but approval is pending")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Approved")
                        .statusCode("APP")
                        .description("Request has been approved for processing")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Ordered")
                        .statusCode("ORD")
                        .description("The area has been ordered from a supplier or vendor")
                        .build(),

                GenericStatusType.builder()
                        .statusName("At Factory")
                        .statusCode("FAC")
                        .description("Manufactured and packed at the factory, ready for shipment")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Shipped")
                        .statusCode("SHPD")
                        .description("The asset is in transit")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Warehouse")
                        .statusCode("WHP")
                        .description("The asset is parked in warehouse")
                        .build(),

                GenericStatusType.builder()
                        .statusName("FMO")
                        .statusCode("FMO")
                        .description("The asset is ready to be installed")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Completed")
                        .statusCode("CMPLTD")
                        .description("The asset has been installed successfully completed")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Rejected")
                        .statusCode("REJ")
                        .description("The asset has been rejected")
                        .build(),

                GenericStatusType.builder()
                        .statusName("In Transit")
                        .statusCode("INT")
                        .description("Currently being transported")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Delivered")
                        .statusCode("DLVD")
                        .description("Shipped has reached at delivery point and received by customer")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Waiting")
                        .statusCode("WAIT")
                        .description("Placed in storage or inactive state, awaiting instructions")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Commissioned")
                        .statusCode("COMM")
                        .description("Fully installed and operational")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Active")
                        .statusCode("ACT")
                        .description("Fully installed and operational")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Vacated")
                        .statusCode("VAC")
                        .description("Area has been removed from a facility or decommissioned")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Under Maintenance")
                        .statusCode("UMNT")
                        .description("Being serviced, repaired or updated")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Hold")
                        .statusCode("HOLD")
                        .description("Area is under account in inventory for a specific or pending transaction")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Repair")
                        .statusCode("REP")
                        .description("Being fixed or undergoing maintenance")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Damaged")
                        .statusCode("DMG")
                        .description("Identified as defective or functional, requiring repair or replacement")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Decommissioned")
                        .statusCode("DCMN")
                        .description("Permanently removed from active use but not yet disposed")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Scrapped")
                        .statusCode("SCRP")
                        .description("Permanently removed and sold or disposed as waste or for parts")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Returned to Vendor")
                        .statusCode("RTVD")
                        .description("Sent back to the supplier or manufacturer (e.g., for warranty claims, defects)")
                        .build(),

                GenericStatusType.builder()
                        .statusName("Datacenter")
                        .statusCode("DCE")
                        .description("Parked in datacenter for processing")
                        .build()
        );

        genericStatusTypeRepository.saveAll(statusTypes);
        log.info("Seeded {} generic status types", statusTypes.size());
    }
}
