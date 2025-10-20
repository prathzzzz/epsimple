package com.eps.module.location;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Location name is required")
    @Size(max = 255, message = "Location name cannot exceed 255 characters")
    @Column(name = "location_name", nullable = false, length = 255)
    private String locationName;

    @Size(max = 5000, message = "Address cannot exceed 5000 characters")
    @Column(columnDefinition = "TEXT")
    private String address;

    @Size(max = 100, message = "District name cannot exceed 100 characters")
    @Column(length = 100)
    private String district;

    @NotNull(message = "City is required")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be exactly 6 digits")
    @Column(length = 10)
    private String pincode;

    @Size(max = 50, message = "Region cannot exceed 50 characters")
    @Column(length = 50)
    private String region;

    @Size(max = 50, message = "Zone cannot exceed 50 characters")
    @Column(length = 50)
    private String zone;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @Digits(integer = 3, fraction = 8, message = "Longitude must have max 3 integer digits and 8 decimal digits")
    @Column(precision = 10, scale = 8)
    private BigDecimal longitude;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @Digits(integer = 2, fraction = 8, message = "Latitude must have max 2 integer digits and 8 decimal digits")
    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;
}
