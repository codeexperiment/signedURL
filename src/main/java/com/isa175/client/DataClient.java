package com.isa175.client;

import org.apache.commons.codec.DecoderException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class DataClient {


    public static void main(String[] args) throws IOException, DecoderException, URISyntaxException {
        //Get the signature from the authorization server
        //You can change the dataset ID here.
        String datasetID = "326";
        String signature = new AuthorizedSignatureReader().getSignature(datasetID);
        //Present the authorization signature along with the dataset ID you want to access
        System.out.println(new DataReader().getSignature(signature, datasetID));
    }

    private static class AuthorizedSignatureReader {
        private HttpClient httpClient = new DefaultHttpClient();

        public String getSignature(String dataID) throws IOException, URISyntaxException {
            HttpClient client = new DefaultHttpClient();
            URI uri = new URI("http://localhost:8080/getKey/" + URLEncoder.encode(dataID ,"UTF8"));
            HttpGet httpGet = new HttpGet(uri);
            ResponseHandler<String> handler = new BasicResponseHandler();
            HttpResponse resp = client.execute(httpGet);
            return handler.handleResponse(resp);
        }
    }

    private static class DataReader {
        private HttpClient httpClient = new DefaultHttpClient();

        public String getSignature(String signature, String dataID) throws IOException, DecoderException {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://localhost:8081/" + dataID + "?sig=" + URLEncoder.encode( signature , "UTF8" ));
            System.out.println(httpGet.getURI());
            System.out.println("You can use the link above in your browser!");
            System.out.println("The signature presented in the sig param is only valid for dataset with ID " + dataID);
            System.out.println("Go ahead, try to change the dataset ID to 325 or 1403052");
            System.out.println("Or try to forge the signature without having access to the private key!\n\n\n");
            ResponseHandler<String> handler = new BasicResponseHandler();
            HttpResponse resp = client.execute(httpGet);

            return handler.handleResponse(resp);
        }
    }

}
