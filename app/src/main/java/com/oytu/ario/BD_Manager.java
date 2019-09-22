package com.oytu.ario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BD_Manager extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "bd_ario";
    private static final int VERSAO_BANCO = 2;


    //Definições referentes a tabela alarmaes
    private static final String TABELA_1 = "tb_alarmes";
    private static final String COLUNA_CODIGO_1 = "codigo";
    private static final String COLUNA_HORA_1 = "hora";
    private static final String COLUNA_TOQUE_1 = "toque"; //inteiro pois usa o indice do spinner
    private static final String COLUNA_ATIVADO_1 = "ativado";


    //Definições referentes a tabela Wifis
    private static final String TABELA_2 = "tb_wifis";
    private static final String COLUNA_CODIGO_2 = "codigo";
    private static final String COLUNA_HORA_2 = "hora";
    private static final String COLUNA_HORA_DESAT_2 = "hora_desat";
    private static final String COLUNA_ATIVADO_2 = "ativado";

    //lastKey
    long lastKey = -1;
    public BD_Manager(Context context){
        super(context, NOME_BANCO, null,VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String QUERY = "CREATE TABLE "+TABELA_1+"" +
                "("+
                    COLUNA_CODIGO_1+" INTEGER PRIMARY KEY,"+
                    COLUNA_HORA_1+" TEXT,"+
                    COLUNA_TOQUE_1+" INTEGER,"+
                    COLUNA_ATIVADO_1+"  BOOLEAN) ";

        System.out.println("[QUERY]=> "+QUERY);
        db.execSQL(QUERY);

            QUERY = "CREATE TABLE "+TABELA_2+
                "("+
                COLUNA_CODIGO_2+" INTEGER PRIMARY KEY,"+
                COLUNA_HORA_2+" TEXT,"+
                    COLUNA_HORA_DESAT_2+" TEXT,"+
                COLUNA_ATIVADO_2+"  BOOLEAN)";

        System.out.println("[QUERY]=> "+QUERY);
        db.execSQL(QUERY);
    }

    public void addAlarm(M_Alarme ala){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUNA_HORA_1, ala.getHora());
        values.put(COLUNA_TOQUE_1,ala.getToque());
        values.put(COLUNA_ATIVADO_1,ala.getAtivado());

        this.lastKey = db.insert(TABELA_1,null,values);
        db.close();

    }

    public void addWifi(M_Wifi fiwi){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUNA_HORA_2, fiwi.getHora());
        values.put(COLUNA_HORA_DESAT_2, fiwi.getHoraDesat());
        values.put(COLUNA_ATIVADO_2,fiwi.getAtivado());

        this.lastKey = db.insert(TABELA_2,null,values);
        db.close();
    }

    public List<M_Alarme> getAllAlarms(){
        List<M_Alarme>listaAlarmes = new ArrayList<>();
        String QUERY = "SELECT * FROM " + TABELA_1;
        //SQLiteDatabase db = this.getReadableDatabase(); fjhadufguigdsgfydgkgdsj
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(QUERY,null);
        if(c.moveToFirst()){
            do{
                M_Alarme a = new M_Alarme();
                a.setCodigo(c.getInt(0));
                a.setHora(c.getString(1));
                a.setToque(c.getInt(2));
                a.setAtivado(c.getInt(3));
                listaAlarmes.add(a);
            }while(c.moveToNext());
        }
        db.close();
        return listaAlarmes;
    }

    public List<M_Wifi> getAllWifis(){
        List<M_Wifi>listaWifis = new ArrayList<>();
        String QUERY = "SELECT * FROM " + TABELA_2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(QUERY,null);
        if(c.moveToFirst()){
            do{
                M_Wifi a = new M_Wifi();
                a.setCodigo(c.getInt(0));
                a.setHora(c.getString(1));
                a.setHoraDesat(c.getString(2));
                a.setAtivado(c.getInt(3));
                listaWifis.add(a);
            }while(c.moveToNext());
        }
        db.close();
        return listaWifis;
    }

    public M_Wifi getWifi(int index){
        M_Wifi fiwi = new M_Wifi();
        String QUERY = "SELECT * FROM " + TABELA_2+" WHERE CODIGO = "+index;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(QUERY, null);
        if (c.moveToFirst()){
            fiwi.setCodigo(c.getInt(0));
            fiwi.setHora(c.getString(1));
            fiwi.setHoraDesat(c.getString(2));
            fiwi.setAtivado(c.getInt(3));
            db.close();
            return fiwi;
        }
        return null;
    }

    public M_Alarme getAlarme(int index){
        M_Alarme ala = new M_Alarme();
        String QUERY = "SELECT * FROM " + TABELA_1+" WHERE CODIGO = "+index;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(QUERY, null);
        if (c.moveToFirst()){
            ala.setCodigo(c.getInt(0));
            ala.setHora(c.getString(1));
            ala.setToque(c.getInt(2));
            ala.setAtivado(c.getInt(3));
            db.close();
            return ala;
        }
        return null;
    }


    public void switiAlarme(int index, boolean ativa){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUNA_CODIGO_1, index);
        String SQL = "UPDATE "+TABELA_1+" SET "+COLUNA_ATIVADO_1+" = \'"+(ativa?1:0)+"' WHERE "+COLUNA_CODIGO_1+" = "+index;
        base.execSQL(SQL);
        base.close();
    }

    public void switiWifi(int index, boolean ativa){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUNA_CODIGO_2, index);
        String SQL = "UPDATE "+TABELA_2+" SET "+COLUNA_ATIVADO_2+" = \'"+(ativa?1:0)+"' WHERE "+COLUNA_CODIGO_2+" = "+index;
        base.execSQL(SQL);
        base.close();
    }

    public void excluirAlarm(int index){
        SQLiteDatabase data = this.getWritableDatabase();
        data.delete(TABELA_1, COLUNA_CODIGO_1+"= ?",
                new String[]{Integer.toString(index)});
        data.close();
    }

    public void excluirWifi(int index){
        SQLiteDatabase data = this.getWritableDatabase();
        data.delete(TABELA_2, COLUNA_CODIGO_2+"= ?",
                new String[]{Integer.toString(index)});
        data.close();
    }

    public void atualizarAlarme (M_Alarme ala){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUNA_CODIGO_1,ala.getCodigo());
        values.put(COLUNA_HORA_1,ala.getHora());
        values.put(COLUNA_TOQUE_1,ala.getToque());
        values.put(COLUNA_ATIVADO_1,ala.getAtivado());
        base.update(TABELA_1, values, COLUNA_CODIGO_1+" = ?", new String[]{String.valueOf(ala.getCodigo())});
        base.close();
    }

    public void atualizarWifi (M_Wifi fiwi){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUNA_CODIGO_2,fiwi.getCodigo());
        values.put(COLUNA_HORA_2,fiwi.getHora());
        values.put(COLUNA_HORA_DESAT_2,fiwi.getHoraDesat());
        values.put(COLUNA_ATIVADO_2,fiwi.getAtivado());
        base.update(TABELA_2, values, COLUNA_CODIGO_2+" = ?", new String[]{String.valueOf(fiwi.getCodigo())});
        base.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
