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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class CreateAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener  {

    EditText hora_edt;
    Spinner spinner_alarm;
    String global_index;
    M_Alarme ala;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner_alarm = (Spinner) findViewById(R.id.spinner_alarm);
        hora_edt = (EditText) findViewById(R.id.edt_hora);
        ala = null;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.toques,R.layout.my_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_alarm.setAdapter(adapter);

        calendar = Calendar.getInstance();

        global_index = getIntent().getStringExtra("index");

        if(global_index==null) { //carrega os dados do sys
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
            hora_edt.setText(hora+":"+min);
        }else{ //carrega os dados do banco

            int index = Integer.parseInt(global_index);
            ala = new BD_Manager(this).getAlarme(index);
            if(ala == null)
                Toast.makeText(this,"Ih rapaiz",Toast.LENGTH_LONG).show();
            hora_edt.setText(ala.getHora());
            Spinner s = (Spinner) findViewById(R.id.spinner_alarm);
            s.setSelection(ala.getToque(),true);

        }


        hora_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "PegaTempo");

            }
        });

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



    }

    /** Define no EditText o tempo definido no timePicker, o listner do hora_edt apenas abre o
     *  timePicker o resto é definido aqui0
     */

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //minute = minute+1;
        //ativado quando ok no timePicker

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
        hora_edt.setText(hora+":"+min);

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
    }


    //metodo para armar o alarme
    private void armar(){
        Intent intent = new Intent("DISPARAR");
        PendingIntent p = PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 8);

        AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarme.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),p);
    }

    private void armar2(long intentRequestCode){
        int index = new Long(intentRequestCode).intValue();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), BroadCastAlarme.class);
        intent.putExtra("index_", index);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),index, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(int codeRequest){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), BroadCastAlarme.class);
        toastWarn(codeRequest,"codeRequest");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),codeRequest, intent,PendingIntent.FLAG_CANCEL_CURRENT);

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
            ala.setCodigo(Integer.parseInt(global_index));
            ala.setHora(hora_edt.getText().toString());
            ala.setToque(spinner_alarm.getSelectedItemPosition());
            ala.setAtivado(1);
        }

        switch (item.getItemId()){
            case R.id.item_menu_salvar:
                if(global_index==null) {
                    db.addAlarm(new M_Alarme(this.hora_edt.getText().toString(), this.spinner_alarm.getSelectedItemPosition(), 1));
                    armar2(db.lastKey);
                    toastWarn(new Long(db.lastKey).intValue(), "LastKeyAddAlarm ");
                }else{
                    db.atualizarAlarme(ala);
                    cancelAlarm(ala.getCodigo());
                    armar2(new Long(ala.getCodigo()));

                }
                Toast.makeText(getApplicationContext(), "Alarme Salvo!", Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.item_menu_excluir:
                if(global_index!=null) {
                    db.excluirAlarm(Integer.parseInt(global_index));
                    cancelAlarm(ala.getCodigo());
                }
                Toast.makeText(getApplicationContext(), "Alarme Excluido!", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
        return true;
    }

    private void toastWarn(int index, String message){
        if(index ==-1){
            Toast.makeText(getBaseContext(), message + " == -1",Toast.LENGTH_LONG).show();
            Log.i("script", message + " == -1");
        }else{
            Log.i("script",message + " = ["+index+"]");
        }
    }

}
