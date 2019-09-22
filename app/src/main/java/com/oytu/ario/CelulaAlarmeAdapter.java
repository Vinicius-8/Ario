package com.oytu.ario;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Classe resposável por implementar cada tupla do lisView personalizado com suas devidas particula.
 */
public class CelulaAlarmeAdapter extends ArrayAdapter<M_Alarme> {
    private Context context;
    private ArrayList<M_Alarme> itens;
    Calendar calendar;
    //int index;
    public CelulaAlarmeAdapter(Context context, ArrayList<M_Alarme> itens) {
        super(context, R.layout.celula_alarme,itens);
        this.context = context;
        this.itens = itens;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.celula_alarme, parent, false);

        TextView codigo = (TextView) rowView.findViewById(R.id.tv_codigo_celula);
        TextView hora = (TextView) rowView.findViewById(R.id.tv_hora_celula);
        Switch switi = (Switch) rowView.findViewById(R.id.switch_celula);

        final int index = this.itens.get(position).getCodigo();
        codigo.setText(index+"");
        hora.setText(this.itens.get(position).getHora());
        switi.setChecked((this.itens.get(position).getAtivado() == 1));

        System.out.println("POisção: "+position+" == index: "+index);
        //tratamento de hora
        //System.out.println("____-getHOra: "+this.itens.get(position).getHora());

        String contacHour =  this.itens.get(position).getHora();
        int horaCalendar = Integer.parseInt(contacHour.substring(0,contacHour.indexOf(":")));
        int minuteCalendar = Integer.parseInt(contacHour.substring(contacHour.indexOf(":")+1,contacHour.length()));

        //System.out.println("????/////??// o que foi achado! _ h:"+ horaCalendar+"m:"+minuteCalendar);


        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, horaCalendar);
        calendar.set(Calendar.MINUTE, minuteCalendar);
        calendar.set(Calendar.SECOND, 0);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Index: "+index, Snackbar.LENGTH_LONG).setAction("Action",null).show();
                Intent i = new Intent(getContext(),CreateAlarm.class);
                i.putExtra("index",index+"");
                //Toast.makeText(getContext(),"index de num: "+index,Toast.LENGTH_LONG).show();
                getContext().startActivity(i);
            }
        });


        switi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(getContext(),b?"Alarme ativado":"Alarme desativado",Toast.LENGTH_LONG).show();
                new BD_Manager(getContext()).switiAlarme(index, b);
                if(b) {
                    armar2(index);
                }else{
                    cancelAlarm(index);
                }
            }
        });
        return rowView;
    }

    private void armar2(long intentRequestCode){
        int index = new Long(intentRequestCode).intValue();
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), BroadCastAlarme.class);
        toastWarn(index,"armar2Adapter:");
        intent.putExtra("index", index);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),index, intent,PendingIntent.FLAG_CANCEL_CURRENT);
        //Toast.makeText(getContext(),"Armando alarme de id: "+index,Toast.LENGTH_LONG).show();
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(int codeRequest){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), BroadCastAlarme.class);
        toastWarn(codeRequest,"cancelAdapter:");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),codeRequest, intent,PendingIntent.FLAG_CANCEL_CURRENT);
        //Toast.makeText(context,"Parando alarme de id: "+codeRequest,Toast.LENGTH_LONG).show();
        alarmManager.cancel(pendingIntent);
    }

    private void toastWarn(int index, String message){
        if(index == -1){
            Toast.makeText(getContext(), message + " == -1",Toast.LENGTH_LONG).show();
            Log.i("script", message + " == -1");
        }else{
            Log.i("script",message + " = ["+index+"]");
        }
    }
}
