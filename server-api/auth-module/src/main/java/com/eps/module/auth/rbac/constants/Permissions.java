package com.eps.module.auth.rbac.constants;

/**
 * Constants for all permissions in the system.
 * Use these constants instead of hardcoding permission strings to avoid typos.
 */
public class Permissions {

    // ========== SUPER ADMIN ==========
    public static final String ALL = "ALL";

    // ========== CORE MASTERS - STATE ==========
    public static final String STATE_CREATE = "STATE:CREATE";
    public static final String STATE_READ = "STATE:READ";
    public static final String STATE_UPDATE = "STATE:UPDATE";
    public static final String STATE_DELETE = "STATE:DELETE";
    public static final String STATE_BULK_UPLOAD = "STATE:BULK_UPLOAD";
    public static final String STATE_EXPORT = "STATE:EXPORT";

    // ========== CORE MASTERS - CITY ==========
    public static final String CITY_CREATE = "CITY:CREATE";
    public static final String CITY_READ = "CITY:READ";
    public static final String CITY_UPDATE = "CITY:UPDATE";
    public static final String CITY_DELETE = "CITY:DELETE";
    public static final String CITY_BULK_UPLOAD = "CITY:BULK_UPLOAD";
    public static final String CITY_EXPORT = "CITY:EXPORT";

    // ========== CORE MASTERS - LOCATION ==========
    public static final String LOCATION_CREATE = "LOCATION:CREATE";
    public static final String LOCATION_READ = "LOCATION:READ";
    public static final String LOCATION_UPDATE = "LOCATION:UPDATE";
    public static final String LOCATION_DELETE = "LOCATION:DELETE";
    public static final String LOCATION_BULK_UPLOAD = "LOCATION:BULK_UPLOAD";
    public static final String LOCATION_EXPORT = "LOCATION:EXPORT";

    // ========== CORE MASTERS - WAREHOUSE ==========
    public static final String WAREHOUSE_CREATE = "WAREHOUSE:CREATE";
    public static final String WAREHOUSE_READ = "WAREHOUSE:READ";
    public static final String WAREHOUSE_UPDATE = "WAREHOUSE:UPDATE";
    public static final String WAREHOUSE_DELETE = "WAREHOUSE:DELETE";
    public static final String WAREHOUSE_BULK_UPLOAD = "WAREHOUSE:BULK_UPLOAD";
    public static final String WAREHOUSE_EXPORT = "WAREHOUSE:EXPORT";

    // ========== CORE MASTERS - DATACENTER ==========
    public static final String DATACENTER_CREATE = "DATACENTER:CREATE";
    public static final String DATACENTER_READ = "DATACENTER:READ";
    public static final String DATACENTER_UPDATE = "DATACENTER:UPDATE";
    public static final String DATACENTER_DELETE = "DATACENTER:DELETE";
    public static final String DATACENTER_BULK_UPLOAD = "DATACENTER:BULK_UPLOAD";
    public static final String DATACENTER_EXPORT = "DATACENTER:EXPORT";

    // ========== CORE MASTERS - ASSET_TYPE ==========
    public static final String ASSET_TYPE_CREATE = "ASSET_TYPE:CREATE";
    public static final String ASSET_TYPE_READ = "ASSET_TYPE:READ";
    public static final String ASSET_TYPE_UPDATE = "ASSET_TYPE:UPDATE";
    public static final String ASSET_TYPE_DELETE = "ASSET_TYPE:DELETE";
    public static final String ASSET_TYPE_BULK_UPLOAD = "ASSET_TYPE:BULK_UPLOAD";
    public static final String ASSET_TYPE_EXPORT = "ASSET_TYPE:EXPORT";

    // ========== CORE MASTERS - ASSET_CATEGORY ==========
    public static final String ASSET_CATEGORY_CREATE = "ASSET_CATEGORY:CREATE";
    public static final String ASSET_CATEGORY_READ = "ASSET_CATEGORY:READ";
    public static final String ASSET_CATEGORY_UPDATE = "ASSET_CATEGORY:UPDATE";
    public static final String ASSET_CATEGORY_DELETE = "ASSET_CATEGORY:DELETE";
    public static final String ASSET_CATEGORY_BULK_UPLOAD = "ASSET_CATEGORY:BULK_UPLOAD";
    public static final String ASSET_CATEGORY_EXPORT = "ASSET_CATEGORY:EXPORT";

