package com.javaboy.mani.model;

import jakarta.persistence.*;

import java.sql.Blob;
@Entity
@Table(name = "Image")

public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String filename;
    private String filetype;
    @Lob
    private Blob image;

    private String downloadUrl;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    public Image(long id, String filename, String filetype, Blob image, String downloadUrl, Product product) {
        this.id = id;
        this.filename = filename;
        this.filetype = filetype;
        this.image = image;
        this.downloadUrl = downloadUrl;
        this.product = product;
    }
    public Image() {
    }

    public static void setFileName(String originalFilename) {
        // This method is not used in the current context, but can be implemented if needed.
        // It could be used to set the filename based on the original file name.
        // For example: this.filename = originalFilename;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", filetype='" + filetype + '\'' +
                ", image=" + image +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", product=" + product +
                '}';
    }
}
