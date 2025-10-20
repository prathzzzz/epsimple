package com.eps.module.activity;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_work_remarks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityWorkRemarks extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "activity_work_id", nullable = false)
        private ActivityWork activityWork;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "commented_on", nullable = false)
    private LocalDateTime commentedOn;

    @Column(name = "commented_by")
    private Long commentedBy;
}
