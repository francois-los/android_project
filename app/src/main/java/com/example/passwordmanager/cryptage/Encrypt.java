package com.example.passwordmanager.cryptage;

public class Encrypt {
    public static String encrypt(String password) {
        String crypte="fzqgQjbVba";
        for (int i=0; i<password.length();i++)  {
            int c=password.charAt(i)^48;
            crypte=crypte+(char)c;
        }
        return crypte;
    }
}

