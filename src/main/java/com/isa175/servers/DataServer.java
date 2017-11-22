package com.isa175.servers;

import com.isa175.keypair.KeyUtilities;
import com.isa175.servers.handlers.DataHandler;
import com.isa175.servers.handlers.Handler;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.Server;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * Role: Upon receiving a signedURL, it returns dataset for specified dataset id upon verification of valid signature.
 * How: it takes the signature (query param "sig")from the signedURL and verifies it using the Authorization Server's
 * (@{@link AuthorizationServer}) public key.
 * Note: The server requests the public key Authorization Server's (@{@link AuthorizationServer}) once upon startup. Technically,
 * it should be able to fetch the public key again when keys are rotated due to expiration date or compromised keys. For proof
 * of concept, it just grabs it once during server startup time.
 *
 * A signedURL is a regular URL + digital signature as query parameter, i.e. http://myexampleserver.billybob.com/12314?sig=AB75FEDC94
 */
public class DataServer {

    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8081);
        Handler handler = new DataHandler(new HttpClientKeyReader().getPublicKey());
        server.setHandler(handler.getHandler());
        server.start();
        server.join();
    }

    private static class HttpClientKeyReader {
        private HttpClient httpClient = new DefaultHttpClient();

        public PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://localhost:8080/getAuthorization");
            ResponseHandler<String> handler = new BasicResponseHandler();
            HttpResponse resp = client.execute(httpGet);

            String body = handler.handleResponse(resp);

            BASE64Decoder decoder = new BASE64Decoder();

            return KeyUtilities.toPublicKeyFromBytes(decoder.decodeBuffer(body));
        }
    }

}
