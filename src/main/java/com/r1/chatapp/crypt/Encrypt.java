// Author: Satu
// tietokantaan tallennettavien salasanojen salaaminen SHA-256-salausalgoritmilla

package com.r1.chatapp.crypt;

import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
  
public class Encrypt {  

    public static String encryptPassword(String plainTextPassword) {

        String password = plainTextPassword;  
        String encryptedpassword = null;  

        try {
            // SHA-256 instanssi
            MessageDigest m = MessageDigest.getInstance("SHA-256");

            // parametrina saadun selkokielisen salasanan muuttaminen hash-muotoon
            m.update(password.getBytes());

            // hashattu salasana tavumuodossa:
            byte[] bytes = m.digest();  

            // tavujen muuttaminen hexadesimaalimuotoon
            StringBuilder sb = new StringBuilder();  

            for (byte b : bytes) {  
                sb.append(String.format("%02x", b));
            }  
            
            // palauttaa hashatun salasanan string-muodossa
            encryptedpassword = sb.toString();  
            return encryptedpassword;
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Salasanan hashaus epäonnistui");
            e.printStackTrace();
            return null;
        }  
    }  
}  