package com.eps.module.bank;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "bank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bank extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Bank name is required")
    @Size(max = 255, message = "Bank name cannot exceed 255 characters")
    @Column(name = "bank_name", nullable = false, unique = true, length = 255)
    private String bankName;

    @Size(max = 10, message = "RBI bank code cannot exceed 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "RBI bank code can only contain letters and numbers")
    @Column(name = "rbi_bank_code", unique = true, length = 10)
    private String rbiBankCode;

    @Size(max = 10, message = "EPS bank code cannot exceed 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "EPS bank code can only contain letters, numbers, hyphens and underscores")
    @Column(name = "eps_bank_code", unique = true, length = 10)
    private String epsBankCode;

    @Size(max = 10, message = "Alternate bank code cannot exceed 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Alternate bank code can only contain letters, numbers, hyphens and underscores")
    @Column(name = "bank_code_alt", unique = true, length = 10)
    private String bankCodeAlt;

    @Size(max = 500, message = "Bank logo URL cannot exceed 500 characters")
    @Column(name = "bank_logo", length = 500)
    private String bankLogo;
}
