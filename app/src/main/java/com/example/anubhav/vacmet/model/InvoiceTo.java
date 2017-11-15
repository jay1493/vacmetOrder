package com.example.anubhav.vacmet.model;

/**
 * Created by anubhav on 15/11/17.
 */

public class InvoiceTo {

    private String invoiceNo;

    private byte[] invoice;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public byte[] getInvoice() {
        return invoice;
    }

    public void setInvoice(byte[] invoice) {
        this.invoice = invoice;
    }

}
