package com.eps.module.activity;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.status.GenericStatusType;
import com.eps.module.vendor.Vendor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "activity_work")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityWork extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "activities_id", nullable = false)
        private Activities activities;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "vendor_id", nullable = false)
        private Vendor vendor;

    @Column(name = "vendor_order_number", length = 100)
    private String vendorOrderNumber;

    @Column(name = "work_order_date")
    private LocalDate workOrderDate;

    @Column(name = "work_start_date")
    private LocalDate workStartDate;

    @Column(name = "work_completion_date")
    private LocalDate workCompletionDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "status_type_id", nullable = false)
        private GenericStatusType statusType;
}
