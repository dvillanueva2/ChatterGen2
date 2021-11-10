package com.example.chattergen2;

import android.net.Uri;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NetworkAPI {

    public int postFormData(String urlString, Map<String, String> requestFormDataMap) // a string, a map that contains strings called requestFormDataMap
    {
        int responseCode = HttpURLConnection.HTTP_BAD_REQUEST;

        try
        {
            URL postUrl = new URL(urlString);
            // Step 2: open a connection
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();// (6)
            // Step 3: Set the request method
            connection.setRequestMethod("GET"); // (7)
            // Step 4: Set the request content-type header parameter. What format is the format of the data?
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // Step 5: Enable the connection to send output. Set output to the server.
            connection.setDoInput(true);
            // Step 6: Create the request body. How do we send data to the server? The data is sent as part of the body of the request. Let's create a map of all the data we need to send to the server.
            // Step 7: Convert key value pairs from the requestFormDataMap to a query string in the format
            // "paramName1=param1Value&paramName2&param2Value" (9)
            StringBuilder requestBodyBuilder = new StringBuilder(); // for dynamically building a new string
            requestFormDataMap.forEach((key, value) -> { // process one item to a collection and send the key and value of each item to the this block of code
                if (requestBodyBuilder.length() > 0) // we need to put an ampersand symbol if the length is greater than zero. If there are multiple parameters in the current string, they are separated by the ampersand symbol.
                {
                    requestBodyBuilder.append("&");
                }
                requestBodyBuilder.append(String.format("%s=%s", key, Uri.encode(value, "utf-8"))); // (10) the value has to be encoded and basically formed
            });

            String requestBody = requestBodyBuilder.toString(); // convert the stringBuilder to a string

            OutputStream out = new BufferedOutputStream(connection.getOutputStream()); // get a connection to an output stream... // there will be a network exception error here (see logcat, choose Info and choose network as a search keyword
            out.write(requestBody.getBytes(StandardCharsets.UTF_8)); // write requestBody to an output string. converts the string to an array of bytes. Standard character set is UTF_8
            out.close(); // closes the stream

            responseCode = connection.getResponseCode(); // (11)

            connection.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return responseCode;
    }
}
