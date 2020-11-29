package com.izzyacademy.sales.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.izzyacademy.sales.controllers.SalesReportController;
import com.izzyacademy.sales.models.*;
import com.izzyacademy.sales.utils.MySQLUtil;
import com.izzyacademy.sales.utils.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SalesReportService {

    private static final Logger logger = LoggerFactory.getLogger(SalesReportService.class);

    public static final String PRODUCT_SALES_BY_DATE_KEY_PREFIX = "productSalesByDate.";

    public static final String DEPT_SALES_BY_DATE_KEY_PREFIX = "departmentSalesByDate.";

    private MySQLUtil databaseUtil;

    private RedisCacheUtil cacheUtil;

    public SalesReportService()
    {
        this.databaseUtil = new MySQLUtil();

        this.cacheUtil = new RedisCacheUtil();
    }

    public List<ProductDetail> listProducts() {

        List<ProductDetail> results = new ArrayList<ProductDetail>();

        return results;
    }

    public ProductDetail getProduct(int productId) {

        return new ProductDetail();
    }

    public List<DepartmentDetail> listDepartments() {

        List<DepartmentDetail> results = new ArrayList<>();

        return results;
    }

    public DepartmentDetail getDepartment(int departmentId) {

        return new DepartmentDetail();
    }

    public List<ProductSalesReportForDate> getProductSalesByDate(String reportDate) {

        Gson gson = new Gson();

        Type dataType = new TypeToken<List<ProductSalesReportForDate>>() {}.getType();

        final String lookUpKey = PRODUCT_SALES_BY_DATE_KEY_PREFIX + reportDate;

        String cachedValue = this.cacheUtil.get(lookUpKey);

        List<ProductSalesReportForDate> results = null;

        boolean fromCache = false;

        // If this was a cache miss
        if (cachedValue == null || cachedValue.length() == 0) {

            // fetch from database
            results = this.getProductSalesByDateAux(reportDate);

            String resultString = gson.toJson(results, dataType);

            // store results in Redis cache
            this.cacheUtil.set(lookUpKey, resultString);

        } else { // result was found in cache

            fromCache = true;

            results = gson.fromJson(cachedValue, dataType);

        }

        logger.info("Results from Cache = " + fromCache);

        return results;
    }

    private List<ProductSalesReportForDate> getProductSalesByDateAux(String reportDate) {

        String SQL_SELECT = "SELECT p.product_id, p.name, SUM(ps.sku_price * oi.item_count) AS order_total" +
                " FROM products AS p" +
                " INNER JOIN product_skus AS ps ON (p.product_id = ps.product_id)" +
                " INNER JOIN order_items AS oi ON (oi.sku_id = ps.sku_id)" +
                " INNER JOIN orders AS o ON (o.order_id = oi.order_id)" +
                " WHERE SUBSTRING(o.date_created, 1, 10) = ? " +
                " GROUP BY p.product_id " +
                " ORDER BY p.product_id ASC";

        Connection conn = this.databaseUtil.getConnection();

        List<ProductSalesReportForDate> results = new ArrayList<>(32);
        try {

            PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT);

            preparedStatement.setString(1, reportDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int productId = resultSet.getInt("product_id");
                double orderTotal = resultSet.getDouble("order_total");
                String productName = resultSet.getString("name");

                final ProductSalesReportForDate resultItem = new ProductSalesReportForDate(productId, productName, orderTotal);

                results.add(resultItem);

            }

            this.databaseUtil.closeConnection();

            return results;

        } catch (Exception e) {

            throw new RuntimeException("DB Connection error during query", e);
        }
    }

    public List<DepartmentSalesReportForDate> getDepartmentSalesByDate(String reportDate) {

        Gson gson = new Gson();

        Type dataType = new TypeToken<List<DepartmentSalesReportForDate>>() {}.getType();

        final String lookUpKey = DEPT_SALES_BY_DATE_KEY_PREFIX + reportDate;

        String cachedValue = this.cacheUtil.get(lookUpKey);

        List<DepartmentSalesReportForDate> results = null;

        boolean fromCache = false;

        // If this was a cache miss
        if (cachedValue == null || cachedValue.length() == 0) {

            // fetch from database
            results = this.getDepartmentSalesByDateAux(reportDate);

            String resultString = gson.toJson(results, dataType);

            // store results in Redis cache
            this.cacheUtil.set(lookUpKey, resultString);

        } else { // result was found in cache

            fromCache = true;

            results = gson.fromJson(cachedValue, dataType);
        }

        logger.info("Results from Cache = " + fromCache);

        return results;
    }

    private List<DepartmentSalesReportForDate> getDepartmentSalesByDateAux(String reportDate) {

        String SQL_SELECT = "SELECT d.name AS department, SUM(ps.sku_price * oi.item_count) AS order_total" +
                " FROM products AS p  " +
                " INNER JOIN product_skus AS ps ON (p.product_id = ps.product_id) " +
                " INNER JOIN departments AS d ON (d.department_id = p.department) " +
                " INNER JOIN order_items AS oi ON (oi.sku_id = ps.sku_id) " +
                " INNER JOIN orders AS o ON (o.order_id = oi.order_id) " +
                " WHERE SUBSTRING(o.date_created, 1, 10) = ? " +
                " GROUP BY d.department_id " +
                " ORDER BY d.department_id ASC;";

        Connection conn = this.databaseUtil.getConnection();

        List<DepartmentSalesReportForDate> results = new ArrayList<>(32);

        try {

            PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT);

            preparedStatement.setString(1, reportDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String dept  = resultSet.getString("department");
                double orderTotal = resultSet.getDouble("order_total");

                final DepartmentSalesReportForDate resultItem = new DepartmentSalesReportForDate(dept, orderTotal);

                results.add(resultItem);

            }

            this.databaseUtil.closeConnection();

            return results;

        } catch (Exception e) {

            throw new RuntimeException("DB Connection error during query", e);
        }
    }

    public List<MaxProductSales> getMaxProductSales() {

        String SQL_SELECT = "SELECT SUBSTRING(o.date_created, 1, 10) AS sales_date, p.product_id, " +
                " p.name, MAX(ps.sku_price * oi.item_count) AS max_order_amount " +
                " FROM products AS p " +
                " INNER JOIN product_skus AS ps ON (p.product_id = ps.product_id) " +
                " INNER JOIN order_items AS oi ON (oi.sku_id = ps.sku_id) " +
                " INNER JOIN orders AS o ON (o.order_id = oi.order_id) " +
                " GROUP BY sales_date;";

        Connection conn = this.databaseUtil.getConnection();

        List<MaxProductSales> results = new ArrayList<>(32);

        try {

            PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT);

            ResultSet resultSet = preparedStatement.executeQuery();

            // sales_date | product_id | name | max_order_amount
            while (resultSet.next()) {

                String salesDate  = resultSet.getString("sales_date");
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("name");
                double maxOrderAmount = resultSet.getDouble("max_order_amount");

                final MaxProductSales resultItem = new MaxProductSales(salesDate, productId, productName, maxOrderAmount);

                results.add(resultItem);

            }

            this.databaseUtil.closeConnection();

            return results;

        } catch (Exception e) {

            throw new RuntimeException("DB Connection error during query", e);
        }
    }
}
