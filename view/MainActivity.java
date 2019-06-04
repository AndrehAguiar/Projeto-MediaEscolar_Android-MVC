package com.topartes.mediaescolar.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.topartes.mediaescolar.R;
import com.topartes.mediaescolar.adapter.ResultadoFinalListAdapter;
import com.topartes.mediaescolar.controller.MediaEscolarCtrl;
import com.topartes.mediaescolar.datamodel.MediaEscolarDataModel;
import com.topartes.mediaescolar.fragments.BimestreAFragment;
import com.topartes.mediaescolar.fragments.BimestreBFragment;
import com.topartes.mediaescolar.fragments.BimestreCFragment;
import com.topartes.mediaescolar.fragments.BimestreDFragment;
import com.topartes.mediaescolar.fragments.ModeloFragment;
import com.topartes.mediaescolar.fragments.ResultadoFinalFragment;
import com.topartes.mediaescolar.model.MediaEscolar;
import com.topartes.mediaescolar.util.UtilMediaEscolar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    MediaEscolar mediaEscolar;

    FragmentManager fragmentManager;
    MediaEscolarCtrl controller;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getBaseContext();
        controller = new MediaEscolarCtrl(context);

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_fragment, new ModeloFragment()).commit();

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                MainActivity mainActivity = MainActivity.this;
                ResultadoFinalListAdapter resultadoFinalListAdapter;
                Snackbar.make(view, "Sincronizando os dados", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                fragmentManager.beginTransaction().replace(R.id.content_fragment, new ResultadoFinalFragment()).commit();

                SincronizarAsyncTask task = new SincronizarAsyncTask();
                task.execute();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sair) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.prim_bimestre) {

            setTitle("Nota 1º Bimestre");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new BimestreAFragment()).commit();

        } else if (id == R.id.seg_bimestre) {

            setTitle("Nota 2º Bimestre");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new BimestreBFragment()).commit();

        } else if (id == R.id.terc_bimestre) {

            setTitle("Nota 3º Bimestre");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new BimestreCFragment()).commit();

        } else if (id == R.id.quart_bimestre) {

            setTitle("Nota 4º Bimestre");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new BimestreDFragment()).commit();

        } else if (id == R.id.result_final) {

            setTitle("Resultado Final");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new ResultadoFinalFragment()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class SincronizarAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDiaglog = new ProgressDialog(MainActivity.this);

        HttpURLConnection conn;
        URL url = null;

        Uri.Builder builder;

        public SincronizarAsyncTask() {

            this.builder = new Uri.Builder();

            // "app" nome do dispositivo autrizado
            // "MediaEscolar" key da aplicação permitida
            // A key deve ser criptografada para manter a segurança

            builder.appendQueryParameter("app", "MediaEscolar");

        }

        @Override
        protected void onPreExecute() {
            progressDiaglog.setMessage("Os dados estão sendo atualizados, por favor aguarde...");
            progressDiaglog.setCancelable(false);
            progressDiaglog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            // Monta URL com script PHP
            try {

                url = new URL(UtilMediaEscolar.URL_WEB_SERVICE + "APISincronizarSistema.php");

            } catch (MalformedURLException e) {

                Log.e("WEBService", "MalformedURLException - " + e.getMessage());

            } catch (Exception e) {

                Log.e("WEBService", "Exception - " + e.getMessage());
            }

            try {

                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(UtilMediaEscolar.CONECTION_TIMEOUT);
                conn.setReadTimeout(UtilMediaEscolar.READ_TIME_OUT);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("charset", "utf-8");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

            } catch (IOException e) {

                Log.e("WEBService", "IOException - " + e.getMessage());

            }

            try {
                int response_code = conn.getResponseCode();

                /* Lista de ResponseCodes comuns
                 * 200 OK
                 * 403 forbideen
                 * 404 pg não encontrada
                 * 500 erro interno no servidor */

                if (response_code == HttpURLConnection.HTTP_OK) {

                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();

                    String line;

                    while ((line = reader.readLine()) != null) {

                        result.append(line);
                    }
                    return (result.toString());
                } else {
                    return "Erro de conexão";
                }

            } catch (IOException e) {

                Log.e("WEBService", "IOException - " + e.getMessage());
                return e.toString();

            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            try {

                JSONArray jArray = new JSONArray(result);

                if (jArray.length() != 0) {
                    // Salvar dados recebidos no BD SQLite

                    controller.deletarTabela(MediaEscolarDataModel.getTABELA());
                    controller.criarTabela(MediaEscolarDataModel.criarTabela());

                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject jsonObject = jArray.getJSONObject(i);
                        MediaEscolar obj = new MediaEscolar();

                        obj.setIdpk((jsonObject.getInt(MediaEscolarDataModel.getId())));
                        obj.setMateria((jsonObject.getString(MediaEscolarDataModel.getMateria())));
                        obj.setBimestre((jsonObject.getString(MediaEscolarDataModel.getBimestre())));
                        obj.setNotaProva((jsonObject.getDouble(MediaEscolarDataModel.getNotaProva())));
                        obj.setNotaTrabalho((jsonObject.getDouble(MediaEscolarDataModel.getNotaMateria())));
                        obj.setMediaFinal((jsonObject.getDouble(MediaEscolarDataModel.getMediaFinal())));
                        obj.setSituacao((jsonObject.getString(MediaEscolarDataModel.getSituacao())));

                        controller.salvar(obj);

                    }
                } else {
                    UtilMediaEscolar.showMensagem(context, "Nenhum registro encontrado...");
                }

            } catch (Exception e) {

                Log.e("WEBService", "Erro JSONException - " + e.getMessage());

            } finally {


                if (progressDiaglog != null && progressDiaglog.isShowing()) {
                    progressDiaglog.dismiss();

                }

            }
        }

    }

}
