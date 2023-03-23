package com.example.librerira;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText idusuario, nomusuario, email, password;
    RadioButton activo, noactivo;
    Button crear, editar, buscar, borrar, listar, libros, renta;

    String oldidUser = "";

    //instanciar la clase DbUsers para los diferentes botones (crud)

    DbUsers Usuarios = new DbUsers(this, "usuariodb", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //Referencia los elementos instanciados

        idusuario = findViewById(R.id.etidusuario);
        nomusuario = findViewById(R.id.etnombreusuario);
        email = findViewById(R.id.etemail);
        password = findViewById(R.id.etpassword);

        activo = findViewById(R.id.rbactivo);
        noactivo = findViewById(R.id.rbnoactivo);

        crear = findViewById(R.id.btncrear);
        editar = findViewById(R.id.btneditar);
        buscar = findViewById(R.id.btnbuscar);
        borrar = findViewById(R.id.btnborrar);
        listar = findViewById(R.id.btnlist);
        listar.setEnabled(false);

        libros = findViewById(R.id.btnlibros);
        renta = findViewById(R.id.btnRenta);

        //Evento crear usuarios

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!idusuario.getText().toString().isEmpty() && !nomusuario.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    //Buscar la identificacion en la DB
                    SQLiteDatabase userRead = Usuarios.getReadableDatabase();
                    String query = "Select idUser From Users Where idUser ='" + idusuario.getText().toString() + "'";
                    Cursor cUser = userRead.rawQuery(query, null);
                    if (!cUser.moveToFirst()) {//si no encuentra el id del usuario

                        //Instanciar clase SQLiteDatabase como escritura
                        SQLiteDatabase sdUser = Usuarios.getWritableDatabase();

                        //Contenedor de Valores

                        ContentValues cvUser = new ContentValues();
                        cvUser.put("idUser", idusuario.getText().toString());
                        cvUser.put("name", nomusuario.getText().toString());
                        cvUser.put("email", email.getText().toString());
                        cvUser.put("password", password.getText().toString());
                        cvUser.put("status", activo.isChecked() ? 1 : 0);
                        sdUser.insert("Users", null, cvUser);
                        sdUser.close();
                        limpiar();

                        Toast.makeText(getApplicationContext(), "Usuario guardado correctamente", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario ya registrado,intenta con otro número de indentificación..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese todos los datos del formulario..", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Evento para Editar

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase sdUsuarios = Usuarios.getWritableDatabase();
                if (idusuario.getText().toString().equals(oldidUser)){
                    sdUsuarios.execSQL("Update Users set name ='"+nomusuario.getText().toString()+"',email= '"+email.getText().toString()+"',password ='"+password.getText().toString()+"',status='"+(activo.isChecked()? 1:0)+"'where idUser= '"+oldidUser+"'");
                    Toast.makeText(getApplicationContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    SQLiteDatabase sdUsuariosread = Usuarios.getReadableDatabase();
                    String query = "Select idUser From Users Where idUser ='"+idusuario.getText().toString()+"'";
                    Cursor cUser = sdUsuariosread.rawQuery(query,null);
                    if (!cUser.moveToFirst()){
                        sdUsuarios.execSQL("Update Users set name ='"+nomusuario.getText().toString()+"',email= '"+email.getText().toString()+"',password ='"+password.getText().toString()+"',status='"+(activo.isChecked()? 1:0)+"'where idUser= '"+oldidUser+"'");
                        Toast.makeText(getApplicationContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Este Email ya esta asignado a otro usuario, intente con otro email...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Evento Click para buscar

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUser(idusuario.getText().toString());
            }

            private Cursor findUser(String fidUser) {
                SQLiteDatabase dbsearchUser = Usuarios.getReadableDatabase();
                String sqlSearch = "Select idUser, name, email, status From Users Where idUser ='"+fidUser+"'";
                Cursor cUser = dbsearchUser.rawQuery(sqlSearch,null);
                if (cUser.moveToFirst()){
                    dbsearchUser.close();
                    return cUser;
                }else {
                    dbsearchUser.close();
                    return cUser;
                }
            }

            private void searchUser(String sidUsuario) {
                Cursor cUser =findUser(sidUsuario);
                if (findUser(sidUsuario).moveToFirst()){
                    //Mostrar los datos del usuario en pantalla
                    oldidUser=idusuario.getText().toString();
                    nomusuario.setText(cUser.getString(1));
                    email.setText(cUser.getString(2));
                    if (cUser.getInt(3)==1){
                        activo.setChecked(true);
                        listar.setEnabled(true);
                    }else {
                        noactivo.setChecked(true);
                        listar.setEnabled(false);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "El ID del usuario no fue encontrado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Evento para borrar

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nomusuario.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                    AlertDialog.Builder alertDialogBuilder =new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setMessage("Deseas Eliminar el Contacto");
                    alertDialogBuilder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            SQLiteDatabase obUser = Usuarios.getWritableDatabase();
                            obUser.execSQL("Delete From Users Where idUser='"+idusuario.getText().toString()+"'");
                            Toast.makeText(getApplicationContext(), "Contacto Eliminado correctamente....", Toast.LENGTH_SHORT).show();
                            limpiar();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else {
                    Toast.makeText(getApplicationContext(),"Debes tener todos los datos diligenciados", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Evento Click para Listado de Usuarios
        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verificar si el Usuario tiene Rol de administrador
                if (activo.isChecked()){
                    //pasar a la actividad que muestra los usuarios
                    startActivity(new Intent(getApplicationContext(),listUsers.class));//permite ir a otra pagina
                }
                else{
                    Toast.makeText(getApplicationContext(),"El ususario activo no tiene privilegios para este informe",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Evento Click para Registro libros

        libros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verificar si el Usuario tiene Rol de administrador
                if (activo.isChecked() && !idusuario.getText().toString().isEmpty()){
                    //pasar a la actividad que muestra los usuarios
                    /*Intent intent = new Intent(MainActivity.this, MainActivity_books.class);
                    startActivity(intent);*/
                    startActivity(new Intent(getApplicationContext(),MainActivity_books.class));//permite ir a otra pagina
                }
                else{
                    Toast.makeText(getApplicationContext(),"El ususario activo no tiene privilegios para este informe",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Evento limpiar campos
    private void limpiar() {
            idusuario.setText("");
            nomusuario.setText("");
            email.setText("");
            password.setText("");
    }
}