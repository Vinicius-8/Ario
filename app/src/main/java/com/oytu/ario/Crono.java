package com.oytu.ario;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class Crono extends AppCompatActivity {
    Button btn_init;
    private Chronometer ch;
    private long millisecs;
    private boolean paused;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crono);
        setTitle("Cronometro");
        btn_init = (Button)findViewById(R.id.btn_iniciar);
        paused = true;
        ch = (Chronometer) findViewById(R.id.cronus);
        millisecs = 0;
    }
    public void choice(View v){
        if(paused)
            startCrono(v);
        else
            pause(v);
    }
    public void pause(View view){
        millisecs = SystemClock.elapsedRealtime() - ch.getBase();
        ch.stop();
        paused = true;
        btn_init.setText("Iniciar");
    }

    public void startCrono(View view){
        ch.setBase(SystemClock.elapsedRealtime() - millisecs);
        ch.start();
        paused = false;
        btn_init.setText("Pausar");
    }

    public void cancelCrono(View view){
        ch.stop();
        ch.setBase(SystemClock.elapsedRealtime());
        paused = true;
        millisecs = 0;
    }
}
