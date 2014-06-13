package com.sababado.autoparcel.sample;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate global parameters with this annotation to mark it as a field that should be parceled by an {@link AutoParcel} implementation.
 * This annotation will not function on final or private fields.
 * Created by rjszabo on 6/4/2014.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParcelMe {
}
