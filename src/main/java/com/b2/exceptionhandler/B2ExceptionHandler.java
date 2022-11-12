package com.b2.exceptionhandler;

import com.b2.exception.B2Exeception;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class B2ExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource ms;

    @ExceptionHandler({ B2Exeception.class })
    public ResponseEntity<Object> handleAnamneseException(B2Exeception ex, WebRequest request) {
        String mu = ex.getMsgUsuario();
        String md = ex.getMessage();// ex.getMsgPadrao();
        return handleExceptionInternal(ex, new Erro(mu, md), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        // TODO Auto-generated method stub
        List<Erro> list = criarListaDeErro(ex.getBindingResult());

        return handleExceptionInternal(ex, list, headers, HttpStatus.BAD_REQUEST, request);
    }

    private List<Erro> criarListaDeErro(BindingResult br) {
        List<Erro> list = new ArrayList<>();

        for (FieldError fe : br.getFieldErrors()) {
            String mu = ms.getMessage(fe, LocaleContextHolder.getLocale());
            String md = fe.toString();
            list.add(new Erro(mu, md));
        }

        return list;
    }


    public static class Erro {
        private String mensagemUsuario;
        private String mensagemDesenvolvedor;

        public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
            super();
            this.mensagemUsuario = mensagemUsuario;
            this.mensagemDesenvolvedor = mensagemDesenvolvedor;
        }

        public String getMensagemUsuario() {
            return mensagemUsuario;
        }

        public void setMensagemUsuario(String mensagemUsuario) {
            this.mensagemUsuario = mensagemUsuario;
        }

        public String getMensagemDesenvolvedor() {
            return mensagemDesenvolvedor;
        }

        public void setMensagemDesenvolvedor(String mensagemDesenvolvedor) {
            this.mensagemDesenvolvedor = mensagemDesenvolvedor;
        }

    }
}
