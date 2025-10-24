package com.eps.module.site;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.bank.ManagedProject;
import com.eps.module.location.Location;
import com.eps.module.person.PersonDetails;
import com.eps.module.status.GenericStatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "site")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Site extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ManagedProject project;

    @Size(max = 100, message = "Project phase cannot exceed 100 characters")
    @Column(name = "project_phase", length = 100)
    private String projectPhase;

    @NotBlank(message = "Site code is required")
    @Size(min = 5, max = 50, message = "Site code must be between 5 and 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Site code must be uppercase alphanumeric with hyphens/underscores")
    @Column(name = "site_code", nullable = false, unique = true, length = 50)
    private String siteCode;

    @Size(max = 50, message = "Old site code cannot exceed 50 characters")
    @Column(name = "old_site_code", length = 50)
    private String oldSiteCode;

    @Size(max = 50, message = "Previous MSP term ID cannot exceed 50 characters")
    @Column(name = "previous_msp_term_id", length = 50)
    private String previousMspTermId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_category_id")
    private SiteCategory siteCategory;

    @NotNull(message = "Location is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Size(max = 50, message = "Location class cannot exceed 50 characters")
    @Column(name = "location_class", length = 50)
    private String locationClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_type_id")
    private SiteType siteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_status_id")
    private GenericStatusType siteStatus;

    @Column(name = "tech_live_date")
    private LocalDate techLiveDate;

    @Column(name = "cash_live_date")
    private LocalDate cashLiveDate;

    @Column(name = "site_close_date")
    private LocalDate siteCloseDate;

    @Column(name = "possession_date")
    private LocalDate possessionDate;

    @Column(name = "actual_possession_date")
    private LocalDate actualPossessionDate;

    @Size(max = 50, message = "Grouting status cannot exceed 50 characters")
    @Column(name = "grouting_status", length = 50)
    private String groutingStatus;

    @Size(max = 50, message = "IT stabilizer info cannot exceed 50 characters")
    @Column(name = "it_stabilizer", length = 50)
    private String itStabilizer;

    @Size(max = 50, message = "Ramp status cannot exceed 50 characters")
    @Column(name = "ramp_status", length = 50)
    private String rampStatus;

    @Size(max = 50, message = "UPS battery backup capacity cannot exceed 50 characters")
    @Column(name = "ups_battery_backup_capacity", length = 50)
    private String upsBatteryBackupCapacity;

    @Size(max = 50, message = "Connectivity type cannot exceed 50 characters")
    @Column(name = "connectivity_type", length = 50)
    private String connectivityType;

    @Size(max = 50, message = "AC units info cannot exceed 50 characters")
    @Column(name = "ac_units", length = 50)
    private String acUnits;

    @DecimalMin(value = "0.0", message = "Main door glass width cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid width format")
    @Column(name = "main_door_glass_width", precision = 10, scale = 2)
    private BigDecimal mainDoorGlassWidth;

    @DecimalMin(value = "0.0", message = "Fixed glass width cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid width format")
    @Column(name = "fixed_glass_width", precision = 10, scale = 2)
    private BigDecimal fixedGlassWidth;

    @Size(max = 50, message = "Signboard size cannot exceed 50 characters")
    @Column(name = "signboard_size", length = 50)
    private String signboardSize;

    @Size(max = 50, message = "Branding size cannot exceed 50 characters")
    @Column(name = "branding_size", length = 50)
    private String brandingSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_manager_contact_id")
    private PersonDetails channelManagerContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regional_manager_contact_id")
    private PersonDetails regionalManagerContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_head_contact_id")
    private PersonDetails stateHeadContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_person_contact_id")
    private PersonDetails bankPersonContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_franchisee_contact_id")
    private PersonDetails masterFranchiseeContact;

    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", 
             message = "Invalid IPv4 address format")
    @Column(name = "gateway_ip", length = 45)
    private String gatewayIp;

    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", 
             message = "Invalid IPv4 address format")
    @Column(name = "atm_ip", length = 45)
    private String atmIp;

    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", 
             message = "Invalid IPv4 address format")
    @Column(name = "subnet_mask", length = 45)
    private String subnetMask;

    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", 
             message = "Invalid IPv4 address format")
    @Column(name = "nat_ip", length = 45)
    private String natIp;

    @Size(max = 20, message = "TLS port cannot exceed 20 characters")
    @Column(name = "tls_port", length = 20)
    private String tlsPort;

    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", 
             message = "Invalid IPv4 address format")
    @Column(name = "switch_ip", length = 45)
    private String switchIp;

    @Size(max = 255, message = "TLS domain name cannot exceed 255 characters")
    @Column(name = "tls_domain_name", length = 255)
    private String tlsDomainName;

    @Size(max = 100, message = "EJ docket cannot exceed 100 characters")
    @Column(name = "ej_docket", length = 100)
    private String ejDocket;

    @Size(max = 100, message = "TSS docket cannot exceed 100 characters")
    @Column(name = "tss_docket", length = 100)
    private String tssDocket;

    @Size(max = 50, message = "OTC activation status cannot exceed 50 characters")
    @Column(name = "otc_activation_status", length = 50)
    private String otcActivationStatus;

    @Column(name = "otc_activation_date")
    private LocalDate otcActivationDate;

    @Size(max = 255, message = "CRA name cannot exceed 255 characters")
    @Column(name = "cra_name", length = 255)
    private String craName;

    @Size(max = 50, message = "Cassette swap status cannot exceed 50 characters")
    @Column(name = "cassette_swap_status", length = 50)
    private String cassetteSwapStatus;

    @Size(max = 50, message = "Cassette type 1 cannot exceed 50 characters")
    @Column(name = "cassette_type_1", length = 50)
    private String cassetteType1;

    @Size(max = 50, message = "Cassette type 2 cannot exceed 50 characters")
    @Column(name = "cassette_type_2", length = 50)
    private String cassetteType2;

    @Size(max = 50, message = "Cassette type 3 cannot exceed 50 characters")
    @Column(name = "cassette_type_3", length = 50)
    private String cassetteType3;

    @Size(max = 50, message = "Cassette type 4 cannot exceed 50 characters")
    @Column(name = "cassette_type_4", length = 50)
    private String cassetteType4;
}
