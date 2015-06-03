package org.team.sdsc.datamodel.annotation;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)


public @interface Edit{
    public String role();
}

