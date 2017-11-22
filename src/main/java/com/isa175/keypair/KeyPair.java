package com.isa175.keypair;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class KeyPair {

    private PublicKey publicKey = null;
    private PrivateKey privateKey = null;
    private KeyReaders keyReaders = null;

    public KeyPair(KeyReaders keyReaders) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        this.keyReaders = keyReaders;
        publicKey = readPublicKey();
        privateKey = readPrivateKey();
    }

    public PublicKey getPublicKey(){
        return publicKey;
    }

    public PrivateKey getPrivateKey(){
        return privateKey;
    }

    private PublicKey readPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        return keyReaders.getPublicKeyReader().getKey();
    }

    private PrivateKey readPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        return keyReaders.getPrivateKeyReader().getKey();
    }
}
