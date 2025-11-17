package com.br.digitalmenu.util;

public class Util {
    public static boolean validarSenha(String senha) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9])\\S{6,8}$";
        return senha.matches(regex);
    }
}
