package org.example.model;

import javax.persistence.*;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "src")
    private String src;

    @Column(name = "image")
    private int image;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product owner;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Media(String src, int image, Product owner) {
        this.src = src;
        this.owner = owner;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Media() {
    }

    public Product getOwner() {
        return owner;
    }

    public void setOwner(Product owner) {
        this.owner = owner;
    }
}
