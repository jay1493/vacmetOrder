package com.imagesoftware.anubhav.vacmet.model;

import java.util.List;

/**
 * Created by anubhav on 4/11/17.
 */

public class OrderContainer {
    private String invoiceNo;
    private List<OrderModel> orders;
    private String invoiceDate;

    public OrderContainer(String invoiceNo, List<OrderModel> orders, String invoiceDate) {
        this.invoiceNo = invoiceNo;
        this.orders = orders;
        this.invoiceDate = invoiceDate;
    }

    public OrderContainer() {
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public OrderContainer setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public List<OrderModel> getOrders() {
        return orders;
    }

    public OrderContainer setOrders(List<OrderModel> orders) {
        this.orders = orders;
        return this;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public OrderContainer setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }
}
