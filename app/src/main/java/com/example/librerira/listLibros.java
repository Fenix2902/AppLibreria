package com.example.librerira;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class listLibros extends AppCompatActivity {
    ListView listbooks;
    ArrayList<String>arrbooks;
    Button btnback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vistabooks);
        listbooks.findViewById(R.id.lvlibros);
        btnback.findViewById(R.id.btnback);
        loadbooks();
        getSupportActionBar().hide();

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                //finish();
            }
        });
    }


    private void loadbooks() {
        arrbooks = retrieveBooks();
        //Generar el adaptador que pasara los datos al listView
        ArrayAdapter<String> adpBooks = new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1, arrbooks);
        listbooks.setAdapter(adpBooks);
    }

    private ArrayList<String> retrieveBooks() {
        ArrayList<String> bookData = new ArrayList<>();
        //Cargar los libros en el arrayList arrBooks
        DbUsers sohdbbook = new DbUsers(getApplicationContext(),"librosdb",null,1);
        SQLiteDatabase dbBookread = sohdbbook.getReadableDatabase();
        String qAllbooks = "Select idBook, name, coste, available from Books";
        Cursor cBooks = dbBookread.rawQuery(qAllbooks,null);
        if (cBooks.moveToFirst()){
            do{
                //generar un String para almacenar toda la informacion de cada libro y guardarlo
                String mAvailable = cBooks.getInt(3)==1? "Disponible": "No Disponible";
                String recBook = cBooks.getString(0)+"  "+cBooks.getString(1)+"  "+cBooks.getString(2)+"  "+mAvailable;
                bookData.add(recBook);
            }while (cBooks.moveToNext());
        }
        dbBookread.close();
        return bookData;
    }
}
