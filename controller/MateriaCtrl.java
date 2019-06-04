package com.topartes.mediaescolar.controller;

import android.content.ContentValues;
import android.content.Context;

import com.topartes.mediaescolar.datamodel.MateriaDataModel;
import com.topartes.mediaescolar.datasource.DataSource;
import com.topartes.mediaescolar.model.Materia;

public class MateriaCtrl extends DataSource {

    public MateriaCtrl(Context context) {
        super(context);
    }

    ContentValues dados;

    public boolean salvarMateria(Materia objMateria) {

        boolean sucesso = true;

        dados = new ContentValues();

        dados.put(MateriaDataModel.getMateria(), objMateria.getMateria());

        sucesso = insert(MateriaDataModel.getTABELA(), dados);

        return sucesso;
    }
}
