package com.example.passwordmanager;

import java.security.MessageDigest;

/**
 ** http://lugeek.com
 */

public class md5 {
    public static byte[] encryptMD5(byte[] data) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();

    }
}