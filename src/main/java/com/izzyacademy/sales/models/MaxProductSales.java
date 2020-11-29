package com.izzyacademy.sales.models;

public class MaxProductSales {

    private String salesDate;

    private int productId;

    private String productName;

    private double maxOrderAmount;

    public MaxProductSales() {

    }

    public MaxProductSales(String date, int id, String name, double amount) {

        this.productId = id;
        this.productName = name;
        this.salesDate = date;
        this.maxOrderAmount = amount;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getMaxOrderAmount() {
        return maxOrderAmount;
    }

    public void setMaxOrderAmount(double maxOrderAmount) {
        this.maxOrderAmount = maxOrderAmount;
    }
}
