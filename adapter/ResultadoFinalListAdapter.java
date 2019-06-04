package com.topartes.mediaescolar.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.topartes.mediaescolar.R;
import com.topartes.mediaescolar.controller.MediaEscolarCtrl;
import com.topartes.mediaescolar.datasource.DataSource;
import com.topartes.mediaescolar.model.MediaEscolar;
import com.topartes.mediaescolar.util.AlterarAsyncTask;
import com.topartes.mediaescolar.util.DeletarAsyncTask;
import com.topartes.mediaescolar.util.UtilMediaEscolar;
import com.topartes.mediaescolar.view.MainActivity;

import java.util.ArrayList;

public class ResultadoFinalListAdapter
        extends ArrayAdapter<MediaEscolar>
        implements View.OnClickListener {

    public static int mediaControler;
    Context context;
    // private int ultimaPositcao = -1;

    AlertDialog.Builder builder;
    AlertDialog alert;
    MediaEscolar mediaEscolar;
    MediaEscolarCtrl controller;

    ArrayList<MediaEscolar> dados;
    ViewHolder linha;

    private static class ViewHolder {
        TextView txtBimestre,
                txtSituacao,
                txtMateria,
                txtMedia;
        ImageView imgItem,
                imgConsultar,
                imgEditar,
                imgDeletar,
                imgSalvar;
    }

    public ResultadoFinalListAdapter(ArrayList<MediaEscolar> dataSet, Context context) {
        super(context, R.layout.listview_resultado_final, dataSet);

        this.dados = dataSet;
        this.context = context;
    }

    public void atualizarLista(ArrayList<MediaEscolar> novosDados) {
        this.dados.clear();
        this.dados.addAll(novosDados);
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(final View view) {
        //mediaEscolar = null;
        int posicao = (Integer) view.getTag();
        Object object = getItem(posicao);
        mediaEscolar = (MediaEscolar) object;
        controller = new MediaEscolarCtrl(getContext());

        switch (view.getId()) {
            case R.id.imgItem:
                Snackbar.make(view, "Nota da Prova: " +
                                mediaEscolar.getNotaProva(),
                        Snackbar.LENGTH_LONG).setAction("No Action", null).show();
                break;

            case R.id.imgDeletar:
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("ALERTA");
                builder.setMessage("Deseja deletar esse registro");
                builder.setCancelable(true);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("SIM", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            controller.deletar(mediaEscolar);

                            DeletarAsyncTask task =
                                    new DeletarAsyncTask(mediaEscolar, context);
                            task.execute();

                            //notifyDataSetChanged();

                            atualizarLista(controller.getResultadoFinal());

                        } catch (Exception e) {
                            Log.e("DELETAR", "ERRO-----Resultado Final delete média escolar" + e.getMessage());
                        }
                    }
                });
                builder.setNegativeButton("CANCELAR", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert = builder.create();
                alert.show();

                break;

            case R.id.imgConsultar:
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("CONSULTA");
                builder.setMessage(
                        "Bimestre: " + mediaEscolar.getBimestre() + "\n" +
                                "Matéria: " + mediaEscolar.getMateria() + "\n" +
                                "Situação: " + mediaEscolar.getSituacao() + "\n" +
                                "Nota da Prova: " + mediaEscolar.getNotaProva() + "\n" +
                                "Nota do Trabalho: " + mediaEscolar.getNotaTrabalho() + "\n" +
                                "Média Final: " + mediaEscolar.getMediaFinal()
                );
                builder.setCancelable(true);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("Voltar", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert = builder.create();
                alert.show();

                break;

            case R.id.imgSalvar:
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("ALERTA");
                builder.setMessage("Deseja salvar esse registro");
                builder.setCancelable(true);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("SIM", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            controller.alterar(mediaEscolar);

                        } catch (Exception e) {
                            Log.e("SALVAR", "ERRO-----Resultado Final salvar média escolar" + e.getMessage());
                        }
                        atualizarLista(controller.getResultadoFinal());
                    }
                });
                builder.setNegativeButton("CANCELAR", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert = builder.create();
                alert.show();

                break;

            case R.id.imgEditar:

                final View alertView = view.inflate(getContext(), R.layout.editar_listview, null);

                final EditText edMateria = alertView.findViewById(R.id.edMateria);
                final EditText edNotaProva = alertView.findViewById(R.id.edNotaProva);
                final EditText edNotaTrabalho = alertView.findViewById(R.id.edNotaTrabalho);

                final TextView txtSituacao = alertView.findViewById(R.id.txtResultado);
                final TextView txtMediaFinal = alertView.findViewById(R.id.txtSituacaoFinal);
                final Integer id = mediaEscolar.getId();
                final Integer idpk = mediaEscolar.getIdpk();

                edNotaProva.setText(Double.toString(mediaEscolar.getNotaProva()));
                edNotaTrabalho.setText(Double.toString(mediaEscolar.getNotaTrabalho()));
                edMateria.setText(mediaEscolar.getMateria());

                txtSituacao.setText(mediaEscolar.getSituacao());
                txtMediaFinal.setText(Double.toString(mediaEscolar.getMediaFinal()));

                AlertDialog.Builder alertbox = new AlertDialog.Builder(alertView.getRootView().getContext());
                alertbox.setMessage(mediaEscolar.getBimestre());
                alertbox.setTitle("Editar");

                alertbox.setView(alertView);
                alertbox.setNeutralButton("Gravar alteração",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //MediaEscolarCtrl mediaController = new MediaEscolarCtrl(context);
                                //mediaController.idSelectMediaEscolar(id);
                                mediaEscolar.setId(id);
                                mediaEscolar.setIdpk(idpk);
                                mediaEscolar.setMateria(edMateria.getText().toString());
                                mediaEscolar.setNotaProva(Double.parseDouble(edNotaProva.getText().toString()));
                                mediaEscolar.setNotaTrabalho(Double.parseDouble(edNotaTrabalho.getText().toString()));

                                Double mediaFinal = controller.calcularMedia(mediaEscolar);
                                mediaEscolar.setSituacao(controller.resultadoFinal(mediaFinal));

                                mediaEscolar.setMediaFinal(mediaFinal);

                                try {

                                    controller.alterar(mediaEscolar);
                                    AlterarAsyncTask task = new AlterarAsyncTask(mediaEscolar, context);
                                    task.execute();

                                } catch (Exception e) {

                                    Log.e("DELETAR", "ERRO-----Resultado Final alterar média escolar" + e.getMessage());
                                }
                                atualizarLista(controller.getResultadoFinal());
                            }
                        });

                alertbox.show();
                break;
        }
    }

    // Padrão de projeto Observer
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position,
                        View dataSet,
                        @NonNull ViewGroup parent) {

        mediaEscolar = getItem(position);

        if (dataSet == null) {

            linha = new ViewHolder();
            LayoutInflater layoutResultadoFinalList = LayoutInflater.from(getContext());
            dataSet = layoutResultadoFinalList.inflate(R.layout.listview_resultado_final, parent, false);

            linha.txtMateria = dataSet.findViewById(R.id.txtMateria);
            linha.txtBimestre = dataSet.findViewById(R.id.txtBimestre);
            linha.txtSituacao = dataSet.findViewById(R.id.txtResultado);
            linha.txtMedia = dataSet.findViewById(R.id.txtMedia);

            linha.imgItem = dataSet.findViewById(R.id.imgItem);
            linha.imgConsultar = dataSet.findViewById(R.id.imgConsultar);
            linha.imgEditar = dataSet.findViewById(R.id.imgEditar);
            linha.imgDeletar = dataSet.findViewById(R.id.imgDeletar);
            linha.imgSalvar = dataSet.findViewById(R.id.imgSalvar);

            dataSet.setTag(linha);
        } else {
            linha = (ViewHolder) dataSet.getTag();
        }

        linha.txtMateria.setText(mediaEscolar.getMateria());
        linha.txtBimestre.setText(mediaEscolar.getBimestre());
        linha.txtSituacao.setText(mediaEscolar.getSituacao());
        linha.txtMedia.setText(UtilMediaEscolar.formataValorDecimal(mediaEscolar.getMediaFinal()));

        linha.txtMedia.setText(String.valueOf(mediaEscolar.getMediaFinal()));

        linha.imgItem.setOnClickListener(this);
        linha.imgItem.setTag(position);

        linha.imgDeletar.setOnClickListener(this);
        linha.imgDeletar.setTag(position);

        linha.imgEditar.setOnClickListener(this);
        linha.imgEditar.setTag(position);

        linha.imgSalvar.setOnClickListener(this);
        linha.imgSalvar.setTag(position);

        linha.imgConsultar.setOnClickListener(this);
        linha.imgConsultar.setTag(position);

        return dataSet;
    }

}