    // ========== CORE MASTERS - MOVEMENT_TYPE ==========
    public static final String MOVEMENT_TYPE_CREATE = "MOVEMENT_TYPE:CREATE";
    public static final String MOVEMENT_TYPE_READ = "MOVEMENT_TYPE:READ";
    public static final String MOVEMENT_TYPE_UPDATE = "MOVEMENT_TYPE:UPDATE";
    public static final String MOVEMENT_TYPE_DELETE = "MOVEMENT_TYPE:DELETE";
    public static final String MOVEMENT_TYPE_BULK_UPLOAD = "MOVEMENT_TYPE:BULK_UPLOAD";
    public static final String MOVEMENT_TYPE_EXPORT = "MOVEMENT_TYPE:EXPORT";

    // ========== CORE MASTERS - ASSET_TAG_GENERATOR ==========
    public static final String ASSET_TAG_GENERATOR_CREATE = "ASSET_TAG_GENERATOR:CREATE";
    public static final String ASSET_TAG_GENERATOR_READ = "ASSET_TAG_GENERATOR:READ";
    public static final String ASSET_TAG_GENERATOR_UPDATE = "ASSET_TAG_GENERATOR:UPDATE";
    public static final String ASSET_TAG_GENERATOR_DELETE = "ASSET_TAG_GENERATOR:DELETE";
    public static final String ASSET_TAG_GENERATOR_BULK_UPLOAD = "ASSET_TAG_GENERATOR:BULK_UPLOAD";
    public static final String ASSET_TAG_GENERATOR_EXPORT = "ASSET_TAG_GENERATOR:EXPORT";

    // ========== CORE MASTERS - SITE_TYPE ==========
    public static final String SITE_TYPE_CREATE = "SITE_TYPE:CREATE";
    public static final String SITE_TYPE_READ = "SITE_TYPE:READ";
    public static final String SITE_TYPE_UPDATE = "SITE_TYPE:UPDATE";
    public static final String SITE_TYPE_DELETE = "SITE_TYPE:DELETE";
    public static final String SITE_TYPE_BULK_UPLOAD = "SITE_TYPE:BULK_UPLOAD";
    public static final String SITE_TYPE_EXPORT = "SITE_TYPE:EXPORT";

    // ========== CORE MASTERS - SITE_CATEGORY ==========
    public static final String SITE_CATEGORY_CREATE = "SITE_CATEGORY:CREATE";
    public static final String SITE_CATEGORY_READ = "SITE_CATEGORY:READ";
    public static final String SITE_CATEGORY_UPDATE = "SITE_CATEGORY:UPDATE";
    public static final String SITE_CATEGORY_DELETE = "SITE_CATEGORY:DELETE";
    public static final String SITE_CATEGORY_BULK_UPLOAD = "SITE_CATEGORY:BULK_UPLOAD";
    public static final String SITE_CATEGORY_EXPORT = "SITE_CATEGORY:EXPORT";

    // ========== CORE MASTERS - SITE_CODE_GENERATOR ==========
    public static final String SITE_CODE_GENERATOR_CREATE = "SITE_CODE_GENERATOR:CREATE";
    public static final String SITE_CODE_GENERATOR_READ = "SITE_CODE_GENERATOR:READ";
    public static final String SITE_CODE_GENERATOR_UPDATE = "SITE_CODE_GENERATOR:UPDATE";
    public static final String SITE_CODE_GENERATOR_DELETE = "SITE_CODE_GENERATOR:DELETE";
    public static final String SITE_CODE_GENERATOR_BULK_UPLOAD = "SITE_CODE_GENERATOR:BULK_UPLOAD";
    public static final String SITE_CODE_GENERATOR_EXPORT = "SITE_CODE_GENERATOR:EXPORT";

    // ========== CORE MASTERS - ACTIVITY ==========
    public static final String ACTIVITY_CREATE = "ACTIVITY:CREATE";
    public static final String ACTIVITY_READ = "ACTIVITY:READ";
    public static final String ACTIVITY_UPDATE = "ACTIVITY:UPDATE";
    public static final String ACTIVITY_DELETE = "ACTIVITY:DELETE";
    public static final String ACTIVITY_BULK_UPLOAD = "ACTIVITY:BULK_UPLOAD";
    public static final String ACTIVITY_EXPORT = "ACTIVITY:EXPORT";

