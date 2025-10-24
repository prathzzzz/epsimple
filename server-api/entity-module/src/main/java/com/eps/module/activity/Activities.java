package com.eps.module.activity;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activities extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "activity_id", nullable = false)
        private Activity activity;

    @Column(name = "activity_name", nullable = false, length = 100)
    private String activityName;

    @Column(name = "activity_category", length = 100)
    private String activityCategory;

    @Column(name = "activity_description", columnDefinition = "TEXT")
    private String activityDescription;
}
