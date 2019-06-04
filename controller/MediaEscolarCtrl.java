package com.topartes.mediaescolar.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.topartes.mediaescolar.datamodel.MediaEscolarDataModel;
import com.topartes.mediaescolar.datasource.DataSource;
import com.topartes.mediaescolar.model.MediaEscolar;

import java.util.ArrayList;
import java.util.List;

public class MediaEscolarCtrl extends DataSource {

    ContentValues dados;

    public MediaEscolarCtrl(Context context) {
        super(context);
    }

    public double calcularMedia(MediaEscolar obj) {

        return (obj.getNotaProva() + obj.getNotaTrabalho()) / 2;
    }

    public String resultadoFinal(double media) {

        return media >= 7 ? "Aprovado" : "Reprovado";
    }

    public boolean salvar(MediaEscolar obj) {

        boolean sucesso = true;

        dados = new ContentValues();

        dados.put(MediaEscolarDataModel.getIdpk(), obj.getIdpk());
        dados.put(MediaEscolarDataModel.getMateria(), obj.getMateria());
        dados.put(MediaEscolarDataModel.getBimestre(), obj.getBimestre());
        dados.put(MediaEscolarDataModel.getSituacao(), obj.getSituacao());
        dados.put(MediaEscolarDataModel.getNotaMateria(), obj.getNotaTrabalho());
        dados.put(MediaEscolarDataModel.getNotaProva(), obj.getNotaProva());
        dados.put(MediaEscolarDataModel.getMediaFinal(), obj.getMediaFinal());

        sucesso = insert(MediaEscolarDataModel.getTABELA(), dados);

        return sucesso;
    }

    public boolean alterar(MediaEscolar obj) {

        boolean sucesso = true;

        dados = new ContentValues();

        dados.put(MediaEscolarDataModel.getId(), obj.getId());
        dados.put(MediaEscolarDataModel.getIdpk(), obj.getIdpk());
        dados.put(MediaEscolarDataModel.getMateria(), obj.getMateria());
        dados.put(MediaEscolarDataModel.getBimestre(), obj.getBimestre());
        dados.put(MediaEscolarDataModel.getSituacao(), obj.getSituacao());
        dados.put(MediaEscolarDataModel.getNotaMateria(), obj.getNotaTrabalho());
        dados.put(MediaEscolarDataModel.getNotaProva(), obj.getNotaProva());
        dados.put(MediaEscolarDataModel.getMediaFinal(), obj.getMediaFinal());

        sucesso = alterar(MediaEscolarDataModel.getTABELA(), dados);

        return sucesso;
    }

    public boolean deletar(MediaEscolar obj) {

        boolean sucesso = true;
        sucesso = deletar(MediaEscolarDataModel.getTABELA(), obj.getId());

        return sucesso;
    }

    public List<MediaEscolar> listar() {
        return getAllMediaEscolar();
    }

    public ArrayList<MediaEscolar> getResultadoFinal() {
        return getAllResultadoFinal();
    }

    public Integer idSelectMediaEscolar(Integer id) {

        MediaEscolar mediaEscolar = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT id FROM MediaEscolarDataModel.getTABELA() WHERE id = " + id;

        return id;
    }

    public boolean validaNotas(Double nota) {
        return nota > 10;
    }

}
