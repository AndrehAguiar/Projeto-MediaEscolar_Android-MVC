package com.topartes.mediaescolar.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.topartes.mediaescolar.datamodel.MediaEscolarDataModel;
import com.topartes.mediaescolar.model.MediaEscolar;
import com.topartes.mediaescolar.util.UtilMediaEscolar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class DataSource extends SQLiteOpenHelper {

    private static final String DB_NAME = "media_escolar.sqlite";
    private static final int DB_VERSION = 1;

    Cursor cursor;
    SQLiteDatabase db;

    /**
     * Abre banco de dados para edição
     *
     * @param context
     */

    public DataSource(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    /**
     * Cria a tabela no banco de dados com os atributos definidos
     *
     * @param db
     * @see com.topartes.mediaescolar.datamodel
     */

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            db.execSQL(MediaEscolarDataModel.criarTabela());

        } catch (Exception e) {

            Log.e("Media", "DB---> ERRO: " + e.getMessage());

        }

    }

    /**
     * Atualiza alterações na estrutura do banco de dados
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     * @see com.topartes.mediaescolar.datamodel
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Insere novos registros na tabela criada
     *
     * @param tabela
     * @param dados
     * @return boolean Sucesso se alterado
     * @see com.topartes.mediaescolar.controller
     */

    public boolean insert(String tabela, ContentValues dados) {
        boolean sucesso = true;
        try {
            sucesso = db.insert(tabela, null, dados) > 0;
        } catch (Exception e) {
            sucesso = false;
            Log.e("Insert", "ERRO ------> BD" + e.getMessage());
        }
        return sucesso;
    }

    /**
     * Altera os registros da tabela de acordo com o ID
     *
     * @param tabela
     * @param dados
     * @return boolean Sucesso se alterado
     * @see com.topartes.mediaescolar.controller
     */

    public boolean alterar(String tabela, ContentValues dados) {
        boolean sucesso = true;
        int id = dados.getAsInteger("id");
        try {
            sucesso = db.update(tabela, dados, "id=?", new String[]{Integer.toString(id)}) > 0;

        } catch (Exception e) {
            sucesso = false;
            Log.e("Editar", "ERRO -------- Id" + e.getMessage());
        }
        return sucesso;
    }

    /**
     * Deleta registros da tabela de acordo com o ID
     *
     * @param tabela
     * @param id
     * @return
     * @see com.topartes.mediaescolar.controller
     */

    public boolean deletar(String tabela, int id) {
        boolean sucesso = true;
        sucesso = db.delete(tabela, "id=?", new String[]{Integer.toString(id)}) > 0;

        return sucesso;
    }

    /**
     * Lista todos os registros da tabela
     *
     * @return List <lista>Id e Matéria</lista>
     */

    public List<MediaEscolar> getAllMediaEscolar() {
        MediaEscolar obj;

        List<MediaEscolar> lista = new ArrayList<>();

        String sql = "SELECT * FROM " + MediaEscolarDataModel.getTABELA() + " ORDER BY id ASC";
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                obj = new MediaEscolar();
                obj.setId(cursor.getInt(cursor.getColumnIndex(MediaEscolarDataModel.getId())));
                obj.setIdpk(cursor.getInt(cursor.getColumnIndex(MediaEscolarDataModel.getIdpk())));
                obj.setBimestre(cursor.getString(cursor.getColumnIndex(MediaEscolarDataModel.getBimestre())));
                obj.setMateria(cursor.getString(cursor.getColumnIndex(MediaEscolarDataModel.getMateria())));
                obj.setNotaProva(cursor.getDouble(cursor.getColumnIndex(MediaEscolarDataModel.getNotaProva())));
                obj.setNotaTrabalho(cursor.getDouble(cursor.getColumnIndex(MediaEscolarDataModel.getNotaMateria())));
                obj.setMediaFinal(cursor.getDouble(cursor.getColumnIndex(MediaEscolarDataModel.getMediaFinal())));
                obj.setSituacao(cursor.getString(cursor.getColumnIndex(MediaEscolarDataModel.getSituacao())));

                lista.add(obj);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public ArrayList<MediaEscolar> getAllResultadoFinal() {
        MediaEscolar obj;

        ArrayList<MediaEscolar> lista = new ArrayList<>();

        String sql = "SELECT * FROM " + MediaEscolarDataModel.getTABELA() + " ORDER BY id ASC";
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                obj = new MediaEscolar();

                obj.setId(cursor.getInt(cursor.getColumnIndex(MediaEscolarDataModel.getId())));
                obj.setIdpk(cursor.getInt(cursor.getColumnIndex(MediaEscolarDataModel.getIdpk())));
                obj.setBimestre(cursor.getString(cursor.getColumnIndex(MediaEscolarDataModel.getBimestre())));
                obj.setMateria(cursor.getString(cursor.getColumnIndex(MediaEscolarDataModel.getMateria())));
                obj.setNotaProva(cursor.getDouble(cursor.getColumnIndex(MediaEscolarDataModel.getNotaProva())));
                obj.setNotaTrabalho(cursor.getDouble(cursor.getColumnIndex(MediaEscolarDataModel.getNotaMateria())));
                obj.setMediaFinal(cursor.getDouble(cursor.getColumnIndex(MediaEscolarDataModel.getMediaFinal())));
                obj.setSituacao(cursor.getString(cursor.getColumnIndex(MediaEscolarDataModel.getSituacao())));

                lista.add(obj);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public void deletarTabela(String tabela) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + tabela);
        } catch (Exception e) {
            Log.e("ERRO Excluindo Tabela", e.getMessage());
        }
    }

    public void criarTabela(String queryCriarTabela) {
        try {
            db.execSQL(queryCriarTabela);
        } catch (SQLiteCantOpenDatabaseException e) {
            Log.e("ERRO Criando Tabela", e.getMessage());
        }
    }

    public void backupBancoDeDados() {

        File sd; // Caminho de destino - Download
        File data; // Caminho de origem - data/data/pacote/db_name

        File arquivoBancoDeDados; // Nome do banco de dados
        File arquivoBackupBancoDeDados; // Nome do arquivo de backup

        FileChannel origem; // Leitura do arquivo original
        FileChannel destino; // Gravação do arquivo de destino com o backup

        try {

            sd = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);

            data = Environment.getDataDirectory();

            Log.v("DB", "SD - " + sd.getAbsolutePath());
            Log.v("DB", "DATA - " + data.getAbsolutePath());

            if (sd.canWrite()) {

                String nomeDoBancoDeDados =

                        "//data//com.topartes.mediaescolar//databases/" + DB_NAME;

                String nomeDoArquivoDeBackup =

                        "bkp_" + DB_NAME;

                arquivoBancoDeDados = new File(data, nomeDoBancoDeDados);
                arquivoBackupBancoDeDados = new File(sd, nomeDoArquivoDeBackup);

                if (arquivoBancoDeDados.exists()) {

                    origem = new FileInputStream(
                            arquivoBancoDeDados).getChannel();

                    destino = new FileOutputStream(
                            arquivoBackupBancoDeDados).getChannel();

                    destino.transferFrom(origem, 0, origem.size());

                    origem.close();
                    destino.close();

                }

            }

        } catch (Exception e) {

            Log.e("DB", "Erro: " + e.getMessage());

        }
    }
}
