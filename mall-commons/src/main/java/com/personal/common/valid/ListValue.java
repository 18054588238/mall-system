package com.personal.common.valid;


import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/*
* 自定义注解
* */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ListValueConstraintValidator.class})
public @interface ListValue {

    String message() default "提交的数据必须是在数据列表中";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] val() default {};
}
