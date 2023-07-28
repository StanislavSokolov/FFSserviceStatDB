package org.example.model;

public class ItemShop {
    private String subject = "";
    private String supplierArticle = "";
    private String totalPrice = "0";
    private String forPay = "0";
    private String warehouseName = "";
    private String regionName = "";

    public ItemShop(String subject, String supplierArticle, String totalPrice, String warehouseName, String oblast, String date) {
        this.subject = subject;
        this.supplierArticle = supplierArticle;
        this.totalPrice = totalPrice;
        this.warehouseName = warehouseName;
        this.oblast = oblast;
        this.date = date;

        this.order = 1;
        this.rating = 1;
    }

    public String getOblast() {
        return oblast;
    }

    public void setOblast(String oblast) {
        this.oblast = oblast;
    }

    private String oblast = "";

    private int sale = 0;
    private int order = 0;
    private int rating = 0;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ItemShop(String subject, String supplierArticle, String totalPrice, String forPay, String warehouseName, String regionName, String date) {
        this.subject = subject;
        this.supplierArticle = supplierArticle;
        this.totalPrice = totalPrice;
        this.forPay = forPay;
        this.warehouseName = warehouseName;
        this.regionName = regionName;
        this.date = date;

        this.sale = 1;
        this.rating = 1;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSupplierArticle() {
        return supplierArticle;
    }

    public void setSupplierArticle(String supplierArticle) {
        this.supplierArticle = supplierArticle;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getForPay() {
        return forPay;
    }

    public void setForPay(String forPay) {
        this.forPay = forPay;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date = "";


}



