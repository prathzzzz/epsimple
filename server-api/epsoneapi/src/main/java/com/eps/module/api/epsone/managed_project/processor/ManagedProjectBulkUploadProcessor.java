package com.eps.module.api.epsone.managed_project.processor;

import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.api.epsone.managed_project.dto.ManagedProjectBulkUploadDto;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.api.epsone.managed_project.validator.ManagedProjectBulkUploadValidator;
import com.eps.module.bank.Bank;
import com.eps.module.bank.ManagedProject;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagedProjectBulkUploadProcessor extends BulkUploadProcessor<ManagedProjectBulkUploadDto, ManagedProject> {

    private final ManagedProjectRepository managedProjectRepository;
    private final BankRepository bankRepository;
    private final ManagedProjectBulkUploadValidator validator;

    @Override
    protected ManagedProjectBulkUploadValidator getValidator() {
        return validator;
    }

    @Override
    protected ManagedProject convertToEntity(ManagedProjectBulkUploadDto dto) {
        log.debug("Converting DTO to ManagedProject entity: {}", dto.getProjectName());

        // Find bank by name
        Bank bank = bankRepository.findByBankNameIgnoreCase(dto.getBankName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bank '" + dto.getBankName() + "' not found"
                ));

        ManagedProject managedProject = new ManagedProject();
        managedProject.setProjectName(dto.getProjectName());
        managedProject.setProjectCode(dto.getProjectCode());
        managedProject.setProjectType(dto.getProjectType());
        managedProject.setProjectDescription(dto.getProjectDescription());
        managedProject.setBank(bank);

        return managedProject;
    }

    @Override
    protected void saveEntity(ManagedProject entity) {
        log.debug("Saving managed project: {}", entity.getProjectName());
        managedProjectRepository.save(entity);
    }

    @Override
    protected boolean isEmptyRow(ManagedProjectBulkUploadDto dto) {
        return (dto.getProjectName() == null || dto.getProjectName().trim().isEmpty()) &&
               (dto.getBankName() == null || dto.getBankName().trim().isEmpty());
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(ManagedProjectBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("projectName", dto.getProjectName());
        rowData.put("projectCode", dto.getProjectCode());
        rowData.put("projectType", dto.getProjectType());
        rowData.put("projectDescription", dto.getProjectDescription());
        rowData.put("bankName", dto.getBankName());
        return rowData;
    }
}
