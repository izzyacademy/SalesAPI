package com.izzyacademy.sales.models;

public class ProductSalesReportForDate {

    private int productId;

    private String productName;

    private double orderTotal;

    public ProductSalesReportForDate()
    {

    }

    public ProductSalesReportForDate(int productId, double orderTotal)
    {
        this.productId = productId;
        this.orderTotal = orderTotal;
    }

    public ProductSalesReportForDate(int productId, String name, double orderTotal)
    {
        this.productId = productId;
        this.productName = name;
        this.orderTotal = orderTotal;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
