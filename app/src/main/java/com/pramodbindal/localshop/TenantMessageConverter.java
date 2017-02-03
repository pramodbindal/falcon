package com.pramodbindal.localshop;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Pramod.Bindal
 */

public class TenantMessageConverter extends GsonHttpMessageConverter {
    @Override
    protected boolean supports(Class<?> clazz) {
        return super.supports(clazz);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.APPLICATION_JSON);
    }
}
