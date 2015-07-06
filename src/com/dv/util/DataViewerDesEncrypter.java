/*
 * DataViewerDesEncrypter.java  2/6/13 1:04 PM
 *
 * Copyright (C) 2012-2013 Nick Ma
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.dv.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * @author xyma
 */
public class DataViewerDesEncrypter {

    private static char[] pwdChars =
            "abcdefghijklmnopqrstuvqxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();

    private static String key = "ILOVEDATAVIEWER";

    public DataViewerDesEncrypter() {
    }

    public static String encrypt(String value) {
        try {
            SecretKey secretKey = getSecretKey();

            Cipher ecipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cleartext = value.getBytes("UTF8");
            byte[] ciphertext = ecipher.doFinal(cleartext);

            sun.misc.BASE64Encoder base64encoder = new sun.misc.BASE64Encoder();
            return base64encoder.encode(ciphertext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKey getSecretKey() throws Exception {
        byte[] keyAsBytes = key.getBytes("UTF8");
        KeySpec keySpec = new DESKeySpec(keyAsBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(keySpec);
    }

    public static String decrypt(String value) {
        try {
            SecretKey secretKey = getSecretKey();

            Cipher dcipher = Cipher.getInstance("DES");
            dcipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(value);
            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateKey(int length) {
        try {
            StringBuffer key = new StringBuffer(length);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] intbytes = new byte[4];

            for (int i = 0; i < length; i++) {
                random.nextBytes(intbytes);
                key.append(pwdChars[Math.abs(getIntFromByte(intbytes) % pwdChars.length)]);
            }

            return key.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static int getIntFromByte(byte[] bytes) {
        int returnNumber = 0;
        int pos = 0;
        returnNumber += byteToInt(bytes[pos++]) << 24;
        returnNumber += byteToInt(bytes[pos++]) << 16;
        returnNumber += byteToInt(bytes[pos++]) << 8;
        returnNumber += byteToInt(bytes[pos++]) << 0;
        return returnNumber;
    }

    private static int byteToInt(byte b) {
        return (int) b & 0xFF;
    }
}
