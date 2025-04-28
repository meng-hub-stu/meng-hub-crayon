package com.crayon.teacher.valid;

import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author Mengdl
 * @date 2025/04/28
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MobileValid {

    boolean required() default true;

    String message() default "手机号格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
