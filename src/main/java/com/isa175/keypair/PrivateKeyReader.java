package com.isa175.keypair;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class PrivateKeyReader implements KeyReader<PrivateKey> {

    private String filename = null;

    PrivateKeyReader(String filename){
        this.filename = filename;
    }

    public PrivateKey getKey() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(KeyUtilities.getFileContent(filename));
        return KeyUtilities.getKeyFactory().generatePrivate(spec);
    }
}
