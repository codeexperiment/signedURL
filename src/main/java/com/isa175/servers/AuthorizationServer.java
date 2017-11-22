package com.isa175.servers;

import com.isa175.keypair.KeyPair;
import com.isa175.keypair.KeyReaders;
import com.isa175.servers.handlers.AuthorizationHandler;
import com.isa175.servers.handlers.Handler;
import org.eclipse.jetty.server.Server;

/**
 * Role: grants signed document (signature) of dataset id to users eligible for access to dataset id.
 * signature is usually appended to url as a query parameter to form a signedURL.
 * How: if user granted access, the server will sign the dataset id with the private key.
 * Note: Only the @{@link AuthorizationServer} has access to the private key.
 * Other functions: returns public key to any client upon request used for verification of signature.
 */
public class AuthorizationServer {

    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        KeyPair keyPair = new KeyPair(new KeyReaders(args[0], args[1]));
        Handler handler = new AuthorizationHandler(keyPair);
        server.setHandler(handler.getHandler());
        server.start();
        server.join();
    }

}
