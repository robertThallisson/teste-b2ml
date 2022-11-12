package com.b2.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Payment {
    private Float entry;
    @NotNull
    private Long installments;
}
