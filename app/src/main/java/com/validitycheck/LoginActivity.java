package com.validitycheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class LoginActivity extends AppCompatActivity {

    TextView infoServer;
    EditText ipText;
    Button loginButton;
    String myPrefs = "COM.VALIDYCHECK.PREFERENCES";
    boolean ativo = false;
    private String user = "";
    private String password = "";
    private String ip_server = "";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText login = (EditText) findViewById(R.id.login);
        final EditText senha = (EditText) findViewById(R.id.password);


        infoServer = (TextView) findViewById(R.id.infoServer);
        ipText = (EditText) this.findViewById(R.id.ipText);
        ipText.setEnabled(false);

        loginButton = (Button) findViewById(R.id.buttonLogin);
        loginButton.setEnabled(false);

        NetworkSniffTask task = new NetworkSniffTask(this);
        task.execute();

        SharedPreferences sharedPref = getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(getString(R.string.login), "" + login.getText());
                editor.putString(getString(R.string.senha), "" + senha.getText());
                editor.putString(getString(R.string.ip_server), "" + ipText.getText());
                editor.commit();

                user = "" + login.getText();
                password = "" + senha.getText();

                SharedPreferences sharedPref = getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
                System.out.println(sharedPref.getString(getString(R.string.login), getString(R.string.login_default)));
                System.out.println(sharedPref.getString(getString(R.string.senha), getString(R.string.senha_default)));
                System.out.println(sharedPref.getString(getString(R.string.ip_server), getString(R.string.ip_server_default)));


                Autenticacao autenticacao = new Autenticacao();
                ip_server = "" + ipText.getText();
                autenticacao.execute("" + ipText.getText());

                System.out.println(ip_server);

                if (ativo) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public class NetworkSniffTask extends AsyncTask<Void, Void, String> {

        private static final String LOG_TAG = "nstask";

        private WeakReference<Context> mContextRef;

        public NetworkSniffTask(Context context) {
            mContextRef = new WeakReference<Context>(context);
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(LOG_TAG, "Let's sniff the network");
            String response = getString(R.string.server_not_found);
            try {
                Context context = mContextRef.get();

                if (context != null) {

                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                    WifiInfo connectionInfo = wm.getConnectionInfo();
                    int ipAddress = connectionInfo.getIpAddress();
                    String ipString = Formatter.formatIpAddress(ipAddress);


                    Log.d(LOG_TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                    Log.d(LOG_TAG, "ipString: " + String.valueOf(ipString));

                    String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                    Log.d(LOG_TAG, "prefix: " + prefix);

                    for (int i = 0; i < 255; i++) {
                        String resposta = "";
                        String testIp = "http://" + prefix + String.valueOf(i) + ":8080/Validy_Check/ws/autenticacao";

                        URL url = null;

                        HttpURLConnection urlConnection = null;
                        InputStream inputStream = null;
                        try {
                            url = new URL(testIp);
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setReadTimeout(80 /* milliseconds */);
                            urlConnection.setConnectTimeout(30 /* milliseconds */);
                            urlConnection.setRequestMethod("GET");
                            urlConnection.connect();

                            // If the request was successful (response code 200),
                            // then read the input stream and parse the response.
                            if (urlConnection.getResponseCode() == 200) {
                                inputStream = urlConnection.getInputStream();
                                resposta = readFromStream(inputStream);
                                System.out.println(resposta);
                                return response = "http://" + prefix + String.valueOf(i) + ":8080/";
                            } else {
                                Log.e(LOG_TAG, "Error response code: \n" + "" +
                                        urlConnection.toString() + "\n" + urlConnection.getResponseCode() + "\n" + urlConnection.getResponseMessage());
                            }
                        } catch (IOException e) {
                            Log.v(LOG_TAG, "NOT reacheable " + url.toString());
                        } finally {
                            if (urlConnection != null) {
                                urlConnection.disconnect();
                            }
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                Log.e(LOG_TAG, "Well that's not good.", t);
            }
            System.out.println(response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            ProgressBar progressBar = (ProgressBar) LoginActivity.this.findViewById(R.id.progressBarLogin);
            progressBar.setVisibility(View.GONE);
            if (s != getString(R.string.server_not_found)) {
                infoServer.setText("SERVIDOR ENCONTRADO");
                ipText.setText(s.toString());
                ipText.setEnabled(true);
            } else {
                infoServer.setText(R.string.server_not_found);
                ipText.setEnabled(true);
            }
            loginButton.setEnabled(true);
        }
    }

    public class Autenticacao extends AsyncTask<String, Void, String> {

        private static final String LOG_TAG = "Autenticação";

        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            String jsonResponse = "";
            try {
                System.out.println(params);
                url = new URL("" + ip_server + "Validy_Check/ws/autenticacao");
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error with creating URL ", e);
                return jsonResponse = "invalido";
            }

            String json = "{\"login\":\"" + user + "\",\"senha\":\"" + password + "\"}";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse = "invalido";
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
                    return jsonResponse = "falha ao conectar";
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("Sim")) {
                ativo = true;
            } else if (s.equals("Não")) {
                Toast.makeText(LoginActivity.this, "Usuario não encontrado ou inativo", Toast.LENGTH_SHORT).show();
                ativo = false;
            } else if (s.equals("falha ao conectar")) {
                Toast.makeText(LoginActivity.this, "Falha ao Conectar", Toast.LENGTH_SHORT).show();
                ativo = false;
            } else if (s.equals("invalido")) {
                Toast.makeText(LoginActivity.this, "Falha ao Conectar", Toast.LENGTH_SHORT).show();
                ativo = false;
            }
        }
    }
}
