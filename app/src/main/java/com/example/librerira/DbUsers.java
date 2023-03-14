package com.example.librerira;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbUsers extends SQLiteOpenHelper {
    //creacion de tablas

    String tbUser = "Create Table Users(idUser text primary key, name text,email text, password text, status integer)";
    String tbBook = "Create Table Books(idBook text primary key, name text, coste text, available integer)";
    String tbRent = "Create Table Rent(idRent text primary key, idUser text, idBook text, date date)";

    public DbUsers(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase Db) {
        //ingresar tablas
        Db.execSQL(tbUser);
        Db.execSQL(tbBook);
        Db.execSQL(tbRent);

    }

    @Override
    public void onUpgrade(SQLiteDatabase Db, int oldVersion, int newVersion) {
        Db.execSQL("Drop Table Users");
        Db.execSQL("Drop Table Books");
        Db.execSQL("Drop Table Rent");//borra el dato anterior

        Db.execSQL(tbUser);//actualiza//
        Db.execSQL(tbBook);
        Db.execSQL(tbRent);
    }
}
