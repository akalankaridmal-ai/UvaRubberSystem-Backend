package com.uvarubber.model;

public class Supplier {
    private int id;
    private String name;
    private String bankName;
    private String accountNo;

    // Constructor
    public Supplier(int id, String name, String bankName, String accountNo) {
        this.id = id;
        this.name = name;
        this.bankName = bankName;
        this.accountNo = accountNo;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBankName() { return bankName; }
    public String getAccountNo() { return accountNo; }
}