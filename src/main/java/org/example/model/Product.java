package org.example.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "supplierArticle")
    private String supplierArticle;
    @Column(name = "quantity")
    private String quantity;
    @Column(name = "quantityFull")
    private String quantityFull;
    @Column(name = "nmId")
    private String nmId;
    @Column(name = "subject")
    private String subject;
    @Column(name = "warehouseName")
    private String warehouseName;
    @Column(name = "shopName")
    private String shopName;

    @OneToMany(mappedBy = "owner")
    private List<Stock> stocks;

    @OneToMany(mappedBy = "owner")
    private List<Item> items;

    public Product() {
    }

    public Product(String supplierArticle, String quantity, String quantityFull, String nmId, String subject, String warehouseName, String shopName, List<Stock> stocks) {
        this.supplierArticle = supplierArticle;
        this.quantity = quantity;
        this.quantityFull = quantityFull;
        this.nmId = nmId;
        this.subject = subject;
        this.warehouseName = warehouseName;
        this.shopName = shopName;
        this.stocks = stocks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupplierArticle() {
        return supplierArticle;
    }

    public void setSupplierArticle(String supplierArticle) {
        this.supplierArticle = supplierArticle;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityFull() {
        return quantityFull;
    }

    public void setQuantityFull(String quantityFull) {
        this.quantityFull = quantityFull;
    }

    public String getNmId() {
        return nmId;
    }

    public void setNmId(String nmId) {
        this.nmId = nmId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}