    // ========== CORE MASTERS - BANK ==========
    public static final String BANK_CREATE = "BANK:CREATE";
    public static final String BANK_READ = "BANK:READ";
    public static final String BANK_UPDATE = "BANK:UPDATE";
    public static final String BANK_DELETE = "BANK:DELETE";
    public static final String BANK_BULK_UPLOAD = "BANK:BULK_UPLOAD";
    public static final String BANK_EXPORT = "BANK:EXPORT";

    // ========== CORE MASTERS - COST_CATEGORY ==========
    public static final String COST_CATEGORY_CREATE = "COST_CATEGORY:CREATE";
    public static final String COST_CATEGORY_READ = "COST_CATEGORY:READ";
    public static final String COST_CATEGORY_UPDATE = "COST_CATEGORY:UPDATE";
    public static final String COST_CATEGORY_DELETE = "COST_CATEGORY:DELETE";
    public static final String COST_CATEGORY_BULK_UPLOAD = "COST_CATEGORY:BULK_UPLOAD";
    public static final String COST_CATEGORY_EXPORT = "COST_CATEGORY:EXPORT";

    // ========== CORE MASTERS - COST_TYPE ==========
    public static final String COST_TYPE_CREATE = "COST_TYPE:CREATE";
    public static final String COST_TYPE_READ = "COST_TYPE:READ";
    public static final String COST_TYPE_UPDATE = "COST_TYPE:UPDATE";
    public static final String COST_TYPE_DELETE = "COST_TYPE:DELETE";
    public static final String COST_TYPE_BULK_UPLOAD = "COST_TYPE:BULK_UPLOAD";
    public static final String COST_TYPE_EXPORT = "COST_TYPE:EXPORT";

    // ========== CORE MASTERS - COST_ITEM ==========
    public static final String COST_ITEM_CREATE = "COST_ITEM:CREATE";
    public static final String COST_ITEM_READ = "COST_ITEM:READ";
    public static final String COST_ITEM_UPDATE = "COST_ITEM:UPDATE";
    public static final String COST_ITEM_DELETE = "COST_ITEM:DELETE";
    public static final String COST_ITEM_BULK_UPLOAD = "COST_ITEM:BULK_UPLOAD";
    public static final String COST_ITEM_EXPORT = "COST_ITEM:EXPORT";

    // ========== CORE MASTERS - PAYMENT_METHOD ==========
    public static final String PAYMENT_METHOD_CREATE = "PAYMENT_METHOD:CREATE";
    public static final String PAYMENT_METHOD_READ = "PAYMENT_METHOD:READ";
    public static final String PAYMENT_METHOD_UPDATE = "PAYMENT_METHOD:UPDATE";
    public static final String PAYMENT_METHOD_DELETE = "PAYMENT_METHOD:DELETE";
    public static final String PAYMENT_METHOD_BULK_UPLOAD = "PAYMENT_METHOD:BULK_UPLOAD";
    public static final String PAYMENT_METHOD_EXPORT = "PAYMENT_METHOD:EXPORT";

    // ========== CORE MASTERS - PAYEE_TYPE ==========
    public static final String PAYEE_TYPE_CREATE = "PAYEE_TYPE:CREATE";
    public static final String PAYEE_TYPE_READ = "PAYEE_TYPE:READ";
    public static final String PAYEE_TYPE_UPDATE = "PAYEE_TYPE:UPDATE";
    public static final String PAYEE_TYPE_DELETE = "PAYEE_TYPE:DELETE";
    public static final String PAYEE_TYPE_BULK_UPLOAD = "PAYEE_TYPE:BULK_UPLOAD";
    public static final String PAYEE_TYPE_EXPORT = "PAYEE_TYPE:EXPORT";

    // ========== CORE MASTERS - VENDOR_CATEGORY ==========
    public static final String VENDOR_CATEGORY_CREATE = "VENDOR_CATEGORY:CREATE";
    public static final String VENDOR_CATEGORY_READ = "VENDOR_CATEGORY:READ";
    public static final String VENDOR_CATEGORY_UPDATE = "VENDOR_CATEGORY:UPDATE";
    public static final String VENDOR_CATEGORY_DELETE = "VENDOR_CATEGORY:DELETE";
    public static final String VENDOR_CATEGORY_BULK_UPLOAD = "VENDOR_CATEGORY:BULK_UPLOAD";
    public static final String VENDOR_CATEGORY_EXPORT = "VENDOR_CATEGORY:EXPORT";

