//package com.topartes.mediaescolar.util;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.topartes.mediaescolar.controller.MediaEscolarCtrl;
//import com.topartes.mediaescolar.datamodel.MediaEscolarDataModel;
//import com.topartes.mediaescolar.model.MediaEscolar;
//import com.topartes.mediaescolar.view.MainActivity;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//public class SincronizarAsyncTask extends AsyncTask<String, String, String> {
//
//    HttpURLConnection conn;
//    URL url = null;
//    Uri.Builder builder;
//
//    private MediaEscolarCtrl controller;
//    Context context;
//
//    ProgressDialog progressDialog = new ProgressDialog(context);
//    MainActivity mainActivity;
//
//    final ThreadLocal<ProgressDialog> progressDiaglog = new ThreadLocal<ProgressDialog>() {
//        @Override
//        protected ProgressDialog initialValue() {
//            return new ProgressDialog(context);
//        }
//    };
//
//    public SincronizarAsyncTask(MediaEscolar obj, Context context) {
//
//
//        this.builder = new Uri.Builder();
//
//        this.context = context;
//
//        // "app" nome do dispositivo autrizado
//        // "MediaEscolar" key da aplicação permitida
//        // A key deve ser criptografada para manter a segurança
//
//        builder.appendQueryParameter("app", "MediaEscolar");
//
//    }
//
//    @Override
//    protected void onPreExecute() {
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Os dados estão sendo atualizados, por favor aguarde...");
//        progressDialog.setCancelable(false);
//        progressDialog.getContext();
//        progressDialog.show();
//
//    }
//
//    @Override
//    protected String doInBackground(String... strings) {
//
//        // Monta URL com script PHP
//        try {
//
//            url = new URL(UtilMediaEscolar.URL_WEB_SERVICE + "APISincronizarSistema.php");
//
//        } catch (MalformedURLException e) {
//
//            Log.e("WEBService", "MalformedURLException - " + e.getMessage());
//
//        } catch (Exception e) {
//
//            Log.e("WEBService", "Exception - " + e.getMessage());
//        }
//
//        try {
//
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(UtilMediaEscolar.CONECTION_TIMEOUT);
//            conn.setReadTimeout(UtilMediaEscolar.READ_TIME_OUT);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("charset", "utf-8");
//
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//
//            String query = builder.build().getEncodedQuery();
//
//            OutputStream os = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//
//            writer.write(query);
//            writer.flush();
//            writer.close();
//            os.close();
//
//            conn.connect();
//
//        } catch (IOException e) {
//
//            Log.e("WEBService", "IOException - " + e.getMessage());
//
//        }
//
//        try {
//            int response_code = conn.getResponseCode();
//
//            /* Lista de ResponseCodes comuns
//             * 200 OK
//             * 403 forbideen
//             * 404 pg não encontrada
//             * 500 erro interno no servidor */
//
//            if (response_code == HttpURLConnection.HTTP_OK) {
//
//                InputStream input = conn.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//                StringBuilder result = new StringBuilder();
//
//                String line;
//
//                while ((line = reader.readLine()) != null) {
//
//                    result.append(line);
//                }
//                return (result.toString());
//            } else {
//                return "Erro de conexão";
//            }
//
//        } catch (IOException e) {
//
//            Log.e("WEBService", "IOException - " + e.getMessage());
//            return e.toString();
//
//        } finally {
//            conn.disconnect();
//        }
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//
//        try {
//
//            JSONArray jArray = new JSONArray(result);
//
//            if (jArray.length() != 0) {
//                // Salvar dados recebidos no BD SQLite
//
//                controller.deletarTabela(MediaEscolarDataModel.getTABELA());
//                controller.criarTabela(MediaEscolarDataModel.criarTabela());
//
//                for (int i = 0; i < jArray.length(); i++) {
//
//                    JSONObject jsonObject = jArray.getJSONObject(i);
//                    MediaEscolar obj = new MediaEscolar();
//
//                    obj.setId((jsonObject.getInt(MediaEscolarDataModel.getId())));
//                    obj.setIdpk((jsonObject.getInt(MediaEscolarDataModel.getId())));
//                    obj.setMateria((jsonObject.getString(MediaEscolarDataModel.getMateria())));
//                    obj.setBimestre((jsonObject.getString(MediaEscolarDataModel.getBimestre())));
//                    obj.setNotaProva((jsonObject.getDouble(MediaEscolarDataModel.getNotaProva())));
//                    obj.setNotaTrabalho((jsonObject.getDouble(MediaEscolarDataModel.getNotaMateria())));
//                    obj.setMediaFinal((jsonObject.getDouble(MediaEscolarDataModel.getMediaFinal())));
//                    obj.setSituacao((jsonObject.getString(MediaEscolarDataModel.getSituacao())));
//
//                    controller.salvar(obj);
//
//                }
//            } else {
//                UtilMediaEscolar.showMensagem(context, "Nenhum registro encontrado...");
//            }
//
//        } catch (Exception e) {
//
//            Log.e("WEBService", "Erro JSONException - " + e.getMessage());
//
//        } finally {
//
//
//            if (progressDialog != null && progressDialog.isShowing()) {
//                progressDialog.dismiss();
//
//            }
//
//        }
//    }
//
//}
