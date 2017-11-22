package com.isa175.servers.handlers;

import com.isa175.keypair.KeyUtilities;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.*;

/**
 * The data handler returns the requested dataset upon verification of signature of given dataset id.
 *
 * In effect if signature is enc(datasetID) and dataset id == dec(enc(dataset id)), then the data server returns the dataset.
 *
 * The decryption works using the public key obtained from the authorization server (@{@link com.isa175.servers.AuthorizationServer}).
 *
 * Anyone can have access to the public key. Only the private key should be in the hands of the Authorization Server.
 *
 * A signedURL is a regular URL + digital signature as query parameter, i.e. http://myexampleserver.billybob.com/12314?sig=AB75FEDC94
 *
 * In other words, in this example, user is granted access to dataset with id 12314 if dec(enc(12314)) == dec(sig) == 12314
 */

public class DataHandler implements Handler {

    private PublicKey publicKey;

    public DataHandler(PublicKey publicKey){
        this.publicKey = publicKey;
    }

    public static class DataServlet extends HttpServlet
    {
        private PublicKey publicKey = null;

        public DataServlet(PublicKey publicKey)
        {
            this.publicKey = publicKey;
        }

        //
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);

            String dataID = request.getRequestURI().split("/")[1];
            byte[] signature = new byte[0];
            try {
                signature = new BASE64Decoder().decodeBuffer(new String(Hex.decodeHex(String.format(URLDecoder.decode(request.getParameter("sig"), "UTF8")).trim().toCharArray())));
            } catch (DecoderException e) {
                e.printStackTrace();
            }

            Signature sig = null;
            try {
                sig = KeyUtilities.getSignature();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            try {
                sig.initVerify(publicKey);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }

            try {
                sig.update(dataID.getBytes());
            } catch (SignatureException e) {
                e.printStackTrace();
            }

            boolean verifies = false;

            try {
                verifies = sig.verify(signature);
            } catch (SignatureException e) {
                e.printStackTrace();
            }
            if(verifies){
                response.getWriter().println("Congrats!  Here is the dataset with dataset ID " + dataID + ": not very interesting data");
            } else {
                response.getWriter().println("You do not have access to dataset ID " + dataID);
                response.getWriter().println("Please present a valid signature for dataset ID " + dataID);
            }
        }
    }

    public ServletContextHandler getHandler(){
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        context.addServlet(new ServletHolder(new DataServlet(publicKey)),"/*");

        return context;
    }

}