    // ========== CORE MASTERS - VENDOR_TYPE ==========
    public static final String VENDOR_TYPE_CREATE = "VENDOR_TYPE:CREATE";
    public static final String VENDOR_TYPE_READ = "VENDOR_TYPE:READ";
    public static final String VENDOR_TYPE_UPDATE = "VENDOR_TYPE:UPDATE";
    public static final String VENDOR_TYPE_DELETE = "VENDOR_TYPE:DELETE";
    public static final String VENDOR_TYPE_BULK_UPLOAD = "VENDOR_TYPE:BULK_UPLOAD";
    public static final String VENDOR_TYPE_EXPORT = "VENDOR_TYPE:EXPORT";

    // ========== CORE MASTERS - PERSON_TYPE ==========
    public static final String PERSON_TYPE_CREATE = "PERSON_TYPE:CREATE";
    public static final String PERSON_TYPE_READ = "PERSON_TYPE:READ";
    public static final String PERSON_TYPE_UPDATE = "PERSON_TYPE:UPDATE";
    public static final String PERSON_TYPE_DELETE = "PERSON_TYPE:DELETE";
    public static final String PERSON_TYPE_BULK_UPLOAD = "PERSON_TYPE:BULK_UPLOAD";
    public static final String PERSON_TYPE_EXPORT = "PERSON_TYPE:EXPORT";

    // ========== CORE MASTERS - STATUS_TYPE ==========
    public static final String STATUS_TYPE_CREATE = "STATUS_TYPE:CREATE";
    public static final String STATUS_TYPE_READ = "STATUS_TYPE:READ";
    public static final String STATUS_TYPE_UPDATE = "STATUS_TYPE:UPDATE";
    public static final String STATUS_TYPE_DELETE = "STATUS_TYPE:DELETE";
    public static final String STATUS_TYPE_BULK_UPLOAD = "STATUS_TYPE:BULK_UPLOAD";
    public static final String STATUS_TYPE_EXPORT = "STATUS_TYPE:EXPORT";

    // ========== OPERATIONS - ASSET ==========
    public static final String ASSET_CREATE = "ASSET:CREATE";
    public static final String ASSET_READ = "ASSET:READ";
    public static final String ASSET_UPDATE = "ASSET:UPDATE";
    public static final String ASSET_DELETE = "ASSET:DELETE";
    public static final String ASSET_BULK_UPLOAD = "ASSET:BULK_UPLOAD";
    public static final String ASSET_EXPORT = "ASSET:EXPORT";
    // Custom asset permissions
    public static final String ASSET_PLACE = "ASSET:PLACE";
    public static final String ASSET_TRANSFER = "ASSET:TRANSFER";
    public static final String ASSET_VIEW_MOVEMENT_HISTORY = "ASSET:VIEW_MOVEMENT_HISTORY";
    public static final String ASSET_VIEW_CURRENT_LOCATION = "ASSET:VIEW_CURRENT_LOCATION";

    // ========== OPERATIONS - SITE ==========
    public static final String SITE_CREATE = "SITE:CREATE";
    public static final String SITE_READ = "SITE:READ";
    public static final String SITE_UPDATE = "SITE:UPDATE";
    public static final String SITE_DELETE = "SITE:DELETE";
    public static final String SITE_BULK_UPLOAD = "SITE:BULK_UPLOAD";
    public static final String SITE_EXPORT = "SITE:EXPORT";

    // ========== OPERATIONS - ACTIVITY_LIST ==========
    public static final String ACTIVITY_LIST_CREATE = "ACTIVITY_LIST:CREATE";
    public static final String ACTIVITY_LIST_READ = "ACTIVITY_LIST:READ";
    public static final String ACTIVITY_LIST_UPDATE = "ACTIVITY_LIST:UPDATE";
    public static final String ACTIVITY_LIST_DELETE = "ACTIVITY_LIST:DELETE";
    public static final String ACTIVITY_LIST_BULK_UPLOAD = "ACTIVITY_LIST:BULK_UPLOAD";
    public static final String ACTIVITY_LIST_EXPORT = "ACTIVITY_LIST:EXPORT";

