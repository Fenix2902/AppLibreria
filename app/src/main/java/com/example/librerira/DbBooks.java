package com.example.librerira;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbBooks extends SQLiteOpenHelper {

    //creacion de tablas
    String tbBook = "Create Table Books(idbook text primary key, name text, coste text, available integer)";

    public DbBooks(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase Db) {
        //ingresar tablas
        Db.execSQL(tbBook);
    }

    @Override
    public void onUpgrade(SQLiteDatabase Db, int i, int i1) {
        Db.execSQL("Drop table Books");
        Db.execSQL(tbBook);//actualiza
    }
}
