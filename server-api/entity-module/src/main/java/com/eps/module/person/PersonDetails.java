package com.eps.module.person;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "person_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Person type is required")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_type_id", nullable = false)
    private PersonType personType;

    @Size(max = 100, message = "First name cannot exceed 100 characters")
    @Column(name = "first_name", length = 100)
    private String firstName;

    @Size(max = 100, message = "Middle name cannot exceed 100 characters")
    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    @Column(name = "last_name", length = 100)
    private String lastName;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address format")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Column(unique = true, length = 255)
    private String email;

    @Size(max = 5000, message = "Permanent address cannot exceed 5000 characters")
    @Column(name = "permanent_address", columnDefinition = "TEXT")
    private String permanentAddress;

    @Size(max = 5000, message = "Correspondence address cannot exceed 5000 characters")
    @Column(name = "correspondence_address", columnDefinition = "TEXT")
    private String correspondenceAddress;
}
