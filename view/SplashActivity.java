package com.topartes.mediaescolar.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Adapter;

import com.topartes.mediaescolar.R;
import com.topartes.mediaescolar.adapter.ResultadoFinalListAdapter;
import com.topartes.mediaescolar.controller.MediaEscolarCtrl;
import com.topartes.mediaescolar.model.MediaEscolar;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class SplashActivity
        extends AppCompatActivity
        implements TextToSpeech.OnInitListener{

    private static final int SPLASH_TIME_OUT = 5000;

    /*
    O serviço textToSpeech é nativo do androide
    É responsável por reproduzir um texto específico
    depende da propriedade " Locale.ENGLISH "
    No caso a lingua inglesa por padrão e pode ser alterado
    Caso o telefone não tenha o pacote de língua definido será ignorado
    Não funciona no emulador nativo do Android
    Tutorial
    https://pt.stackoverflow.com/questions/45276/rodar-aplicativos-no-android-studio-atrav%C3%A9s-do-celular-pelo-usb
    */

    private  TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MediaEscolarCtrl mediaEscolarCtrl = new MediaEscolarCtrl(getBaseContext());
        mediaEscolarCtrl.backupBancoDeDados();

        textToSpeech = new TextToSpeech(this, this);

        apresentaTelaSplash();
    }

    private void apresentaTelaSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                MediaEscolarCtrl mediaEscolarCtrl = new MediaEscolarCtrl(getBaseContext());

                List<MediaEscolar> objetos = mediaEscolarCtrl.listar();

                for (MediaEscolar obj : objetos) {
                    Log.i("CRUD LISTAR", "ID: " + obj.getId() + " - Matéria: " + obj.getMateria());
                }

                Intent telaPrincipal = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(telaPrincipal);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onInit(int status){
        if(status == TextToSpeech.SUCCESS){
            int result = textToSpeech.setLanguage(Locale.ENGLISH);
            if(result == TextToSpeech.LANG_MISSING_DATA ||
                 result == TextToSpeech.LANG_NOT_SUPPORTED){
                //Log
                Log.e("MVC", "Idioma não suportado");
            }else{
                boasVindas();
            }
        }else{
            Log.e("MVC","Falha ao iniciar TTS");
        }

    }

    private void boasVindas() {
        String texto = "Olá! Bem vindo ao projeto média escolar!";

        textToSpeech.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy(){
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
