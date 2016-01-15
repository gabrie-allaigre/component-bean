package com.talanlabs.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GenerateDtos.class)
public @interface GenerateDto {

    String name() default "";

    String[] includeFields() default {};

    String[] excludeFields() default {};

    String[] includeExtends() default {};

    String[] excludeExtends() default {};

    String[] classToClasss() default {};

}
