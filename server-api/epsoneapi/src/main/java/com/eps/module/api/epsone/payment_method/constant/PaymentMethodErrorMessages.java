package com.eps.module.api.epsone.payment_method.constant;

public class PaymentMethodErrorMessages {
    public static final String PAYMENT_METHOD_NOT_FOUND_ID = "Payment method not found with ID: ";
    public static final String PAYMENT_METHOD_EXISTS = "Payment method '%s' already exists";
    
    // Bulk Upload Validation Messages
    public static final String METHOD_NAME_REQUIRED = "Method name is required";
    public static final String METHOD_NAME_TOO_LONG = "Method name cannot exceed 50 characters";
    
    private PaymentMethodErrorMessages() {}
}
