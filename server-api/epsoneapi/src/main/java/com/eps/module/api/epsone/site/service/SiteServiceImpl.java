package com.eps.module.api.epsone.site.service;

import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.site.dto.SiteRequestDto;
import com.eps.module.api.epsone.site.dto.SiteResponseDto;
import com.eps.module.api.epsone.site.mapper.SiteMapper;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.api.epsone.site_category.repository.SiteCategoryRepository;
import com.eps.module.api.epsone.site_type.repository.SiteTypeRepository;
import com.eps.module.bank.ManagedProject;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.location.Location;
import com.eps.module.person.PersonDetails;
import com.eps.module.site.Site;
import com.eps.module.site.SiteCategory;
import com.eps.module.site.SiteType;
import com.eps.module.status.GenericStatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;
    private final ManagedProjectRepository managedProjectRepository;
    private final SiteCategoryRepository siteCategoryRepository;
    private final LocationRepository locationRepository;
    private final SiteTypeRepository siteTypeRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final PersonDetailsRepository personDetailsRepository;
    private final SiteMapper siteMapper;

    @Override
    @Transactional
    public SiteResponseDto createSite(SiteRequestDto requestDto) {
        log.info("Creating new site with code: {}", requestDto.getSiteCode());

        // Check if site code already exists
        if (siteRepository.existsBySiteCode(requestDto.getSiteCode())) {
            throw new IllegalArgumentException("Site with code '" + requestDto.getSiteCode() + "' already exists");
        }

        // Validate required location exists
        Location location = locationRepository.findById(requestDto.getLocationId())
            .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + requestDto.getLocationId()));

        Site site = siteMapper.toEntity(requestDto);
        site.setLocation(location);

        // Set optional foreign keys
        setOptionalForeignKeys(site, requestDto);

        Site savedSite = siteRepository.save(site);
        log.info("Site created successfully with ID: {}", savedSite.getId());
        return siteMapper.toResponseDto(savedSite);
    }

    @Override
    @Transactional
    public SiteResponseDto updateSite(Long id, SiteRequestDto requestDto) {
        log.info("Updating site with ID: {}", id);

        Site existingSite = siteRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + id));

        // Check if site code is being changed and if it already exists
        if (!existingSite.getSiteCode().equals(requestDto.getSiteCode())) {
            if (siteRepository.existsBySiteCodeAndIdNot(requestDto.getSiteCode(), id)) {
                throw new IllegalArgumentException("Site with code '" + requestDto.getSiteCode() + "' already exists");
            }
        }

        // Validate required location exists
        Location location = locationRepository.findById(requestDto.getLocationId())
            .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + requestDto.getLocationId()));

        siteMapper.updateEntityFromDto(requestDto, existingSite);
        existingSite.setLocation(location);

        // Update optional foreign keys
        setOptionalForeignKeys(existingSite, requestDto);

        Site updatedSite = siteRepository.save(existingSite);
        log.info("Site updated successfully with ID: {}", updatedSite.getId());
        return siteMapper.toResponseDto(updatedSite);
    }

    @Override
    @Transactional
    public void deleteSite(Long id) {
        log.info("Deleting site with ID: {}", id);

        Site site = siteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + id));

        // Check for dependent AssetsOnSite
        long assetsOnSiteCount = siteRepository.countAssetsOnSiteBySiteId(id);
        if (assetsOnSiteCount > 0) {
            String errorMessage = String.format(
                    "Cannot delete site '%s' because it has %d asset%s assigned to it. Please remove or reassign these assets first.",
                    site.getSiteCode(),
                    assetsOnSiteCount,
                    assetsOnSiteCount > 1 ? "s" : ""
            );
            log.warn("Failed to delete site with ID {}: {}", id, errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        // Check for dependent SiteActivityWorkExpenditure
        long siteActivityWorkExpenditureCount = siteRepository.countSiteActivityWorkExpenditureBySiteId(id);
        if (siteActivityWorkExpenditureCount > 0) {
            String errorMessage = String.format(
                    "Cannot delete site '%s' because it has %d activity work expenditure record%s. Please remove these records first.",
                    site.getSiteCode(),
                    siteActivityWorkExpenditureCount,
                    siteActivityWorkExpenditureCount > 1 ? "s" : ""
            );
            log.warn("Failed to delete site with ID {}: {}", id, errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        siteRepository.deleteById(id);
        log.info("Site deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public SiteResponseDto getSiteById(Long id) {
        Site site = siteRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + id));
        return siteMapper.toResponseDto(site);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteResponseDto> getAllSites(Pageable pageable) {
        Page<Site> sites = siteRepository.findAllWithDetails(pageable);
        return sites.map(siteMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteResponseDto> searchSites(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllSites(pageable);
        }
        Page<Site> sites = siteRepository.searchSites(searchTerm.trim(), pageable);
        return sites.map(siteMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteResponseDto> getSiteList() {
        List<Site> sites = siteRepository.findAllSitesList();
        return sites.stream()
                .map(siteMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    private void setOptionalForeignKeys(Site site, SiteRequestDto requestDto) {
        // Set project if provided
        if (requestDto.getProjectId() != null) {
            ManagedProject project = managedProjectRepository.findById(requestDto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Managed Project not found with id: " + requestDto.getProjectId()));
            site.setProject(project);
        } else {
            site.setProject(null);
        }

        // Set site category if provided
        if (requestDto.getSiteCategoryId() != null) {
            SiteCategory siteCategory = siteCategoryRepository.findById(requestDto.getSiteCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Site Category not found with id: " + requestDto.getSiteCategoryId()));
            site.setSiteCategory(siteCategory);
        } else {
            site.setSiteCategory(null);
        }

        // Set site type if provided
        if (requestDto.getSiteTypeId() != null) {
            SiteType siteType = siteTypeRepository.findById(requestDto.getSiteTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Site Type not found with id: " + requestDto.getSiteTypeId()));
            site.setSiteType(siteType);
        } else {
            site.setSiteType(null);
        }

        // Set site status if provided
        if (requestDto.getSiteStatusId() != null) {
            GenericStatusType siteStatus = genericStatusTypeRepository.findById(requestDto.getSiteStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Site Status not found with id: " + requestDto.getSiteStatusId()));
            site.setSiteStatus(siteStatus);
        } else {
            site.setSiteStatus(null);
        }

        // Set contact persons if provided
        setPersonContact(site, requestDto.getChannelManagerContactId(), "channelManager");
        setPersonContact(site, requestDto.getRegionalManagerContactId(), "regionalManager");
        setPersonContact(site, requestDto.getStateHeadContactId(), "stateHead");
        setPersonContact(site, requestDto.getBankPersonContactId(), "bankPerson");
        setPersonContact(site, requestDto.getMasterFranchiseeContactId(), "masterFranchisee");
    }

    private void setPersonContact(Site site, Long personId, String contactType) {
        if (personId != null) {
            PersonDetails person = personDetailsRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person Details not found with id: " + personId));

            switch (contactType) {
                case "channelManager":
                    site.setChannelManagerContact(person);
                    break;
                case "regionalManager":
                    site.setRegionalManagerContact(person);
                    break;
                case "stateHead":
                    site.setStateHeadContact(person);
                    break;
                case "bankPerson":
                    site.setBankPersonContact(person);
                    break;
                case "masterFranchisee":
                    site.setMasterFranchiseeContact(person);
                    break;
            }
        } else {
            // Clear the contact if null
            switch (contactType) {
                case "channelManager":
                    site.setChannelManagerContact(null);
                    break;
                case "regionalManager":
                    site.setRegionalManagerContact(null);
                    break;
                case "stateHead":
                    site.setStateHeadContact(null);
                    break;
                case "bankPerson":
                    site.setBankPersonContact(null);
                    break;
                case "masterFranchisee":
                    site.setMasterFranchiseeContact(null);
                    break;
            }
        }
    }
}
