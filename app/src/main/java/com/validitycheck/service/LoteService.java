package com.validitycheck.service;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.validitycheck.domain.Lote;
import com.validitycheck.domain.Secao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving Lote data from USGS.
 */
public final class LoteService {

    public static final long EMPTY = 0;
    private static final String LOG_TAG = LoteService.class.getSimpleName();
    //http://localhost:8080/Validy_Check/ws/lote
    public static String ip = "Validy_Check/ws/lote";

    /**
     * Create a private constructor because no one should ever create a {@link LoteService} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name LoteService (and an object instance of LoteService is not needed).
     */
    private LoteService() {
    }

    public static ArrayList<Lote> fetchLoteData(String ip_server) {
        Log.v(LOG_TAG, "fetchLoteData");

        // Create URL object
        URL url = createUrl(ip_server + ip);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Lote> lote = extractLotes(jsonResponse);

        // Return the {@link Event}
        return lote;
    }

    /**
     * Return a list of {@link Lote} objects that has been built up from-
     * parsing a JSON response.
     */
    public static ArrayList<Lote> extractLotes(String requestedJSON) {

        if (TextUtils.isEmpty(requestedJSON)) {
            return null;
        }
        ArrayList<Lote> lotes = new ArrayList<>();

        try {
            Gson loteGson = new Gson();
            Type type = new TypeToken<ArrayList<Lote>>() {
            }.getType();
            lotes = loteGson.fromJson(requestedJSON, type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the list of Secoes
        return lotes;
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
                Log.e(LOG_TAG, "Error response code: \n" + "" +
                        urlConnection.toString() + "\n" + urlConnection.getResponseCode() + "\n" + urlConnection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Lote JSON results from: " + url.toString(), e);
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

    public static Lote salvar(String ip_server, Lote lote) {
        Log.v(LOG_TAG, "salvar");

        // Create URL object
        URL url = createUrl(ip_server + ip);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        Gson gson = new Gson();
        try {
            String json = gson.toJson(lote);
            jsonResponse = makeHttpRequestSave(url, json);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        lote = gson.fromJson(jsonResponse, Lote.class);

        Log.v(LOG_TAG, "salvo" + jsonResponse);
        return lote;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequestSave(URL url, String json) throws IOException {
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
                Log.e(LOG_TAG, "Error response code: \n" + "" +
                        urlConnection.toString() + "\n" + urlConnection.getResponseCode() + "\n" + urlConnection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Lote JSON results.", e);
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


    public static Lote update(String ip_server, Lote lote) {
        Log.v(LOG_TAG, "EDITAR");

        // Create URL object
        URL url = createUrl(ip_server + ip);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        Gson gson = new Gson();
        try {
            String json = gson.toJson(lote);
            jsonResponse = makeHttpRequestUpdate(url, json);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        lote = gson.fromJson(jsonResponse, Lote.class);

        Log.v(LOG_TAG, "Salvo alterações no: " + jsonResponse);
        return lote;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequestUpdate(URL url, String json) throws IOException {
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
                Log.e(LOG_TAG, "Error response code: \n" + "" +
                        urlConnection.toString() + "\n" + urlConnection.getResponseCode() + "\n" + urlConnection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Lote JSON results.", e);
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

    public static Lote deletar(String ip_server, Lote lote) {
        Log.v(LOG_TAG, "deletar");

        // Create URL object
        URL url = createUrl(ip_server + ip);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        Gson gson = new Gson();
        try {
            String json = gson.toJson(lote);
            jsonResponse = makeHttpRequestDelete(url, json);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        lote = gson.fromJson(jsonResponse, Lote.class);

        Log.v(LOG_TAG, "Deletado " + jsonResponse);
        return lote;
    }

    private static String makeHttpRequestDelete(URL url, String json) throws IOException {
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
                Log.e(LOG_TAG, "Error response code: \n" + "" +
                        urlConnection.toString() + "\n" + urlConnection.getResponseCode() + "\n" + urlConnection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Lote JSON results.", e);
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

    public static ArrayList<Lote> fetchLoteSecaoFiltered(String ip_server, Secao secao, Long dataInicial, Long dataFinal) {
        Log.v(LOG_TAG, "fetchLoteSecaoFiltered");

        Gson gson = new Gson();
        String secaoJson = gson.toJson(secao, Secao.class);

        String request = null;
        try {
            request = ip + "/listar?secao=" + URLEncoder.encode(secaoJson, "UTF-8")
                    + "&dataInicial=" + URLEncoder.encode(dataInicial.toString(), "UTF-8")
                    + "&dataFinal=" + URLEncoder.encode(dataFinal.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Create URL object
        URL url = createUrl(ip_server + request);
        Log.v(LOG_TAG, "" + url.toString());
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequestSecaoFilter(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Lote> lote = extractLotes(jsonResponse);

        // Return the {@link Event}
        return lote;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequestSecaoFilter(URL url) throws IOException {
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
                Log.e(LOG_TAG, "Error response code: \n" + "" +
                        urlConnection.toString() + "\n" + urlConnection.getResponseCode() + "\n" + urlConnection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Lote JSON results.", e);
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