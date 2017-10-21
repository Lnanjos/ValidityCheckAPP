package com.validycheck.service;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.validycheck.domain.Produto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving Produto data from USGS.
 */
public final class ProdutoService {

    private static final String LOG_TAG = ProdutoService.class.getSimpleName();

    //http://localhost:8080/Validy_Check/ws/produto
    public static String ip = "http://10.0.0.101:8080/Validy_Check/ws/produto";

    public static ArrayList<Produto> fetchProdutoData(){
        Log.v(LOG_TAG,"fetchProdutoData");

        // Create URL object
        URL url = createUrl(ip);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Produto> produto = extractSecoes(jsonResponse);

        // Return the {@link Event}
        return produto;
    }

    /**
     * Create a private constructor because no one should ever create a {@link ProdutoService} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name ProdutoService (and an object instance of ProdutoService is not needed).
     */
    private ProdutoService() {
    }

    /**
     * Return a list of {@link Produto} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Produto> extractSecoes(String requestedJSON) {

        if (TextUtils.isEmpty(requestedJSON)) {
            return null;
        }
        ArrayList<Produto> produtos = new ArrayList<>();

        try {
            Gson produtoGson = new Gson();
            Type type = new TypeToken<ArrayList<Produto>>() {}.getType();
            produtos = produtoGson.fromJson(requestedJSON,type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the list of Secoes
        return produtos;
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
            urlConnection.setReadTimeout(50000 /* milliseconds */);
            urlConnection.setConnectTimeout(55000 /* milliseconds */);
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
            Log.e(LOG_TAG, "Problem retrieving the Produto JSON results.", e);
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

    public static Produto salvar(Produto produto){
        Log.v(LOG_TAG,"salvar");

        // Create URL object
        URL url = createUrl(ip);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        Gson gson = new Gson();
        try {
            String json = gson.toJson(produto);
            jsonResponse = makeHttpRequestSave(url,json);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        produto = gson.fromJson(jsonResponse, Produto.class);

        Log.v(LOG_TAG,"salvo"+jsonResponse);
        return produto;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequestSave(URL url,String json) throws IOException {
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
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-type", "application/json");//define o que será enviado

            PrintStream printStream = new PrintStream(urlConnection.getOutputStream());
            printStream.println(json); //seta o que voce vai enviar

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
            Log.e(LOG_TAG, "Problem retrieving the Produto JSON results.", e);
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


    public static Produto update(Produto produto){
        Log.v(LOG_TAG,"EDITAR");

        // Create URL object
        URL url = createUrl(ip);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        Gson gson = new Gson();
        try {
            String json = gson.toJson(produto);
            jsonResponse = makeHttpRequestUpdate(url,json);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        produto = gson.fromJson(jsonResponse, Produto.class);

        Log.v(LOG_TAG,"Salvo alterações no: "+jsonResponse);
        return produto;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequestUpdate(URL url,String json) throws IOException {
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
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-type", "application/json");//define o que será enviado

            PrintStream printStream = new PrintStream(urlConnection.getOutputStream());
            printStream.println(json); //seta o que voce vai enviar

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
            Log.e(LOG_TAG, "Problem retrieving the Produto JSON results.", e);
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

    public static Produto deletar(Produto produto){
        Log.v(LOG_TAG,"deletar");

        // Create URL object
        URL url = createUrl(ip);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        Gson gson = new Gson();
        try {
            String json = gson.toJson(produto);
            jsonResponse = makeHttpRequestDelete(url,json);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        produto = gson.fromJson(jsonResponse, Produto.class);

        Log.v(LOG_TAG,"Deletado "+jsonResponse);
        return produto;
    }

    private static String makeHttpRequestDelete(URL url,String json) throws IOException {
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
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-type", "application/json");//define o que será enviado

            PrintStream printStream = new PrintStream(urlConnection.getOutputStream());
            printStream.println(json); //seta o que voce vai enviar

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
            Log.e(LOG_TAG, "Problem retrieving the Produto JSON results.", e);
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
}