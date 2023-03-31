package com.example.librerira;

import android.content.ContentValues;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_books extends AppCompatActivity {

    EditText idlibro, libro, costo;
    RadioButton disponible, nodisponible;
    Button crear, editar, buscar, borrar, listar;

    String oldidlibro = " ";

    //instanciar la clase DbUsers para los diferentes botones (crud)

    DbBooks books = new DbBooks(this, "bookdb",null,1);

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libros);
        getSupportActionBar().hide();

        //referenciar elementos instanciados

        idlibro =findViewById(R.id.teidlibro);
        libro = findViewById(R.id.etnomlibro);
        costo = findViewById(R.id.etcosto);

        disponible = findViewById(R.id.rbdisponible);
        nodisponible = findViewById(R.id.rbnodisponible);

        crear = findViewById(R.id.btncrear);
        editar = findViewById(R.id.btneditar);
        buscar = findViewById(R.id.btnbuscar);
        borrar = findViewById(R.id.btnborrar);
        listar = findViewById(R.id.btnlist);
        listar.setEnabled(false);


        //evento de crear libros

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!idlibro.getText().toString().isEmpty() && !libro.getText().toString().isEmpty() && !costo.getText().toString().isEmpty()) {
                    //Buscar la identificacion en la DB
                    SQLiteDatabase userRead = books.getReadableDatabase();
                    String query = "Select idBook From Books Where idBook ='" + idlibro.getText().toString() + "'";
                    Cursor cBook = userRead.rawQuery(query, null);
                    if (!cBook.moveToFirst()) {//si no encuentra el id del usuario

                        //Instanciar clase SQLiteDatabase como escritura
                        SQLiteDatabase sdbook = books.getWritableDatabase();

                        //Contenedor de Valores

                        ContentValues cvBook = new ContentValues();
                        cvBook.put("idBook", idlibro.getText().toString());
                        cvBook.put("name", libro.getText().toString());
                        cvBook.put("coste", costo.getText().toString());
                        cvBook.put("available", disponible.isChecked() ? 1 : 0);
                        sdbook.insert("Books", null, cvBook);
                        sdbook.close();
                        limpiar();

                        Toast.makeText(getApplicationContext(), "Libro guardado correctamente", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "El libro ya esta registrado,intenta con otro número de identificación..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese todos los datos del formulario..", Toast.LENGTH_LONG).show();
                }
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase sdBook = books.getWritableDatabase();
                if (idlibro.getText().toString().equals(oldidlibro)){
                    sdBook.execSQL("Update Books set name ='"+libro.getText().toString()+"',coste= '"+costo.getText().toString()+"',avalaible='"+(disponible.isChecked()? 1:0)+"'where idbook= '"+ oldidlibro +"'");
                    Toast.makeText(getApplicationContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    SQLiteDatabase sdBookread = books.getReadableDatabase();
                    String query = "Select idBook From Books Where idBook ='" + idlibro.getText().toString() + "'";
                    Cursor cBook = sdBookread.rawQuery(query,null);
                    if (!cBook.moveToFirst()){
                        sdBook.execSQL("Update Books set name ='"+libro.getText().toString()+"',coste= '"+costo.getText().toString()+"',avalaible='"+(disponible.isChecked()? 1:0)+"'where idbook= '"+ oldidlibro +"'");
                        Toast.makeText(getApplicationContext(), "Libro actualizado correctamente", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Este Libro ya esta asignado a otro usuario, intente con otro email...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBook(idlibro.getText().toString());
            }

            private Cursor findBook(String fidBook) {
                SQLiteDatabase dbsearchBook = books.getReadableDatabase();
                String sqlSearch = "Select idBook, name, coste, available From Books Where idbook ='"+fidBook+"'";
                Cursor cBook = dbsearchBook.rawQuery(sqlSearch,null);
                if (cBook.moveToFirst()){
                    dbsearchBook.close();
                    return cBook;
                }else {
                    dbsearchBook.close();
                    return cBook;
                }
            }

            private void searchBook(String sidBook) {
                Cursor cBook =findBook(sidBook);
                if (findBook(sidBook).moveToFirst()){
                    //Mostrar los datos del usuario en pantalla
                    oldidlibro = idlibro.getText().toString();
                    libro.setText(cBook.getString(1));
                    costo.setText(cBook.getString(2));
                    if (cBook.getInt(3)==1){
                        disponible.setChecked(true);
                        listar.setEnabled(true);
                    }else {
                        nodisponible.setChecked(true);
                        listar.setEnabled(false);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "El ID del libro no fue encontrado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Evento Click para Listado de Libros
        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verificar si el Libro esta disponible
                if (disponible.isChecked()){
                    //pasar a la actividad que muestra los libros
                    startActivity(new Intent(getApplicationContext(),listLibros.class));//permite ir a otra pagina
                }
                else{
                    Toast.makeText(getApplicationContext(),"El libro no esta disponible",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    //Evento limpiar campos
    private void limpiar() {
        idlibro.setText("");
        libro.setText("");
        costo.setText("");
    }
}
