package com.eps.module.auth.rbac.registry;

import com.eps.module.auth.rbac.dto.PermissionDefinition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Permission registry that defines all entity scopes and auto-generates permissions.
 * Auto-generates 6 basic permissions (READ, CREATE, UPDATE, DELETE, BULK_UPLOAD, EXPORT) for all entity scopes.
 * 
 * NOTE: Core Masters (STATE, CITY, BANK, etc.) are ADMIN-ONLY and don't have individual permissions.
 * They are protected by @RequireAdmin annotation and only users with ALL permission can access them.
 * 
 * Custom permissions can be added manually as needed.
 */
@Component
public class PermissionRegistry {

    // Basic actions auto-generated for every scope
    private static final String[] BASIC_ACTIONS = {
            "READ", "CREATE", "UPDATE", "DELETE", "BULK_UPLOAD", "EXPORT"
    };

    // Core Master Scopes - ADMIN ONLY (no individual permissions generated)
    // These are managed exclusively by users with ALL permission
    // Kept here for reference and getCategoryForScope() method
    private static final String[] CORE_MASTER_SCOPES = {
            "STATE", "CITY", "LOCATION", "WAREHOUSE", "DATACENTER",
            "ASSET_TYPE", "ASSET_CATEGORY", "MOVEMENT_TYPE", "ASSET_TAG_GENERATOR",
            "SITE_TYPE", "SITE_CATEGORY", "SITE_CODE_GENERATOR",
            "ACTIVITY",
            "BANK",
            "COST_CATEGORY", "COST_TYPE", "COST_ITEM",
            "PAYMENT_METHOD", "PAYEE_TYPE",
            "VENDOR_CATEGORY", "VENDOR_TYPE",
            "PERSON_TYPE",
            "STATUS_TYPE"
    };

    // Operational Scopes
    private static final String[] OPERATIONAL_SCOPES = {
            "ASSET", "SITE",
            "ACTIVITY_LIST", "ACTIVITY_WORK",
            "MANAGED_PROJECT",
            "ASSETS_ON_SITE", "ASSETS_ON_WAREHOUSE", "ASSETS_ON_DATACENTER"
    };

    // Financial Scopes
    private static final String[] FINANCIAL_SCOPES = {
            "INVOICE", "VOUCHER",
            "EXPENDITURE_INVOICE", "EXPENDITURE_VOUCHER",
            "ASSET_EXPENDITURE", "SITE_ACTIVITY_WORK_EXPENDITURE",
            "PAYMENT", "PAYEE_DETAILS", "PAYEE"
    };

    // People & Organizations Scopes
    private static final String[] PEOPLE_ORG_SCOPES = {
            "VENDOR", "LANDLORD", "PERSON_DETAILS"
    };

    // System Scopes
    private static final String[] SYSTEM_SCOPES = {
            "USER", "ROLE"
    };

    /**
     * Get all permissions including auto-generated basic permissions and custom permissions.
     * NOTE: Core Masters are excluded - they are admin-only (@RequireAdmin).
     */
    public List<PermissionDefinition> getAllPermissions() {
        List<PermissionDefinition> permissions = new ArrayList<>();

        // Add super admin permission
        permissions.add(PermissionDefinition.builder()
                .name("ALL")
                .description("Complete system access - Super Administrator (includes Core Masters)")
                .scope("SYSTEM")
                .action("ALL")
                .category("System Administration")
                .build());

        // NOTE: Core Masters are NOT included - they are admin-only
        // Users need ALL permission to manage Core Masters

        // Auto-generate basic 6 for Operational
        generateBasicPermissions(permissions, OPERATIONAL_SCOPES, "Operations");

        // Auto-generate basic 6 for Financial
        generateBasicPermissions(permissions, FINANCIAL_SCOPES, "Financial");

        // Auto-generate basic 6 for People & Organizations
        generateBasicPermissions(permissions, PEOPLE_ORG_SCOPES, "People & Organizations");

        // Auto-generate basic 6 for System
        generateBasicPermissions(permissions, SYSTEM_SCOPES, "System Administration");

        // Add custom permissions
        permissions.addAll(getCustomPermissions());

        return permissions;
    }

