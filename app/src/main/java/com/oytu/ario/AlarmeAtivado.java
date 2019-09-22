package com.oytu.ario;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmeAtivado extends AppCompatActivity {
    Ringtone r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarme_ativado);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button button = (Button) findViewById(R.id.btn_ativadoAlarm);


        TextView tv = (TextView) findViewById(R.id.tv_alarmAtivado);
        int toque = getIntent().getExtras().getInt("toque");
        String hora = getIntent().getExtras().getString("hora");

        tv.setText(hora);
        Uri notification;
        if(toque == 0) {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }else {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r.stop();
                finish();
            }
        });

    }

}
