package com.oytu.ario;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class Wifi extends AppCompatActivity {
    BD_Manager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new BD_Manager(this);
        if(toolbar!=null){
            toolbar.setTitle("Wifi");
            toolbar.setNavigationIcon(R.drawable.botao_voltar_menu);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        listarAlarmes();
        FloatingActionButton fab = findViewById(R.id.fab_2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Wifi.this, CreateWifi.class);
                startActivity(intent);

            }
        });
    }

    public void listarAlarmes(){
        ListView listView = (ListView) findViewById(R.id.list_wifi);

        List<M_Wifi> listaObjetosHoras = db.getAllWifis();

        ArrayAdapter adapter = new  CelulaWifiAdapter(this, (ArrayList<M_Wifi>) listaObjetosHoras);

       listView.setAdapter(adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        listarAlarmes();
    }
}
