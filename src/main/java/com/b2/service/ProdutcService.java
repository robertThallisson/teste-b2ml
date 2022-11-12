package com.b2.service;

import com.b2.exception.B2Exeception;
import com.b2.model.InstallmentDefinition;
import com.b2.model.Product;
import com.b2.model.Taxa;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProdutcService {
    public List<InstallmentDefinition> salvar(Product product) {


        if (product.getPayment().getEntry() != null && product.getPrice() <= product.getPayment().getEntry()) {
            throw  new B2Exeception(
                    "Não e possível gerar  parcelas com uma entrada maior que o valor do produto",
                    "Apenas erro de validação , entrada maior que o valor do produto"
            );
        }
        List<InstallmentDefinition> installmentsDefinition = new ArrayList<>();
        Float valorRestante = product.getPayment().getEntry() != null &&
                product.getPayment().getEntry() > 0 ? product.getPrice() - product.getPayment().getEntry() : product.getPrice();
        Float taxa = 0f;

        if (product.getPayment().getInstallments() > 6) {
            try {
                taxa = getTaxaSelic();
            } catch (Exception e) {
                throw e;
            }
        }

        Float valorParcela = (valorRestante/product.getPayment().getInstallments());

        if (taxa > 0) {
            valorParcela = valorParcela + ((valorParcela*taxa)/100);
        }
        for (int i = 1; i <= product.getPayment().getInstallments();i++) {
            InstallmentDefinition installmentDefinition = new InstallmentDefinition();
            installmentDefinition.setInstallmentsNum(i);
            installmentDefinition.setPrice(valorParcela);
            installmentDefinition.setTax(taxa);

            installmentsDefinition.add(installmentDefinition);
        }
        return installmentsDefinition;
    }


    public Float getTaxaSelic()  {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy");
        String dataInicial = LocalDate.now().minusDays(30).format(formatter);
        String dataFinal = LocalDate.now().format(formatter);
        String url = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.11/dados?formato=json&dataInicial=" + dataInicial + "&dataFinal=" + dataFinal;
        InputStream is = null;

        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            List<Taxa> taxas = Arrays.asList(new GsonBuilder().create().fromJson(jsonText, Taxa[].class));
            Float resultado = 0f;
            for (Taxa taxa: taxas) {
                resultado += taxa.getValor();
            }

            return resultado;
        } catch (Exception e) {
            throw  new B2Exeception(
                    "Falha ao carregar taxa selic",
                    e.getMessage()
            );
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw  new B2Exeception(
                        "Falha ao carregar taxa selic",
                        "Erro ao fecha InputStream " + e.getMessage()
                );
            }

        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
