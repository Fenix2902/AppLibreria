package com.example.librerira;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_books extends AppCompatActivity {

    EditText idlibro, libro, costo;
    RadioButton disponible, nodisponible;
    Button crear, editar, buscar, borrar, listar;

    //instanciar la clase DbUsers para los diferentes botones (crud)

    DbBooks books = new DbBooks(this, "bookdb",null,1);

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libros);
        getSupportActionBar().hide();

        //referenciar elementos instanciados

        idlibro.findViewById(R.id.etidlibro);
        libro.findViewById(R.id.etlibro);
        costo.findViewById(R.id.etcosto);

        disponible.findViewById(R.id.rbdisponible);
        nodisponible.findViewById(R.id.rbnodisponible);

        crear.findViewById(R.id.btncrear);
        editar.findViewById(R.id.btneditar);
        buscar.findViewById(R.id.btnbuscar);
        borrar.findViewById(R.id.btnborrar);
        listar.findViewById(R.id.btnlist);
        listar.setEnabled(false);

        //evento de crear libros

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!idlibro.getText().toString().isEmpty() && !libro.getText().toString().isEmpty() && !costo.getText().toString().isEmpty()) {
                    //Buscar la identificacion en la DB
                    SQLiteDatabase userRead = books.getReadableDatabase();
                    String query = "Select idBook From Books Where idBook ='" + idlibro.getText().toString() + "'";
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

    }
}
