package com.talanlabs.component.annotation;

import com.talanlabs.component.IComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedFrom {

    Class<? extends IComponent> value();

}
