package com.b2;

import com.b2.controller.ProductCtrl;
import com.b2.model.InstallmentDefinition;
import com.b2.model.Payment;
import com.b2.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductCtrlTest {

    @Autowired
    private ProductCtrl productCtrl;

    @Test
    public void contextLoads() throws Exception {
        assertThat(productCtrl).isNotNull();
    }

    @Test
    public void salvar() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        Product product = new Product();
        product.setPayment(new Payment());

        product.setId(123l);
        product.setName("RTX 4090");
        product.setPrice(15000f);
        product.getPayment().setEntry(5000f);
        product.getPayment().setInstallments(24l);
        ResponseEntity<InstallmentDefinition[]> response = restTemplate.postForEntity("http://localhost:8080/product/salvar", product, InstallmentDefinition[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
