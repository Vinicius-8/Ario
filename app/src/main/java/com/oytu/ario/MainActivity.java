package com.oytu.ario;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnAlarm, btnWifi, btnCrono,btnTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAlarm = (Button) findViewById(R.id.btn_alarme);
        btnWifi = (Button) findViewById(R.id.btn_wifi);
        btnCrono = (Button) findViewById(R.id.btn_cronometro);
        btnTimer= (Button) findViewById(R.id.btn_timer);
        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Alarme.class);
                startActivity(intent);
            }
        });

        btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Wifi.class);
                startActivity(intent);
            }
        });

        btnCrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Crono.class);
                startActivity(intent);
                //Snackbar.make(v,"Sneckando!!",Snackbar.LENGTH_LONG).setAction("Action",null).show();
            }
        });

        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Timer.class);
                startActivity(intent);
            }
        });
    }
}
