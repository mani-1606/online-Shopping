package com.javaboy.mani.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private long id;
    private String name;

    // No product references here to avoid circular references
}
