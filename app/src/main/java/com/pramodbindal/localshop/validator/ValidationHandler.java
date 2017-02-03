package com.pramodbindal.localshop.validator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ValidationHandler {

    public static Map<String, String> validate(Object object) {
        Map<String, String> errors = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        System.out.println("Arrays.asList(fields) = " + Arrays.asList(fields));
        for (Field field : fields) {
            String error = null;
            try {
                error = validate(field, object);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (error != null && !error.trim().isEmpty()) {
                errors.put(field.getName(), error);
            }
        }
        return errors;


    }

    private static String validate(Field field, Object object) throws Exception {
        System.out.println("Validating : " + field.getName());
        Validate ann = field.getAnnotation(Validate.class);
        String error = null;
        field.setAccessible(true);

        Object value = field.get(object);
        if (field.isAnnotationPresent(Required.class) && (value == null || value.toString().isEmpty())) {
            error = field.getName() + " can not be left blank ";
        } else if (ann != null) {
            String trimValue = value.toString().trim();
            if (ann.length() > 0 && trimValue.length() != ann.length()) {
                error = field.getName() + " length Should be " + ann.length();
            } else if (Double.valueOf(trimValue) < ann.minValue()) {
                error = field.getName() + " should be greater than or equal to :  " + ann.minValue();
            }
        }

        return error;
    }
}
