package com.pramodbindal.localshop.constants;

/**
 * @author Pramod.Bindal
 */

public class ApiConstants {

    private static final String API_HOST = "35.154.136.241";
    private static final int API_PORT = 8080;
    private static final String API_SCHEME = "http";
    private static final String CONTEXT_ROOT = "mobile-khata";
    private static final String API_BASE = API_SCHEME + "://" + API_HOST + ":" + API_PORT + "/" + CONTEXT_ROOT;
    public static final String API_URL_TENANT_REGISTER = API_BASE + "/register/";
    public static final String API_URL_CUSTOMER = API_BASE + "/customer/{tenantId}";

}
