package com.pramodbindal.localshop.domain;

/**
 * @author Pramod.Bindal
 */

public class Tenant {

    private final Long id;
    private final String imei;
    private final String mobile;
    private final String name;


    public Tenant(Long id, String imei, String mobile, String name) {
        this.id = id;
        this.imei = imei;
        this.mobile = mobile;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getImei() {
        return imei;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }
}
