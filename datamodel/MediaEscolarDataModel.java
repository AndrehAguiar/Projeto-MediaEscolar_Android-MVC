package com.topartes.mediaescolar.datamodel;

public class MediaEscolarDataModel {

    private final static String TABELA = "media_escolar";

    private static final String id = "id";
    private static final String idpk = "idpk";
    private static final String materia = "materia";
    private static final String bimestre = "bimestre";
    private static final String situacao = "situacao";
    private static final String notaProva = "notaProva";
    private static final String mediaFinal = "mediaFinal";
    private static final String notaMateria = "notaMateria";

    private static String queryCriarTabela = "";

    public static String criarTabela() {

        queryCriarTabela = "CREATE TABLE " + TABELA;
        queryCriarTabela += "(";
        queryCriarTabela += id + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela += idpk + " INTEGER, ";
        queryCriarTabela += materia + " TEXT, ";
        queryCriarTabela += bimestre + " TEXT, ";
        queryCriarTabela += situacao + " TEXT, ";
        queryCriarTabela += notaProva + " REAL, ";
        queryCriarTabela += notaMateria + " REAL, ";
        queryCriarTabela += mediaFinal + " REAL ";
        queryCriarTabela += ")";

        return queryCriarTabela;
    }

    public static String getTABELA() {
        return TABELA;
    }

    public static String getId() {
        return id;
    }

    public static String getIdpk() {
        return idpk;
    }

    public static String getMateria() {
        return materia;
    }

    public static String getBimestre() {
        return bimestre;
    }

    public static String getSituacao() {
        return situacao;
    }

    public static String getNotaProva() {
        return notaProva;
    }

    public static String getNotaMateria() {
        return notaMateria;
    }

    public static String getMediaFinal() {
        return mediaFinal;
    }

    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        MediaEscolarDataModel.queryCriarTabela = queryCriarTabela;
    }

}
