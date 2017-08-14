package com.dvc.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dvc on 17/7/2017.
 * Annotation to create the class ViewHolder
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XmlDynamicViewId {
    String id() default "";
}