    /**
     * Get all scopes that have assignable permissions.
     * NOTE: Core Master scopes are excluded - they are admin-only.
     */
    public List<String> getAllScopes() {
        List<String> allScopes = new ArrayList<>();
        // Core Masters excluded - admin only
        allScopes.addAll(List.of(OPERATIONAL_SCOPES));
        allScopes.addAll(List.of(FINANCIAL_SCOPES));
        allScopes.addAll(List.of(PEOPLE_ORG_SCOPES));
        allScopes.addAll(List.of(SYSTEM_SCOPES));
        return allScopes;
    }

    /**
     * Check if a scope is a Core Master (admin-only)
     */
    public boolean isCoreMasterScope(String scope) {
        for (String s : CORE_MASTER_SCOPES) {
            if (s.equals(scope)) return true;
        }
        return false;
    }

    /**
     * Get category for a given scope
     */
    public String getCategoryForScope(String scope) {
        for (String s : CORE_MASTER_SCOPES) {
            if (s.equals(scope)) return "Core Masters";
        }
        for (String s : OPERATIONAL_SCOPES) {
            if (s.equals(scope)) return "Operations";
        }
        for (String s : FINANCIAL_SCOPES) {
            if (s.equals(scope)) return "Financial";
        }
        for (String s : PEOPLE_ORG_SCOPES) {
            if (s.equals(scope)) return "People & Organizations";
        }
        for (String s : SYSTEM_SCOPES) {
            if (s.equals(scope)) return "System Administration";
        }
        return "Other";
    }

    /**
     * Generate 6 basic permissions for each scope in the category
     * (READ, CREATE, UPDATE, DELETE, BULK_UPLOAD, EXPORT)
     */
    private void generateBasicPermissions(List<PermissionDefinition> permissions, String[] scopes, String category) {
        for (String scope : scopes) {
            for (String action : BASIC_ACTIONS) {
                permissions.add(PermissionDefinition.builder()
                        .name(scope + ":" + action)
                        .description("Can " + action.toLowerCase().replace("_", " ") + " " + scope.toLowerCase().replace("_", " "))
                        .scope(scope)
                        .action(action)
                        .category(category)
                        .build());
            }
        }
    }

    /**
     * Custom permissions beyond the 6 basic actions
     */
    private List<PermissionDefinition> getCustomPermissions() {
        List<PermissionDefinition> customPermissions = new ArrayList<>();

        // Asset custom permissions
        customPermissions.add(PermissionDefinition.builder()
                .name("ASSET:PLACE")
                .description("Can place asset on site/warehouse/datacenter")
                .scope("ASSET")
                .action("PLACE")
                .category("Operations")
                .build());

        customPermissions.add(PermissionDefinition.builder()
                .name("ASSET:TRANSFER")
                .description("Can transfer asset between locations")
                .scope("ASSET")
                .action("TRANSFER")
                .category("Operations")
                .build());

        customPermissions.add(PermissionDefinition.builder()
                .name("ASSET:VIEW_MOVEMENT_HISTORY")
                .description("Can view asset movement/placement history")
                .scope("ASSET")
                .action("VIEW_MOVEMENT_HISTORY")
                .category("Operations")
                .build());

        customPermissions.add(PermissionDefinition.builder()
                .name("ASSET:VIEW_CURRENT_LOCATION")
                .description("Can view current asset location")
                .scope("ASSET")
                .action("VIEW_CURRENT_LOCATION")
                .category("Operations")
                .build());

        customPermissions.add(PermissionDefinition.builder()
                .name("ASSET:FINANCIAL_VIEW")
                .description("Can view asset financial details (depreciation, WDV)")
                .scope("ASSET")
                .action("FINANCIAL_VIEW")
                .category("Operations")
                .build());

        customPermissions.add(PermissionDefinition.builder()
                .name("ASSET:FINANCIAL_EXPORT")
                .description("Can export asset financial report")
                .scope("ASSET")
                .action("FINANCIAL_EXPORT")
                .category("Operations")
                .build());

        // Activity Work custom permission
        customPermissions.add(PermissionDefinition.builder()
                .name("ACTIVITY_WORK:ASSIGN")
                .description("Can assign activity work to personnel")
                .scope("ACTIVITY_WORK")
                .action("ASSIGN")
                .category("Operations")
                .build());

        return customPermissions;
    }
}