    // ========== OPERATIONS - ACTIVITY_WORK ==========
    public static final String ACTIVITY_WORK_CREATE = "ACTIVITY_WORK:CREATE";
    public static final String ACTIVITY_WORK_READ = "ACTIVITY_WORK:READ";
    public static final String ACTIVITY_WORK_UPDATE = "ACTIVITY_WORK:UPDATE";
    public static final String ACTIVITY_WORK_DELETE = "ACTIVITY_WORK:DELETE";
    public static final String ACTIVITY_WORK_BULK_UPLOAD = "ACTIVITY_WORK:BULK_UPLOAD";
    public static final String ACTIVITY_WORK_EXPORT = "ACTIVITY_WORK:EXPORT";
    // Custom activity work permission
    public static final String ACTIVITY_WORK_ASSIGN = "ACTIVITY_WORK:ASSIGN";

    // ========== OPERATIONS - MANAGED_PROJECT ==========
    public static final String MANAGED_PROJECT_CREATE = "MANAGED_PROJECT:CREATE";
    public static final String MANAGED_PROJECT_READ = "MANAGED_PROJECT:READ";
    public static final String MANAGED_PROJECT_UPDATE = "MANAGED_PROJECT:UPDATE";
    public static final String MANAGED_PROJECT_DELETE = "MANAGED_PROJECT:DELETE";
    public static final String MANAGED_PROJECT_BULK_UPLOAD = "MANAGED_PROJECT:BULK_UPLOAD";
    public static final String MANAGED_PROJECT_EXPORT = "MANAGED_PROJECT:EXPORT";

    // ========== OPERATIONS - ASSETS_ON_SITE ==========
    public static final String ASSETS_ON_SITE_CREATE = "ASSETS_ON_SITE:CREATE";
    public static final String ASSETS_ON_SITE_READ = "ASSETS_ON_SITE:READ";
    public static final String ASSETS_ON_SITE_UPDATE = "ASSETS_ON_SITE:UPDATE";
    public static final String ASSETS_ON_SITE_DELETE = "ASSETS_ON_SITE:DELETE";
    public static final String ASSETS_ON_SITE_BULK_UPLOAD = "ASSETS_ON_SITE:BULK_UPLOAD";
    public static final String ASSETS_ON_SITE_EXPORT = "ASSETS_ON_SITE:EXPORT";

    // ========== OPERATIONS - ASSETS_ON_WAREHOUSE ==========
    public static final String ASSETS_ON_WAREHOUSE_CREATE = "ASSETS_ON_WAREHOUSE:CREATE";
    public static final String ASSETS_ON_WAREHOUSE_READ = "ASSETS_ON_WAREHOUSE:READ";
    public static final String ASSETS_ON_WAREHOUSE_UPDATE = "ASSETS_ON_WAREHOUSE:UPDATE";
    public static final String ASSETS_ON_WAREHOUSE_DELETE = "ASSETS_ON_WAREHOUSE:DELETE";
    public static final String ASSETS_ON_WAREHOUSE_BULK_UPLOAD = "ASSETS_ON_WAREHOUSE:BULK_UPLOAD";
    public static final String ASSETS_ON_WAREHOUSE_EXPORT = "ASSETS_ON_WAREHOUSE:EXPORT";

    // ========== OPERATIONS - ASSETS_ON_DATACENTER ==========
    public static final String ASSETS_ON_DATACENTER_CREATE = "ASSETS_ON_DATACENTER:CREATE";
    public static final String ASSETS_ON_DATACENTER_READ = "ASSETS_ON_DATACENTER:READ";
    public static final String ASSETS_ON_DATACENTER_UPDATE = "ASSETS_ON_DATACENTER:UPDATE";
    public static final String ASSETS_ON_DATACENTER_DELETE = "ASSETS_ON_DATACENTER:DELETE";
    public static final String ASSETS_ON_DATACENTER_BULK_UPLOAD = "ASSETS_ON_DATACENTER:BULK_UPLOAD";
    public static final String ASSETS_ON_DATACENTER_EXPORT = "ASSETS_ON_DATACENTER:EXPORT";

    // ========== FINANCIAL - INVOICE ==========
    public static final String INVOICE_CREATE = "INVOICE:CREATE";
    public static final String INVOICE_READ = "INVOICE:READ";
    public static final String INVOICE_UPDATE = "INVOICE:UPDATE";
    public static final String INVOICE_DELETE = "INVOICE:DELETE";
    public static final String INVOICE_BULK_UPLOAD = "INVOICE:BULK_UPLOAD";
    public static final String INVOICE_EXPORT = "INVOICE:EXPORT";

