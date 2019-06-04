package com.topartes.mediaescolar.datamodel;

public class MateriaDataModel {

    private final static String TABELA = "materia";

    private static String cod = "cod";
    private static String materia = "materia";

    private static String queryCriarTabela = "";

    public static String criarTabela() {
        queryCriarTabela = "CREATE TABLE " + TABELA;
        queryCriarTabela += "(";
        queryCriarTabela += cod + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela += materia + " TEXT)";

        return queryCriarTabela;
    }

    public static String getTABELA() {
        return TABELA;
    }

    public static String getCod() {
        return cod;
    }

    public static String getMateria() {
        return materia;
    }

    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        MateriaDataModel.queryCriarTabela = queryCriarTabela;
    }
}
