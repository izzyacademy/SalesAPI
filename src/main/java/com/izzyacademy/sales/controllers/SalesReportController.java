package com.izzyacademy.sales.controllers;

import com.izzyacademy.sales.models.*;
import com.izzyacademy.sales.services.SalesReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
@Tag(name = "Sales Report APIs", description = "API for Sales Reports")
@RestController()
public class SalesReportController {

    private final SalesReportService salesReportService;

    private static final Logger logger = LoggerFactory.getLogger(SalesReportController.class);

    public SalesReportController() {
        this.salesReportService = new SalesReportService();
    }

    @GetMapping(value="/products", produces = { "application/json" })
    @Operation(description = "Fetches a Product",
            responses = {@ApiResponse(responseCode = "200", description = "Returns all products") })
    public ResponseEntity listProducts() {

        List<ProductDetail> result = salesReportService.listProducts();

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping(value="/departments", produces = { "application/json" })
    @Operation(description = "Fetches a Product",
            responses = {@ApiResponse(responseCode = "200", description = "Returns all products") })
    public ResponseEntity listDepartments() {

        List<DepartmentDetail> result = salesReportService.listDepartments();

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping(value="/products/{productId}", produces = { "application/json" })
    @Operation(description = "Fetches a Product",
            responses = {@ApiResponse(responseCode = "200", description = "Returns all products") })
    public ResponseEntity getProduct(@PathVariable("productId")  int productId) {

        ProductDetail result = salesReportService.getProduct(productId);

        if (result == null) {

            return new ResponseEntity(new ErrorMessage("Product was not found for id=" + productId),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping(value="/departments/{departmentId}", produces = { "application/json" })
    @Operation(description = "Fetches a Dept",
            responses = {@ApiResponse(responseCode = "200", description = "Returns all departments") })
    public ResponseEntity getDepartment(@PathVariable("departmentId") int departmentId) {

        DepartmentDetail result = salesReportService.getDepartment(departmentId);

        if (result == null) {

            return new ResponseEntity(new ErrorMessage("Department was not found for id=" + departmentId),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping(value="/products/sales/{reportDate}", produces = { "application/json" })
    @Operation(description = "Product Sales for a Specific Date",
            responses = {@ApiResponse(responseCode = "200", description = "Returns all products") })
    public ResponseEntity getProductSalesForDate(@PathVariable("reportDate")  String reportDate) {

        List<ProductSalesReportForDate> result = salesReportService.getProductSalesByDate(reportDate);

        ResponseEntity responseEntity = new ResponseEntity(result, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping(value="/departments/sales/{reportDate}", produces = { "application/json" })
    @Operation(description = "Sales Report for Departments for a Date",
            responses = {@ApiResponse(responseCode = "200", description = "Returns all departments") })
    public ResponseEntity getDepartmentSalesForDate(@PathVariable("reportDate")  String reportDate) {

        List<DepartmentSalesReportForDate> result = salesReportService.getDepartmentSalesByDate(reportDate);

        ResponseEntity responseEntity = new ResponseEntity(result, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping(value="/products/max-sales/daily", produces = { "application/json" })
    @Operation(description = "Products with Maximum Sales for Each Date",
            responses = {@ApiResponse(responseCode = "200", description = "Returns all dates and products with max") })
    public ResponseEntity getMaxProductSales() {

        List<MaxProductSales> result = salesReportService.getMaxProductSales();

        ResponseEntity responseEntity = new ResponseEntity(result, HttpStatus.OK);

        return responseEntity;
    }
}
