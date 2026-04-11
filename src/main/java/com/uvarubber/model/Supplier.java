package com.uvarubber.model;

public class Supplier {
    private int id;
    private String name;
    private String bankName;
    private String branchName;
    private String accountNo;
    private String nicNo;

    // Constructor
    public Supplier(int id, String name, String bankName, String branchName, String accountNo, String nicNo) {
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


    @Override
    public String toString() {
        return name; // This makes the name appear in the dropdown
    }
}
