package com.b2.exception;

import lombok.Data;

@Data
public class B2Exeception extends RuntimeException {

    private String msgUsuario;
    private String msgDesenvolvedor;
    public B2Exeception(String msgUsuario,String msgDesenvolvedor) {
        this.msgUsuario = msgUsuario;
        this.msgDesenvolvedor = msgDesenvolvedor;
    }
}
