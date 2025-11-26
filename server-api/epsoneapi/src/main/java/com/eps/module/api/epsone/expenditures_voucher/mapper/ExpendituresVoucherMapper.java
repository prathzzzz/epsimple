package com.eps.module.api.epsone.expenditures_voucher.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.expenditures_voucher.dto.ExpendituresVoucherRequestDto;
import com.eps.module.api.epsone.expenditures_voucher.dto.ExpendituresVoucherResponseDto;
import com.eps.module.cost.ExpendituresVoucher;
import com.eps.module.cost.CostItem;
import com.eps.module.payment.Voucher;
import com.eps.module.bank.ManagedProject;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public abstract class ExpendituresVoucherMapper {

    @Autowired
    private com.eps.module.api.epsone.cost_item.repository.CostItemRepository costItemRepository;

    @Autowired
    private com.eps.module.api.epsone.voucher.repository.VoucherRepository voucherRepository;

    @Autowired
    private com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository managedProjectRepository;

    @Autowired
    protected AuditFieldMapper auditFieldMapper;

    @Mapping(target = "costItemId", source = "costItem.id")
    @Mapping(target = "costItemFor", source = "costItem.costItemFor")
    @Mapping(target = "costTypeName", source = "costItem.costType.typeName")
    @Mapping(target = "costCategoryName", source = "costItem.costType.costCategory.categoryName")
    @Mapping(target = "voucherId", source = "voucher.id")
    @Mapping(target = "voucherNumber", source = "voucher.voucherNumber")
    @Mapping(target = "voucherDate", source = "voucher.voucherDate")
    @Mapping(target = "payeeName", expression = "java(buildPayeeName(expendituresVoucher.getVoucher()))")
    @Mapping(target = "managedProjectId", source = "managedProject.id")
    @Mapping(target = "projectName", source = "managedProject.projectName")
    @Mapping(target = "projectCode", source = "managedProject.projectCode")
    @Mapping(target = "bankName", source = "managedProject.bank.bankName")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    public abstract ExpendituresVoucherResponseDto toResponseDto(ExpendituresVoucher expendituresVoucher);

    @Mapping(target = "costItem", source = "costItemId", qualifiedByName = "mapCostItem")
    @Mapping(target = "voucher", source = "voucherId", qualifiedByName = "mapVoucher")
    @Mapping(target = "managedProject", source = "managedProjectId", qualifiedByName = "mapManagedProject")
    public abstract ExpendituresVoucher toEntity(ExpendituresVoucherRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "costItem", source = "costItemId", qualifiedByName = "mapCostItem")
    @Mapping(target = "voucher", source = "voucherId", qualifiedByName = "mapVoucher")
    @Mapping(target = "managedProject", source = "managedProjectId", qualifiedByName = "mapManagedProject")
    public abstract void updateEntityFromDto(ExpendituresVoucherRequestDto requestDto, @MappingTarget ExpendituresVoucher expendituresVoucher);

    @Named("mapCostItem")
    protected CostItem mapCostItem(Long costItemId) {
        if (costItemId == null) {
            return null;
        }
        CostItem costItem = new CostItem();
        costItem.setId(costItemId);
        return costItem;
    }

    @Named("mapVoucher")
    protected Voucher mapVoucher(Long voucherId) {
        if (voucherId == null) {
            return null;
        }
        Voucher voucher = new Voucher();
        voucher.setId(voucherId);
        return voucher;
    }

    @Named("mapManagedProject")
    protected ManagedProject mapManagedProject(Long managedProjectId) {
        if (managedProjectId == null) {
            return null;
        }
        ManagedProject managedProject = new ManagedProject();
        managedProject.setId(managedProjectId);
        return managedProject;
    }

    protected String buildPayeeName(Voucher voucher) {
        if (voucher == null || voucher.getPayee() == null || voucher.getPayee().getPayeeDetails() == null) {
            return null;
        }
        return voucher.getPayee().getPayeeDetails().getPayeeName();
    }
}
