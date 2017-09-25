package com.validycheck;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving Secao data from USGS.
 */
public final class SecaoService {

    private static final String LOG_TAG = SecaoService.class.getSimpleName();

    public static ArrayList<Secao> fetchSecaoData(String requestUrl){
        Log.v(LOG_TAG,"fetchSecaoData");
        // Create URL object
        /*
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Secao> secao = extractSecoes(jsonResponse);

        // Return the {@link Event}*/

        //dummy data to test
        ArrayList<Secao> secao = new ArrayList<Secao>();
        secao.add(new Secao(1,"Bebidas"));
        secao.add(new Secao(2,"Conservas"));
        secao.add(new Secao(3,"Doces"));
        secao.add(new Secao(4,"Salgados"));
        secao.add(new Secao(5,"Latcineos"));

        return secao;
    }

    /**
     * Create a private constructor because no one should ever create a {@link SecaoService} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name SecaoService (and an object instance of SecaoService is not needed).
     */
    private SecaoService() {
    }

    /**
     * Return a list of {@link Secao} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Secao> extractSecoes(String requestedJSON) {

        if (TextUtils.isEmpty(requestedJSON)) {
            return null;
        }
        ArrayList<Secao> secoes = new ArrayList<>();

        try {
            Gson secaoGson = new Gson();
            Type type = new TypeToken<ArrayList<Secao>>() {}.getType();
            secoes = secaoGson.fromJson(requestedJSON,type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the list of Secoes
        return secoes;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: \n"+"" +
                        urlConnection.toString()+"\n"+ urlConnection.getResponseCode()+"\n"+urlConnection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Secao JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
