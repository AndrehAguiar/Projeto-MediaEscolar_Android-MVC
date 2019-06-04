package com.topartes.mediaescolar.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.topartes.mediaescolar.R;
import com.topartes.mediaescolar.adapter.ResultadoFinalListAdapter;
import com.topartes.mediaescolar.controller.MediaEscolarCtrl;
import com.topartes.mediaescolar.model.MediaEscolar;

import java.util.ArrayList;


public class ResultadoFinalFragment extends Fragment {

    // Adapter
    // dataSet com os dados

    ArrayList<MediaEscolar> dataSet;

    // ListView para apresentar os dados

    ListView listView;

    // Controller para buscar os dados no DB

    MediaEscolarCtrl controller;

    // novo método na controller getResultadoFinal devolvendo um ArrayList
    // Efeito de Animação da Lista

    View view;

    public ResultadoFinalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_resultado_final,
                container,
                false);

        controller = new MediaEscolarCtrl(getContext());

        listView = view.findViewById(R.id.listview);

        dataSet = controller.getAllResultadoFinal();

        final ResultadoFinalListAdapter adapter =
                new ResultadoFinalListAdapter(dataSet, getContext());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MediaEscolar mediaEscolar = dataSet.get(position);

                Snackbar.make(view, mediaEscolar.getMateria() +
                        "\n" + mediaEscolar.getSituacao() +
                        " Média Final: " +
                        mediaEscolar.getMediaFinal(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                dataSet = controller.getAllResultadoFinal();
                adapter.atualizarLista(dataSet);
            }
        });

        return view;
    }
}