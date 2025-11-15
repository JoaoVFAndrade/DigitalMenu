package com.br.digitalmenu.exception;

public class TentativasDeLoginException extends RuntimeException{
    public TentativasDeLoginException(String mensagem) {
        super(mensagem);
    }
}
