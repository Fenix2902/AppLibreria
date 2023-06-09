package com.example.librerira;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class listUsers extends AppCompatActivity {

    ListView listUsers;
    ArrayList<String> arrUsers;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vistausers);
        listUsers = findViewById(R.id.lvUsers);
        btnBack = findViewById(R.id.btnback);
        loadUsers();
        getSupportActionBar().hide();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                //finish();
            }
        });
    }

    private void loadUsers() {
        arrUsers = retrieveUsers();
        //Generar el adaptador que pasara los datos al listview
        ArrayAdapter<String> adpUsers = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arrUsers);
        listUsers.setAdapter(adpUsers);
    }

    private ArrayList<String> retrieveUsers() {
        ArrayList<String> userData = new ArrayList<String>();
        //Cargar los usuarios en el arraylist arrUsers
        DbUsers sohdbuser = new DbUsers(getApplicationContext(), "usuariodb", null, 1);
        SQLiteDatabase dbUserread = sohdbuser.getReadableDatabase();//lectura de la base
        String qAllUsers = "Select idUser, name, email, password, status from Users";
        Cursor cUsers = dbUserread.rawQuery(qAllUsers, null);
        if (cUsers.moveToFirst()) {
            do {
                //Generar un String para almacenar toda la información de cada usuario y guardarlo en el arraylist.
                String mStatus = cUsers.getInt(4) == 1 ? "Activo" : "No Activo";
                String recUser = cUsers.getString(0) + "   " + cUsers.getString(1) + "   " + mStatus;
                userData.add(recUser);
            } while (cUsers.moveToNext());
        }
        dbUserread.close();
        return userData;
    }
}

