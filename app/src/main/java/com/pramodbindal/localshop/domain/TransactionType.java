package com.pramodbindal.localshop.domain;

public enum TransactionType {
    DEBIT("Dr"),
    CREDIT("Cr"),
    NA("Na");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public static TransactionType fromValue(String name) {
        for (TransactionType transactionType : values()) {
            if (transactionType.value().equalsIgnoreCase(name)) {
                return transactionType;
            }
        }
        return NA;
    }

    public String value() {
        return value;
    }
}
