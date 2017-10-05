package com.tradair.mock.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClient {

    private final static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    /**
     * send http get request to the input address
     *
     * @param address the address to send request
     * @param queryString the request parameters
     * @return request response
     */
    public static String sendGetRequest(String address, String queryString) {
        String requestString = queryString == null ? address : address + '?' + queryString;
        String response = null;
        HttpURLConnection connection = null;
        BufferedReader br = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(requestString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                logger.trace("got line : " + line);
                sb.append(line);
            }
            response = sb.toString();
        }
        catch (MalformedURLException e) {
            logger.error("error on creating url", e);
        }
        catch (IOException e) {
            logger.error("error on opening connection", e);
        }
        finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            catch (IOException e) {
                logger.error("error closing resource", e);
            }
        }
        return response;
    }

}
