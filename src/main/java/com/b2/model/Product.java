package com.b2.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Product {

    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Float price;

    @NotNull
    private Payment payment;
}
