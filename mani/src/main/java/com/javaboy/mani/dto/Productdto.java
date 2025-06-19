package com.javaboy.mani.dto;

import com.javaboy.mani.model.Category;
import com.javaboy.mani.model.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data

public class Productdto {

    private long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
    private List<Imagedto> images;

}
