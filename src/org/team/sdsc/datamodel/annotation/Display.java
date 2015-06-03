package org.team.sdsc.datamodel.annotation;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface Display{
    public String title();
    public String var() default "";
    public int index();
    public int width();
}

