package com.isa175.servers.handlers;

import com.isa175.keypair.KeyPair;
import com.isa175.keypair.KeyUtilities;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

/**
 * The authorization handler will prepare a signature for the requested dataset ID.
 * For proof of concept, it will grant access to everyone by returning a signature.
 * In the real world, this is where it decides to a return a signature or not
 * based on the user's credentials.
 *
 * Even though everyone is granted access, users will still have to present a valid
 * signature to the data service @{@link DataHandler}. The data service will
 * verify the signature using the public key available upon request from the
 * authorization server.
 *
 * The key here is that only the authorization handler has access to the private key.
 *
 * Short of that assumption then anyone can generate a valid signature.
 */
public class AuthorizationHandler implements Handler {

    private enum REQUEST {
        AUTH, //request type to return public key
        KEY //request type to return signed document of dataset id (digital signature)
    }

    private ServletContextHandler handler = null;

    public AuthorizationHandler(KeyPair keyPair){
        this.handler = createHandler(keyPair);
    }

    public static class PublicKeyServlet extends HttpServlet
    {
        private KeyPair keypair = null;
        private REQUEST request = null;

        public PublicKeyServlet(KeyPair keypair, REQUEST request)
        {
            this.keypair = keypair;
            this.request = request;
        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            //Client request for public key should always be granted for verification of signature by client
            if(this.request == REQUEST.AUTH) {
                response.getWriter().println(new BASE64Encoder().encode(keypair.getPublicKey().getEncoded()));
            } else {
                //This is where a user's credentials is checked whether to return a signature (grant access) or not.
                //For proof of concept, no checks will be made and all users will be granted signatures upon request
                // but signatures will still need to be presented to the data server @DataHandler
                try {
                    Signature sig = KeyUtilities.getSignature();
                    sig.initSign(keypair.getPrivateKey());
                    sig.update(request.getRequestURI().split("/")[2].getBytes());
                    byte[] signatureBytes = sig.sign();
                    String encodedString = Hex.encodeHexString(new BASE64Encoder().encode(signatureBytes).getBytes());
                    response.getWriter().println(encodedString);
                } catch (SignatureException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ServletContextHandler createHandler(KeyPair keypair){
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        context.addServlet(new ServletHolder(new PublicKeyServlet(keypair, REQUEST.AUTH)),"/getAuthorization/*");
        context.addServlet(new ServletHolder(new PublicKeyServlet(keypair, REQUEST.KEY)),"/getKey/*");

        return context;
    }

    public ServletContextHandler getHandler(){
        return handler;
    }
}
