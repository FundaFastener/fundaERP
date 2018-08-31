package com.company.funda.erp.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(ElementType.FIELD)
@Retention(value=RUNTIME)
public @interface ExcelPointer {
    int[] index() ;
    String formater() default "";
}
