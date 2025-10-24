package com.eps.module.location;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "city")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "City name is required")
    @Size(max = 100, message = "City name cannot exceed 100 characters")
    @Column(name = "city_name", nullable = false, length = 100)
    private String cityName;

    @Size(max = 10, message = "City code cannot exceed 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "City code can only contain letters, numbers, hyphens and underscores")
    @Column(name = "city_code", unique = true, length = 10)
    private String cityCode;

    @NotNull(message = "State is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false)
    private State state;
}
