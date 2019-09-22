package com.oytu.ario;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Alarme extends AppCompatActivity {

    BD_Manager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarme);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(toolbar!=null){
            toolbar.setTitle("Criar Alarm");
            toolbar.setNavigationIcon(R.drawable.botao_voltar_menu);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        db = new BD_Manager(this);

        //lvContatos = (ListView) findViewById(R.id.list_alarm);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Abrindo ALer", Snackbar.LENGTH_LONG)//fashdoaisouasio
//                        .setAction("Action", null).show();

                Intent intent = new Intent(Alarme.this,CreateAlarm.class);
                startActivity(intent);

                /** ---- cria espera de alarme!
                Intent intent = new Intent("ALARME_DISPARADO");
                PendingIntent p = (PendingIntent) PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                c.add(Calendar.SECOND, 3);

                AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarme.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),p);
                 */
            }
        });

        listarAlarmes();
    }

    public void listarAlarmes(){
        ListView listView = (ListView) findViewById(R.id.list_alarm);

        List<M_Alarme> listaObjetosHoras = db.getAllAlarms();

        ArrayAdapter adapter = new CelulaAlarmeAdapter(this, (ArrayList<M_Alarme>) listaObjetosHoras);

        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        /**
        Intent intent = new Intent("ALARME_DISPARADO");
        PendingIntent p = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarme.cancel(p);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarAlarmes();
    }
}
