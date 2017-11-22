package com.isa175.keypair;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface KeyReader<V extends Key> {

    V getKey() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException;

}
