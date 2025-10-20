package com.eps.module.person;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "person_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Person type name is required")
    @Size(max = 100, message = "Type name cannot exceed 100 characters")
    @Column(name = "type_name", nullable = false, unique = true, length = 100)
    private String typeName;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;
}
