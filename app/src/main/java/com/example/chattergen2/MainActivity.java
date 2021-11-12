package com.example.chattergen2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.chattergen2.databinding.ActivityMainBinding;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private final String KEY_RESPONSE_CODE = "com.example.chattergen2.RESPONSE_CODE";

    // Step 2 - View Binding of the object. Creating an instance of the viewBinding object. This will be automatically imported to the main
    private ActivityMainBinding binding;

    private Handler postDataHandler = new Handler (Looper.getMainLooper()) // a handler runs in a main thread
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg); --> commenting this out.  get the status code instead
            Bundle bundle = msg.getData(); // the bundle is composed of key value pairs, just like a MAP
            int responseCode = bundle.getInt(KEY_RESPONSE_CODE);

                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    Toast.makeText(MainActivity.this, "Send Message was successful", Toast.LENGTH_SHORT).show(); // we want the MainActivity to run the toasts, in this case
                    binding.activityMainMessageEdittext.setText(""); // send an empty string after message sending is successful
                }
                else
                {
                    String message = String.format("Error sending with code %d", responseCode);
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show(); // we want the MainActivity to run the toasts, in this case
                }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main); useless for now

        // View Binding Step 3 - initialize the binding object to support view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // we call inflate method, and pass in the layout inflater(1)
        View view = binding.getRoot(); //(2)
        setContentView(view);
    }

    // create the button event handler (4)
    public void onSendClick(View view)
    {
        // NOTE: WE CAN SIMPLIFY EVERYTHING ELSE BELOW USING THE CUSTOM CLASS NetworkAPI
        // I still need to have a map that containing data that we want to send to the server.
        // Request body was mover here
        Map<String, String> requestFormDataMap = new HashMap<>();
        requestFormDataMap.put("DATA", binding.activityMainMessageEdittext.getText().toString());
        requestFormDataMap.put("LOGIN_NAME", "Dvillanueva");// hardcoding for now. We will add shared preferences where we can retrieve a login name as a property of a shared preference. This will be moved to a common class called network API where we can use lots of this code. We will use a class that in this demo is called NetworkAPI
        // End of Request body
        NetworkAPI networkAPI = new NetworkAPI(); // create an instance of NetworkAPI
        final String urlString = "https://capstone1.app.dmitcapstone.ca/api/jitters/JitterServlet";// hardcoding the URL for now that we're gonna post to if we have time we will retrieve this from a shared preference.
            // A Java 8 class called Completable Future can help fix the network exception error. see line 42 for the cause of the error for more details
        CompletableFuture<Void> postDataFuture = CompletableFuture.runAsync(() -> { // run the task aysnchronously, and don't wait for results
            int responseCode = networkAPI.postFormData(urlString, requestFormDataMap); // move this blocks of code to the CompletableFuture code block
//                if (responseCode == HttpURLConnection.HTTP_OK) --> borrowing this block of code because we will be using this in our handleMessage code block
//                {
//                    Toast.makeText(this, "Send Message was successful", Toast.LENGTH_SHORT).show();
//                    binding.activityMainMessageEdittext.setText(""); // send an empty string after message sending is successful
//                }
//                else
//                {
//                    String message = String.format("Error sending with code %d", responseCode);
//                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//                }
            Message msg = postDataHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_RESPONSE_CODE, responseCode);
            msg.setData(bundle);
            postDataHandler.sendMessage(msg); // to avoid missing internet permission exception error go to the manifest and add the internet permissions <uses-permission android:name="android.permission.INTERNET" />
            });
//            int responseCode = networkAPI.postFormData(urlString,  requestFormDataMap); // move this blocks of code to the CompletableFuture code block
//            if (responseCode == HttpURLConnection.HTTP_OK)
//            {
//                Toast.makeText(this, "Send Message was successful", Toast.LENGTH_SHORT).show();
//                binding.activityMainMessageEdittext.setText(""); // send an empty string after message sending is successful
//            }
//            else
//            {
//                String message = String.format("Error sending with code %d", responseCode);
//                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//            }
        }
    }

    // build the constraint layout. See activity_main xml file (3)

// Continue on 1:00 (debugging)
// link: https://nait.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cfb31812-be04-4ad0-b787-adc5015607e4

// View binding
// Once our project is done, I need to quickly adding support for view binding.
// expand the Gradle Scrips folder and open the module version of build.gradle script.
// just below the line 6 compileSdk 31:
// View Binding Step 1 --> inserting this feature as we build the app. After putting this in the gradle script, please hit sync gradle files now
//    buildFeatures{
//        viewBinding = true; // this generates a viewBinding class for me
//    }


// we have to change how the layout is assigned to the activity.
// (1) inflater is an object that we can help convert xml files to Java object for us
// get the root of the view and layout. manually import it.

// (2) in the activity_main xml, this is the root element of the xml
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".MainActivity">
// This can allow the child elements to access the constraint layout

// (3) do this in the activity_main xml

// (4) after doing the layout, need to get messages posted asynchronously.

// (5) Create the HTTPUrl class -- and surround it with a try / catch statement. Adding exceptions won't work - app will crash if I do this

// (6) HttpURLConnection - works for both http and https. HttpsURLConnection only works for https. For flexibility we are going to use the "http" connection
// cast the openConnection() to java (HttpURLConnection) by pressing Alt Enter

// (7) if you want to read data from server, we use GET, if we want to create resources on server, we use POST

// (8) the key is a String, and the value is also a String. A map is a data structure made of collection of key-value pairs (equal to dictionary in C#)
// A map is an interface. We cannot create objects using an interface. We have to create an object using an implementation of an interface.
// One implementation is a hashmap. If the keys have to be sorted, we need to use a TreeMap.

// (9)I need to convert mapped data object to a single string, therefore the parameter highlighted is not needed anymore

// (10) For example, the paramaeter could not contain a space character. What happens is that the space character will be replaced with a "%20" instead.

// (11) To know if the operation was executed successfully: we need to check the response code. When we make a request to the server, the server responds.
// The response contains a status code. If server was able to process the request, the server sends an OK response which is a http status 200.

// (12) this block of code is executing in a background thread. A background thread does not have access to the user interface such as a toast. It cannot access the views inside Activity. Only the Main user interface thread can do that.
// We need to send a message from our background thread to our main foreground thread. One way of sending a message is using a handler.

/* original block of code in the onSendClick method:

            // (5) Create the HTTPUrl class (5)
            // Step 1: Create a URL object
            URL postUrl = new URL("https://capstone1.app.dmitcapstone.ca/api/jitters/JitterServlet");
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
                OutputStream out = new BufferedOutputStream(connection.getOutputStream()); // get a connection to an output stream.
                out.write(requestBody.getBytes(StandardCharsets.UTF_8)); // write requestBody to an output string. converts the string to an array of bytes. Standard character set is UTF_8
                out.close(); // closes the stream

                int responseCode = connection.getResponseCode(); // (11)
                if (responseCode != HttpURLConnection.HTTP_OK)
                {
                    // error
                }
                connection.disconnect();
 */