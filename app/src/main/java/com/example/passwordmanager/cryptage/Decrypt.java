package com.example.passwordmanager.cryptage;

public class Decrypt {

    public static String decrypt(String password){
        String aCrypter= "fzqgQjbVba";
        for (int i=0; i<password.length();i++)  {
            int c=password.charAt(i)^48;
            aCrypter=aCrypter+(char)c;
        }
        return aCrypter;
    }
}
