package com.pramodbindal.localshop.domain;


import com.pramodbindal.localshop.validator.Required;
import com.pramodbindal.localshop.validator.Validate;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private long id;


    @Required
    private Customer customer;

    private String comment;

    @Validate(minValue = 1)
    private double amount;
    private TransactionType transactionType;
    private Date date;

    public Transaction(Customer customer, double amount, TransactionType transactionType, Date date) {
        this.customer = customer;
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Date getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", customer=" + customer.getName() +
                ", amount=" + amount +
                ", transactionType=" + transactionType +
                ", date=" + date +
                '}';
    }
}
