package com.b2.controller;

import com.b2.model.InstallmentDefinition;
import com.b2.model.Product;
import com.b2.service.ProdutcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping("/product")
public class ProductCtrl {

    @Autowired
    private ProdutcService produtcService;

    @PostMapping("/salvar")
    private ResponseEntity<List<InstallmentDefinition>> salvar(@Valid @RequestBody Product product) {

        return ResponseEntity.ok(produtcService.salvar(product));

    }
}
