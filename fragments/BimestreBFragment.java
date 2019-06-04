package com.topartes.mediaescolar.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.topartes.mediaescolar.R;
import com.topartes.mediaescolar.controller.MediaEscolarCtrl;
import com.topartes.mediaescolar.model.MediaEscolar;
import com.topartes.mediaescolar.util.IncluirAsyncTask;
import com.topartes.mediaescolar.util.UtilMediaEscolar;
import com.topartes.mediaescolar.view.MainActivity;

public class BimestreBFragment extends Fragment {

    MediaEscolar mediaEscolar;
    MediaEscolarCtrl controller;

    Button btnCalcular;
    EditText editMateria;
    EditText editNotaProva;
    EditText editNotaTrabalho;
    TextView txtMateria;
    TextView txtResultado;
    TextView txtSituacaoFinal;

    double notaProva;
    double notaTrabalho;
    double media;

    Boolean dadosValidados = true;

    Context context;
    View view;

    public BimestreBFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        controller = new MediaEscolarCtrl(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bimestre_b, container, false);

        editMateria = view.findViewById(R.id.edMateria);
        editNotaProva = view.findViewById(R.id.edNotaProva);
        editNotaTrabalho = view.findViewById(R.id.edNotaTrabalho);

        btnCalcular = view.findViewById(R.id.btnCalcular);

        txtSituacaoFinal = view.findViewById(R.id.txtSituacaoFinal);
        txtResultado = view.findViewById(R.id.txtResultado);
        txtMateria = view.findViewById(R.id.txtMateria);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (editNotaProva.getText().toString().length() == 0 || controller.validaNotas(10.0)) {
                        dadosValidados = false;
                        UtilMediaEscolar.showMensagem(context, "Informe uma nota prova");
                        editNotaProva.requestFocus();
                    } else {
                        notaProva = Double.parseDouble(editNotaProva.getText().toString());
                        dadosValidados = true;
                    }
                    if (editNotaTrabalho.getText().toString().length() == 0 || controller.validaNotas(10.0)) {
                        dadosValidados = false;
                        UtilMediaEscolar.showMensagem(context, "Informe a nota do trabalho");
                        editNotaTrabalho.requestFocus();
                    } else {
                        notaTrabalho = Double.parseDouble(editNotaTrabalho.getText().toString());
                        dadosValidados = true;
                    }
                    if (editMateria.getText().toString().length() == 0) {
                        dadosValidados = false;
                        UtilMediaEscolar.showMensagem(context, "Informe a matéria");
                        editMateria.requestFocus();
                    }
                    if (dadosValidados) {

                        mediaEscolar = new MediaEscolar();
                        controller = new MediaEscolarCtrl(context);

                        mediaEscolar.setMateria(editMateria.getText().toString());
                        mediaEscolar.setNotaProva(Double.parseDouble(editNotaProva.getText().toString()));
                        mediaEscolar.setNotaTrabalho(Double.parseDouble(editNotaTrabalho.getText().toString()));
                        mediaEscolar.setBimestre("2º Bimestre");

                        media = controller.calcularMedia(mediaEscolar);

                        mediaEscolar.setMediaFinal(media);
                        mediaEscolar.setSituacao(controller.resultadoFinal(media));

                        txtResultado.setText(UtilMediaEscolar.formataValorDecimal(media));
                        txtSituacaoFinal.setText(mediaEscolar.getSituacao());
                        editNotaProva.setText(UtilMediaEscolar.formataValorDecimal(notaProva));
                        editNotaTrabalho.setText(UtilMediaEscolar.formataValorDecimal(notaTrabalho));

                        try {
                            controller.salvar(mediaEscolar);

                            UtilMediaEscolar.showMensagem(context, "Dados salvos com sucesso!");

                            IncluirAsyncTask task = new IncluirAsyncTask(mediaEscolar, context);
                            task.execute();
                        } catch (Exception e) {

                            UtilMediaEscolar.showMensagem(context, "Erro ao salvar os dados!" + e.getMessage());
                        }
                    }

                } catch (Exception e) {
                    UtilMediaEscolar.showMensagem(context, "Informe todos os dados");
                }

            }
        });

        return view;
    }
}