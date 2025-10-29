package com.eps.module.site;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.bank.ManagedProject;
import com.eps.module.location.State;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "site_code_generator",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "state_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteCodeGenerator extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private ManagedProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @Column(name = "max_seq_digit")
    @Builder.Default
    private Integer maxSeqDigit = 5;

    @Column(name = "running_seq")
    @Builder.Default
    private Integer runningSeq = 1;
}
