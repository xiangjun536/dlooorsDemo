package com.example.droolsdemo.model;

public class Order {
    private String customerType;
    private double amount;
    private double discount;

    public Order() {
    }

    public Order(String customerType, double amount) {
        this.customerType = customerType;
        this.amount = amount;
        this.discount = 0.0;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerType='" + customerType + '\'' +
                ", amount=" + amount +
                ", discount=" + discount +
                '}';
    }
}