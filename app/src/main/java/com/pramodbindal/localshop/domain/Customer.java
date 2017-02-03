package com.pramodbindal.localshop.domain;

import com.pramodbindal.localshop.validator.Required;
import com.pramodbindal.localshop.validator.Validate;

import java.io.Serializable;


/**
 * @author Pramod.Bindal
 */

public class Customer implements Serializable {

    private long id;

    @Validate(length = 10)
    private String mobile;

    @Required
    private String name;

    private String address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name;
    }
}
