package com.topartes.mediaescolar.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topartes.mediaescolar.R;
import com.topartes.mediaescolar.controller.MateriaCtrl;
import com.topartes.mediaescolar.datamodel.MateriaDataModel;
import com.topartes.mediaescolar.model.Materia;

public class ModeloFragment extends Fragment {

    View view ;
    Materia materia;

    public ModeloFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_modelo, container, false);

        MateriaCtrl materiaCtrl = new MateriaCtrl(getContext());
        materiaCtrl.criarTabela(MateriaDataModel.criarTabela());

        materia = new Materia();
        materia.setMateria("Português");
        materiaCtrl.salvarMateria(materia);

        materia = new Materia();
        materia.setMateria("Matemática");
        materiaCtrl.salvarMateria(materia);

        materia = new Materia();
        materia.setMateria("Inglês");
        materiaCtrl.salvarMateria(materia);

        return view;
    }
}
