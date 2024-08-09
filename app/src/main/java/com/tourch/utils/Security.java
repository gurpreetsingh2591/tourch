package com.tourch.utils;


import android.util.Log;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Security {

    private static String KEY = "5Gq40NPjueL4BTSOwu8RNtCFb5QKq0IH";
    private static String IV = "80gUaNKtoE5XzfPo";

    public static String encrypt (String input) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        assert crypted != null;
        return new String(Hex.encodeHex(crypted));
    }

    public static String decrypt (String input) {

        byte[] output = new byte[256];
        try {
            SecretKeySpec skey = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);

            //output = cipher.doFinal(Base64.decodeBase64(input.getBytes ()));
            output = cipher.doFinal(Hex.decodeHex(input.toCharArray()));
        } catch (Exception e) {
            Log.e("Exception", "Exception here==>" + e.getLocalizedMessage().toString());
            System.out.println(e.toString());
        }
        return new String(output);
    }

}

