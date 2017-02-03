package com.pramodbindal.localshop.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

enum ValueType {
    BOOLEAN, NUMBER, TEXT
}


/**
 * @author Pramod.Bindal
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    int minLength() default 0;

    int maxLength() default Integer.MAX_VALUE;

    int length() default -1;

    long minValue() default Integer.MIN_VALUE;

    long maxValue() default Long.MAX_VALUE;

    ValueType valueType() default ValueType.NUMBER;

}


