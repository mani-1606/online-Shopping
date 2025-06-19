package com.javaboy.mani.model;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> prduct;

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {
    }

    public Category(String name) {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


    public <E> List<E> getProducts() {
        return (List<E>) prduct;

    }
}
