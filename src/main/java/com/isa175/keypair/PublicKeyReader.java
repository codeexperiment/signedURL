package com.isa175.keypair;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class PublicKeyReader implements KeyReader<PublicKey> {

    private String filename = null;

    PublicKeyReader(String filename){
        this.filename = filename;
    }

    public PublicKey getKey() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(KeyUtilities.getFileContent(filename));
        return KeyUtilities.getKeyFactory().generatePublic(spec);
    }
}