    // ========== FINANCIAL - VOUCHER ==========
    public static final String VOUCHER_CREATE = "VOUCHER:CREATE";
    public static final String VOUCHER_READ = "VOUCHER:READ";
    public static final String VOUCHER_UPDATE = "VOUCHER:UPDATE";
    public static final String VOUCHER_DELETE = "VOUCHER:DELETE";
    public static final String VOUCHER_BULK_UPLOAD = "VOUCHER:BULK_UPLOAD";
    public static final String VOUCHER_EXPORT = "VOUCHER:EXPORT";

    // ========== FINANCIAL - EXPENDITURE_INVOICE ==========
    public static final String EXPENDITURE_INVOICE_CREATE = "EXPENDITURE_INVOICE:CREATE";
    public static final String EXPENDITURE_INVOICE_READ = "EXPENDITURE_INVOICE:READ";
    public static final String EXPENDITURE_INVOICE_UPDATE = "EXPENDITURE_INVOICE:UPDATE";
    public static final String EXPENDITURE_INVOICE_DELETE = "EXPENDITURE_INVOICE:DELETE";
    public static final String EXPENDITURE_INVOICE_BULK_UPLOAD = "EXPENDITURE_INVOICE:BULK_UPLOAD";
    public static final String EXPENDITURE_INVOICE_EXPORT = "EXPENDITURE_INVOICE:EXPORT";

    // ========== FINANCIAL - EXPENDITURE_VOUCHER ==========
    public static final String EXPENDITURE_VOUCHER_CREATE = "EXPENDITURE_VOUCHER:CREATE";
    public static final String EXPENDITURE_VOUCHER_READ = "EXPENDITURE_VOUCHER:READ";
    public static final String EXPENDITURE_VOUCHER_UPDATE = "EXPENDITURE_VOUCHER:UPDATE";
    public static final String EXPENDITURE_VOUCHER_DELETE = "EXPENDITURE_VOUCHER:DELETE";
    public static final String EXPENDITURE_VOUCHER_BULK_UPLOAD = "EXPENDITURE_VOUCHER:BULK_UPLOAD";
    public static final String EXPENDITURE_VOUCHER_EXPORT = "EXPENDITURE_VOUCHER:EXPORT";

    // ========== FINANCIAL - ASSET_EXPENDITURE ==========
    public static final String ASSET_EXPENDITURE_CREATE = "ASSET_EXPENDITURE:CREATE";
    public static final String ASSET_EXPENDITURE_READ = "ASSET_EXPENDITURE:READ";
    public static final String ASSET_EXPENDITURE_UPDATE = "ASSET_EXPENDITURE:UPDATE";
    public static final String ASSET_EXPENDITURE_DELETE = "ASSET_EXPENDITURE:DELETE";
    public static final String ASSET_EXPENDITURE_BULK_UPLOAD = "ASSET_EXPENDITURE:BULK_UPLOAD";
    public static final String ASSET_EXPENDITURE_EXPORT = "ASSET_EXPENDITURE:EXPORT";

    // ========== FINANCIAL - SITE_ACTIVITY_WORK_EXPENDITURE ==========
    public static final String SITE_ACTIVITY_WORK_EXPENDITURE_CREATE = "SITE_ACTIVITY_WORK_EXPENDITURE:CREATE";
    public static final String SITE_ACTIVITY_WORK_EXPENDITURE_READ = "SITE_ACTIVITY_WORK_EXPENDITURE:READ";
    public static final String SITE_ACTIVITY_WORK_EXPENDITURE_UPDATE = "SITE_ACTIVITY_WORK_EXPENDITURE:UPDATE";
    public static final String SITE_ACTIVITY_WORK_EXPENDITURE_DELETE = "SITE_ACTIVITY_WORK_EXPENDITURE:DELETE";
    public static final String SITE_ACTIVITY_WORK_EXPENDITURE_BULK_UPLOAD = "SITE_ACTIVITY_WORK_EXPENDITURE:BULK_UPLOAD";
    public static final String SITE_ACTIVITY_WORK_EXPENDITURE_EXPORT = "SITE_ACTIVITY_WORK_EXPENDITURE:EXPORT";

    // ========== FINANCIAL - PAYMENT ==========
    public static final String PAYMENT_CREATE = "PAYMENT:CREATE";
    public static final String PAYMENT_READ = "PAYMENT:READ";
    public static final String PAYMENT_UPDATE = "PAYMENT:UPDATE";
    public static final String PAYMENT_DELETE = "PAYMENT:DELETE";
    public static final String PAYMENT_BULK_UPLOAD = "PAYMENT:BULK_UPLOAD";
    public static final String PAYMENT_EXPORT = "PAYMENT:EXPORT";

