package com.pramodbindal.localshop;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author Pramod.Bindal
 */

public class RestUtils {

    public static <T> T executePost(String url, Object request, Class<T> responseType, Object... urlArgs) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        restTemplate.getMessageConverters().add(new CustomerMessageConverter());
        restTemplate.getMessageConverters().add(new GenericMessageConverter());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //DO nothing
            }
        });
        return restTemplate.postForObject(url, request, responseType, urlArgs);
    }

}
