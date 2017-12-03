package com.nerdz.planb.auth;

import org.apache.commons.codec.binary.Hex;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by orcunozyurt on 12/3/17.
 */

public class Authentication {
    public static String getSignature(String secretKey, String publicKey) throws NoSuchAlgorithmException, InvalidKeyException {

        long timestamp = System.currentTimeMillis() / 1000L;
        String payload = timestamp + "." + publicKey;

        Mac sha256_Mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        sha256_Mac.init(secretKeySpec);
        String hashHex = Hex.encodeHex(sha256_Mac.doFinal(payload.getBytes())).toString().toLowerCase();
        String signature = payload + "." + hashHex;
        return signature;
    }


}
