package com.eps.module.api.epsone.asset_expenditure_and_activity_work.constant;

public class AssetExpenditureAndActivityWorkErrorMessages {
    public static final String ASSET_NOT_FOUND_ID = "Asset not found with id: ";
    public static final String EXPENDITURES_INVOICE_NOT_FOUND_ID = "Expenditures invoice not found with id: ";
    public static final String ACTIVITY_WORK_NOT_FOUND_ID = "Activity work not found with id: ";
    public static final String DUPLICATE_COMBINATION = "This asset-expenditure-activity work combination already exists";
    public static final String NOT_FOUND_AFTER_SAVE = "Asset expenditure and activity work not found after save";
    public static final String NOT_FOUND_ID = "Asset expenditure and activity work not found with id: ";
    public static final String NOT_FOUND_AFTER_UPDATE = "Asset expenditure and activity work not found after update";
    
    // Bulk Upload Messages
    public static final String ASSET_TAG_ID_REQUIRED = "Asset tag ID is required";
    public static final String ASSET_NOT_FOUND_TAG = "Asset '%s' not found";
    public static final String INVOICE_NOT_FOUND = "Invoice '%s' not found";
    public static final String ACTIVITY_WORK_NOT_FOUND_MSG = "Activity Work with ID '%s' not found";
    public static final String ACTIVITY_WORK_ID_INVALID = "Activity Work ID must be a valid number";
    
    private AssetExpenditureAndActivityWorkErrorMessages() {}
}