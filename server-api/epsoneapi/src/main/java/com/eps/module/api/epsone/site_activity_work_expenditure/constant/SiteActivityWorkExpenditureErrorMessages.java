package com.eps.module.api.epsone.site_activity_work_expenditure.constant;

public class SiteActivityWorkExpenditureErrorMessages {
    public static final String EXPENDITURE_NOT_FOUND_ID = "Site activity work expenditure not found with id: ";
    public static final String EXPENDITURE_NOT_FOUND_AFTER_SAVE = "Site activity work expenditure not found after save";
    public static final String EXPENDITURE_NOT_FOUND_AFTER_UPDATE = "Site activity work expenditure not found after update";
    public static final String SITE_NOT_FOUND_ID = "Site not found with id: ";
    public static final String ACTIVITY_WORK_NOT_FOUND_ID = "Activity work not found with id: ";
    public static final String EXPENDITURES_INVOICE_NOT_FOUND_ID = "Expenditures invoice not found with id: ";
    public static final String DUPLICATE_EXPENDITURE = "This site-activity-work-expenditure combination already exists";
    
    // Bulk Upload Validation Messages
    public static final String SITE_CODE_REQUIRED = "Site Code is required";
    public static final String SITE_NOT_FOUND_CODE = "Site with code '%s' not found";
    public static final String ACTIVITY_NAME_REQUIRED = "Activity Name is required";
    public static final String ACTIVITY_NOT_FOUND_NAME = "Activity with name '%s' not found";
    public static final String VENDOR_ORDER_NUMBER_REQUIRED = "Vendor Order Number is required";
    public static final String ACTIVITY_WORK_NOT_FOUND_DETAILS = "Activity Work not found for activity '%s' and vendor order number '%s'";
    public static final String INVOICE_NUMBER_REQUIRED = "Invoice Number is required";
    public static final String EXPENDITURES_INVOICE_NOT_FOUND_NUMBER = "Expenditures Invoice with number '%s' not found";
    public static final String AMOUNT_REQUIRED = "Amount is required";
    public static final String AMOUNT_MUST_BE_POSITIVE = "Amount must be greater than 0";
    public static final String REMARKS_REQUIRED = "Remarks is required";

    private SiteActivityWorkExpenditureErrorMessages() {}
}
