package com.pramodbindal.localshop;

import com.pramodbindal.localshop.domain.Customer;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.Arrays;
import java.util.List;


/**
 * @author Pramod.Bindal
 */

public class CustomerMessageConverter extends GsonHttpMessageConverter {
    @Override
    protected boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML);
    }
}
