package com.isa175.keypair;

public class KeyReaders {

    private PublicKeyReader publicKeyReader = null;
    private PrivateKeyReader privateKeyReader = null;

    public KeyReaders(String publicKeyFile, String privateKeyFile){
        publicKeyReader = new PublicKeyReader(publicKeyFile);
        privateKeyReader = new PrivateKeyReader(privateKeyFile);
    }

    public PublicKeyReader getPublicKeyReader() {
        return publicKeyReader;
    }

    public PrivateKeyReader getPrivateKeyReader(){
        return privateKeyReader;
    }
}
