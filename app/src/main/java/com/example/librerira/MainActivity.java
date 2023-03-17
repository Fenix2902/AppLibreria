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

public class MainActivity extends AppCompatActivity {

    EditText idusuario, nomusuario, email, password;
    RadioButton activo, noactivo;
    Button crear, editar, buscar, borrar, listar;

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

        //Evento crear

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
                        cvUser.put("status", noactivo.isChecked() ? 0 : 1);
                        sdUser.insert("Users", null, cvUser);
                        sdUser.close();
                        idusuario.setText("");
                        nomusuario.setText("");
                        email.setText("");
                        password.setText("");
                        Toast.makeText(getApplicationContext(), "Usuario guardado correctamente", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario ya registrado,intenta con otro número de indentificación..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese todos los datos del formulario..", Toast.LENGTH_LONG).show();
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

    }
}