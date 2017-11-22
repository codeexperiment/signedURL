package com.isa175.keypair;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class KeyUtilities {

    private static String ALGO = "RSA";
    private static String SIG_ALGO = "SHA256WithRSA";


    public static KeyFactory getKeyFactory() throws NoSuchAlgorithmException {
        return KeyFactory.getInstance(ALGO);
    }

    public static Signature getSignature() throws NoSuchAlgorithmException {
        return Signature.getInstance(SIG_ALGO);
    }

    public static byte[] getFileContent(String filename) throws IOException {
        File file = new File(filename);
        DataInputStream fdis = new DataInputStream(new FileInputStream(file));
        byte[] bytes = new byte[(int)file.length()];

        try {
            fdis.readFully(bytes);
        } finally {
            fdis.close();
        }

        return bytes;
    }

    public static PublicKey toPublicKeyFromBytes(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        return KeyUtilities.getKeyFactory().generatePublic(spec);
    }

}
