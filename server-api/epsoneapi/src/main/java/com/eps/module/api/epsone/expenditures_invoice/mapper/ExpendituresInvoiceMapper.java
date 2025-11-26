package com.eps.module.api.epsone.expenditures_invoice.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceRequestDto;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceResponseDto;
import com.eps.module.cost.ExpendituresInvoice;
import com.eps.module.cost.CostItem;
import com.eps.module.payment.Invoice;
import com.eps.module.bank.ManagedProject;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public abstract class ExpendituresInvoiceMapper {

    @Autowired
    private com.eps.module.api.epsone.cost_item.repository.CostItemRepository costItemRepository;

    @Autowired
    private com.eps.module.api.epsone.invoice.repository.InvoiceRepository invoiceRepository;

    @Autowired
    private com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository managedProjectRepository;

    @Autowired
    protected AuditFieldMapper auditFieldMapper;

    @Mapping(target = "costItemId", source = "costItem.id")
    @Mapping(target = "costItemFor", source = "costItem.costItemFor")
    @Mapping(target = "costTypeName", source = "costItem.costType.typeName")
    @Mapping(target = "costCategoryName", source = "costItem.costType.costCategory.categoryName")
    @Mapping(target = "invoiceId", source = "invoice.id")
    @Mapping(target = "invoiceNumber", source = "invoice.invoiceNumber")
    @Mapping(target = "invoiceDate", source = "invoice.invoiceDate")
    @Mapping(target = "payeeName", expression = "java(buildPayeeName(expendituresInvoice.getInvoice()))")
    @Mapping(target = "managedProjectId", source = "managedProject.id")
    @Mapping(target = "projectName", source = "managedProject.projectName")
    @Mapping(target = "projectCode", source = "managedProject.projectCode")
    @Mapping(target = "bankName", source = "managedProject.bank.bankName")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    public abstract ExpendituresInvoiceResponseDto toResponseDto(ExpendituresInvoice expendituresInvoice);

    @Mapping(target = "costItem", source = "costItemId", qualifiedByName = "mapCostItem")
    @Mapping(target = "invoice", source = "invoiceId", qualifiedByName = "mapInvoice")
    @Mapping(target = "managedProject", source = "managedProjectId", qualifiedByName = "mapManagedProject")
    public abstract ExpendituresInvoice toEntity(ExpendituresInvoiceRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "costItem", source = "costItemId", qualifiedByName = "mapCostItem")
    @Mapping(target = "invoice", source = "invoiceId", qualifiedByName = "mapInvoice")
    @Mapping(target = "managedProject", source = "managedProjectId", qualifiedByName = "mapManagedProject")
    public abstract void updateEntityFromDto(ExpendituresInvoiceRequestDto requestDto, @MappingTarget ExpendituresInvoice expendituresInvoice);

    @Named("mapCostItem")
    protected CostItem mapCostItem(Long costItemId) {
        if (costItemId == null) {
            return null;
        }
        CostItem costItem = new CostItem();
        costItem.setId(costItemId);
        return costItem;
    }

    @Named("mapInvoice")
    protected Invoice mapInvoice(Long invoiceId) {
        if (invoiceId == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        return invoice;
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

    protected String buildPayeeName(Invoice invoice) {
        if (invoice == null || invoice.getPayee() == null || invoice.getPayee().getPayeeDetails() == null) {
            return null;
        }
        return invoice.getPayee().getPayeeDetails().getPayeeName();
    }
}
