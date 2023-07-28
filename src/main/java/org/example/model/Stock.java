package org.example.model;

import org.example.model.Product;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Stock")
public class Stock {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "warehouseName")
    private String warehouseName;
    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product owner;

    public Stock() {
    }

    public Stock(String warehouseName, int quantity, Product owner) {
        this.warehouseName = warehouseName;
        this.quantity = quantity;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getOwner() {
        return owner;
    }

    public void setOwner(Product owner) {
        this.owner = owner;
    }
}

