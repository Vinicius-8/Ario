package com.oytu.ario;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Calendar;

public class CreateWifi extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    EditText hora_edt_wifi,hora_edt_wifi_desat;
    String global_index; //index de um seleccionado
    M_Wifi fiwi; //objeto do tipo wifi para caso possua um index

    Calendar calendar_atv;
    Calendar calendar_desatv;
    int editText; //variavel para decidir qual editextfoi selecionado
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wifi);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(toolbar!=null){
            toolbar.setTitle("Criar Wifi");
            toolbar.setNavigationIcon(R.drawable.botao_voltar_menu);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        hora_edt_wifi = (EditText) findViewById(R.id.edt_hora_wifi);
        hora_edt_wifi_desat = (EditText) findViewById(R.id.edt_hora_wifi_desat);

        calendar_atv = Calendar.getInstance();
        calendar_desatv = Calendar.getInstance();

        fiwi = null;

        editText = -1;


        hora_edt_wifi.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                editText = 0;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "PegaTempo");

            }
        });

        hora_edt_wifi_desat.setOnTouchListener(new View.OnTouchListener() { //ClickLister abre o teclado o que nao é bem vindo
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    editText = 1;
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "PegaTempo");
                }
                return true;
            }
        });


        global_index = getIntent().getStringExtra("index");// pegando o index que pode ter vindo da activity pai
        if(global_index==null) { //bloco que define a hora e os minutos do dia nos edts que não foram criados
            Calendar c = Calendar.getInstance();
            String hora, min;
            if (c.get(Calendar.HOUR_OF_DAY) < 10)
                hora = "0" + c.get(Calendar.HOUR_OF_DAY);
            else
                hora = "" + c.get(Calendar.HOUR_OF_DAY);

            if (c.get(Calendar.MINUTE) < 10)
                min = "0" + c.get(Calendar.MINUTE);
            else
                min = "" + c.get(Calendar.MINUTE);
            hora_edt_wifi.setText(hora+":"+min);
            hora_edt_wifi_desat.setText(hora+":"+min);
        }else{
            //bloco que defini as caracteristica do banco nos edts
            int index = Integer.parseInt(global_index);
            //System.out.println("\t\t\t\t[_______indexRecebido_______]["+index+"]");
            fiwi = new BD_Manager(this).getWifi(index);
            if(fiwi == null)
                Toast.makeText(this,"Ih rapaiz", Toast.LENGTH_LONG).show();
            hora_edt_wifi.setText(fiwi.getHora());
            hora_edt_wifi_desat.setText(fiwi.getHoraDesat());

        }

    }

    private void armar(long intentRequestCode, Calendar c, boolean ativar){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), BroadCastWifi.class);
        intent.putExtra("ativar",ativar);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),new Long(intentRequestCode).intValue(), intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(int requestCode){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), BroadCastWifi.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),requestCode, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.cancel(pendingIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_criar_alarm, menu);
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BD_Manager db = new BD_Manager(this);

        if(global_index!=null){
            fiwi.setCodigo(Integer.parseInt(global_index));
            fiwi.setHora(hora_edt_wifi.getText().toString());
            fiwi.setHoraDesat(hora_edt_wifi_desat.getText().toString());
            fiwi.setAtivado(1);
        }

        switch (item.getItemId()){
            case R.id.item_menu_salvar:
                if(global_index==null) {
                    db.addWifi(new M_Wifi(this.hora_edt_wifi.getText().toString(), this.hora_edt_wifi_desat.getText().toString(), 1));
                    armar(db.lastKey + 50, calendar_atv, true);//ativação  | 50 gap de tabela
                    armar(db.lastKey + 70, calendar_desatv, false);//desativação | 50 gap de tabela + 20 gap de wifi

                }else {
                    db.atualizarWifi(fiwi);

                    cancelAlarm(fiwi.getCodigo()+50);
                    cancelAlarm(fiwi.getCodigo()+70);

                    armar(fiwi.getCodigo()+50, calendar_atv,true);//ativação  | 50 gap de tabela
                    armar(fiwi.getCodigo()+70, calendar_desatv, false);//desativação | 50 gap de tabela + 20 gap de wifi
                }
                finish();
                break;
            case R.id.item_menu_excluir:
                if(global_index!=null){
                    db.excluirWifi(Integer.parseInt(global_index));
                    cancelAlarm(fiwi.getCodigo()+50);
                    cancelAlarm(fiwi.getCodigo()+70);
                }
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Snackbar.make(view,hourOfDay+":"+minute,Snackbar.LENGTH_LONG).setAction("Action",null).show();
        //Toast.makeText(this, hourOfDay+":"+minute, Toast.LENGTH_SHORT).show();
        String hora,min;
        if(hourOfDay<10)
            hora = "0"+hourOfDay;
        else
            hora = ""+hourOfDay;

        if(minute<10)
            min = "0"+minute;
        else
            min = ""+minute;

        //ifs para escolher qual edt foi selecionado (gambiarra msm)
        if(editText == -1)
            return;
        else if(editText == 0) {
            hora_edt_wifi.setText(hora + ":" + min);
            calendar_atv.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar_atv.set(Calendar.MINUTE, minute);
            calendar_atv.set(Calendar.SECOND,0);
        }else if(editText == 1){
            hora_edt_wifi_desat.setText(hora+":"+min);
            calendar_desatv.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar_desatv.set(Calendar.MINUTE, minute);
            calendar_desatv.set(Calendar.SECOND,0);
        }

    }

}
