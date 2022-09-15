package com.example.concesionario_jueves;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ClsSqLite extends SQLiteOpenHelper {
    static final String DB = "Concesionario.db";
    static final int version = 1;
    public ClsSqLite(@Nullable Context context) {
        super(context, DB, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table TblAutomovil(placa text primary key," +
                "marca text not null,modelo text not null,valor integer " +
                "not null, estado text not null default 'si')");
        db.execSQL("create table TblFactura(cod_factura text primary key," +
                "fecha text not null,placa text not null,estado text " +
                "not null default 'si',constraint pkfactura " +
                "foreign key (placa) references TblAutomovil (placa))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE TblAutomovil");{
            onCreate(db);
        }
        db.execSQL("DROP TABLE TblFactura");{
            onCreate(db);
        }
    }
}
