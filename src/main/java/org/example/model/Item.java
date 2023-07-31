package org.example.model;

import javax.persistence.*;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "cdate")
    private String cdate;
    @Column(name = "ctime")
    private String ctime;
    @Column(name = "finishedPrice")
    private int finishedPrice;
    @Column(name = "forPay")
    private int forPay;
    @Column(name = "odid")
    private String odid;
    @Column(name = "oblastOkrugName")
    private String oblastOkrugName;
    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product owner;

    public Item() {
    }

    public Item(String cdate, String ctime, int finishedPrice, int forPay, String odid, String oblastOkrugName, String status, Product owner) {
        this.cdate = cdate;
        this.ctime = ctime;
        this.finishedPrice = finishedPrice;
        this.forPay = forPay;
        this.odid = odid;
        this.oblastOkrugName = oblastOkrugName;
        this.status = status;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public int getFinishedPrice() {
        return finishedPrice;
    }

    public void setFinishedPrice(int finishedPrice) {
        this.finishedPrice = finishedPrice;
    }

    public int getForPay() {
        return forPay;
    }

    public void setForPay(int forPay) {
        this.forPay = forPay;
    }

    public String getOdid() {
        return odid;
    }

    public void setOdid(String odid) {
        this.odid = odid;
    }

    public String getOblastOkrugName() {
        return oblastOkrugName;
    }

    public void setOblastOkrugName(String oblastOkrugName) {
        this.oblastOkrugName = oblastOkrugName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Product getOwner() {
        return owner;
    }

    public void setOwner(Product owner) {
        this.owner = owner;
    }
}
