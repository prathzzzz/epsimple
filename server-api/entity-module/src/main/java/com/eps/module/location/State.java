package com.eps.module.location;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "state")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class State extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "State name is required")
    @Size(max = 100, message = "State name cannot exceed 100 characters")
    @Column(name = "state_name", nullable = false, unique = true, length = 100)
    private String stateName;

    @NotBlank(message = "State code is required")
    @Size(min = 2, max = 10, message = "State code must be between 2 and 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "State code can only contain letters, numbers, hyphens and underscores")
    @Column(name = "state_code", nullable = false, unique = true, length = 10)
    private String stateCode;

    @Size(max = 10, message = "Alternate state code cannot exceed 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Alternate state code can only contain letters, numbers, hyphens and underscores")
    @Column(name = "state_code_alt", length = 10)
    private String stateCodeAlt;
}
