package com.izzyacademy.sales.models;

public class DepartmentSalesReportForDate {

    private String department;

    private double orderTotal;

    public DepartmentSalesReportForDate()
    {

    }

    public DepartmentSalesReportForDate(String dept, double orderTotal)
    {
        this.department = dept;
        this.orderTotal = orderTotal;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }
}