    // ========== FINANCIAL - PAYEE_DETAILS ==========
    public static final String PAYEE_DETAILS_CREATE = "PAYEE_DETAILS:CREATE";
    public static final String PAYEE_DETAILS_READ = "PAYEE_DETAILS:READ";
    public static final String PAYEE_DETAILS_UPDATE = "PAYEE_DETAILS:UPDATE";
    public static final String PAYEE_DETAILS_DELETE = "PAYEE_DETAILS:DELETE";
    public static final String PAYEE_DETAILS_BULK_UPLOAD = "PAYEE_DETAILS:BULK_UPLOAD";
    public static final String PAYEE_DETAILS_EXPORT = "PAYEE_DETAILS:EXPORT";

    // ========== FINANCIAL - PAYEE ==========
    public static final String PAYEE_CREATE = "PAYEE:CREATE";
    public static final String PAYEE_READ = "PAYEE:READ";
    public static final String PAYEE_UPDATE = "PAYEE:UPDATE";
    public static final String PAYEE_DELETE = "PAYEE:DELETE";
    public static final String PAYEE_BULK_UPLOAD = "PAYEE:BULK_UPLOAD";
    public static final String PAYEE_EXPORT = "PAYEE:EXPORT";

    // ========== PEOPLE & ORGANIZATIONS - VENDOR ==========
    public static final String VENDOR_CREATE = "VENDOR:CREATE";
    public static final String VENDOR_READ = "VENDOR:READ";
    public static final String VENDOR_UPDATE = "VENDOR:UPDATE";
    public static final String VENDOR_DELETE = "VENDOR:DELETE";
    public static final String VENDOR_BULK_UPLOAD = "VENDOR:BULK_UPLOAD";
    public static final String VENDOR_EXPORT = "VENDOR:EXPORT";

    // ========== PEOPLE & ORGANIZATIONS - LANDLORD ==========
    public static final String LANDLORD_CREATE = "LANDLORD:CREATE";
    public static final String LANDLORD_READ = "LANDLORD:READ";
    public static final String LANDLORD_UPDATE = "LANDLORD:UPDATE";
    public static final String LANDLORD_DELETE = "LANDLORD:DELETE";
    public static final String LANDLORD_BULK_UPLOAD = "LANDLORD:BULK_UPLOAD";
    public static final String LANDLORD_EXPORT = "LANDLORD:EXPORT";

    // ========== PEOPLE & ORGANIZATIONS - PERSON_DETAILS ==========
    public static final String PERSON_DETAILS_CREATE = "PERSON_DETAILS:CREATE";
    public static final String PERSON_DETAILS_READ = "PERSON_DETAILS:READ";
    public static final String PERSON_DETAILS_UPDATE = "PERSON_DETAILS:UPDATE";
    public static final String PERSON_DETAILS_DELETE = "PERSON_DETAILS:DELETE";
    public static final String PERSON_DETAILS_BULK_UPLOAD = "PERSON_DETAILS:BULK_UPLOAD";
    public static final String PERSON_DETAILS_EXPORT = "PERSON_DETAILS:EXPORT";

    // ========== SYSTEM ADMINISTRATION - USER ==========
    public static final String USER_CREATE = "USER:CREATE";
    public static final String USER_READ = "USER:READ";
    public static final String USER_UPDATE = "USER:UPDATE";
    public static final String USER_DELETE = "USER:DELETE";
    public static final String USER_BULK_UPLOAD = "USER:BULK_UPLOAD";
    public static final String USER_EXPORT = "USER:EXPORT";

    // ========== SYSTEM ADMINISTRATION - ROLE ==========
    public static final String ROLE_CREATE = "ROLE:CREATE";
    public static final String ROLE_READ = "ROLE:READ";
    public static final String ROLE_UPDATE = "ROLE:UPDATE";
    public static final String ROLE_DELETE = "ROLE:DELETE";
    public static final String ROLE_BULK_UPLOAD = "ROLE:BULK_UPLOAD";
    public static final String ROLE_EXPORT = "ROLE:EXPORT";

    private Permissions() {
        // Private constructor to prevent instantiation
    }
}
