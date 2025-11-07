package com.eps.module.common.bulk.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark fields that should be included in Excel export/import
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    
    /**
     * The column header name
     */
    String value();
    
    /**
     * The order of the column (lower numbers appear first)
     */
    int order() default Integer.MAX_VALUE;
    
    /**
     * Whether this column is required for import
     */
    boolean required() default false;
    
    /**
     * Example value for template generation
     */
    String example() default "";
}
