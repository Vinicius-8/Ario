package com.oytu.ario;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

public class CelulaWifiAdapter extends ArrayAdapter<M_Wifi> {
    private Context context;
    private ArrayList<M_Wifi> itens;

    Calendar calendar_atv;
    Calendar calendar_desatv;

    public  CelulaWifiAdapter(Context context, ArrayList<M_Wifi> itens) {
        super(context, R.layout.celula_alarme,itens);
        this.context = context;
        this.itens = itens;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.celula_wifi, parent, false);

        TextView codigo = (TextView) rowView.findViewById(R.id.tv_codigo_celula);
        TextView hora = (TextView) rowView.findViewById(R.id.tv_hora_celula);
        Switch switi = (Switch) rowView.findViewById(R.id.switch_celula);

        final int index = this.itens.get(position).getCodigo();
        codigo.setText(index+"");
        hora.setText(this.itens.get(position).getHora()+" ¬ "+this.itens.get(position).getHoraDesat());
        switi.setChecked((this.itens.get(position).getAtivado() == 1));

        //hora ativar
        String contacHour =  this.itens.get(position).getHora();
        int horaCalendar_atv = Integer.parseInt(contacHour.substring(0,contacHour.indexOf(":")));
        int minuteCalendar_atv = Integer.parseInt(contacHour.substring(contacHour.indexOf(":")+1,contacHour.length()));

        calendar_atv = Calendar.getInstance();
        calendar_atv.set(Calendar.HOUR_OF_DAY,horaCalendar_atv);
        calendar_atv.set(Calendar.MINUTE, minuteCalendar_atv);
        calendar_atv.set(Calendar.SECOND, 0);


        //hora desativar
        String contacHour_des =  this.itens.get(position).getHora();
        int horaCalendar_desatv = Integer.parseInt(contacHour_des.substring(0,contacHour_des.indexOf(":")));
        int minuteCalendar_desatv = Integer.parseInt(contacHour_des.substring(contacHour_des.indexOf(":")+1,contacHour_des.length()));

        calendar_desatv = Calendar.getInstance();
        calendar_desatv.set(Calendar.HOUR_OF_DAY,horaCalendar_desatv);
        calendar_desatv.set(Calendar.MINUTE,minuteCalendar_desatv);
        calendar_desatv.set(Calendar.SECOND,0);


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Index: "+index, Snackbar.LENGTH_LONG).setAction("Action",null).show();
                Intent i = new Intent(getContext(),CreateWifi.class);
                i.putExtra("index",index+"");
                getContext().startActivity(i);
            }
        });


        switi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(getContext(),b?"Wifi ativado":"Wifi desativado",Toast.LENGTH_LONG).show();
                new BD_Manager(getContext()).switiWifi(index, b);
                if(b) {
                    armar(index+50,calendar_atv, true);
                    armar(index+70,calendar_desatv, false);
                }else{
                    cancelAlarm(index+50);
                    cancelAlarm(index+70);
                }
            }
        });
        return rowView;
    }

    private void armar(long intentRequestCode, Calendar c, boolean ativar){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), BroadCastWifi.class);
        intent.putExtra("ativar",ativar);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),new Long(intentRequestCode).intValue(), intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(int requestCode){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), BroadCastWifi.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),requestCode, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.cancel(pendingIntent);

    }

}
