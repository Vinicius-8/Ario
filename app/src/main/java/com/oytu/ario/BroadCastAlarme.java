package com.oytu.ario;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


public class BroadCastAlarme extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,AlarmeAtivado.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        int index = intent.getExtras().getInt("index_");//ta chegando


        M_Alarme al = new BD_Manager(context).getAlarme(index);
        intent1.putExtra("toque",al.getToque());
        intent1.putExtra("hora",al.getHora());
        context.startActivity(intent1);


    }
    private void toastWarn(Context context, int index, String message){
        if(index ==-1){
            Toast.makeText(context, message + " == -1",Toast.LENGTH_LONG).show();
            Log.i("script", message + " == -1");
        }else{
            Log.i("script",message + " != -1 ["+index+"]");
        }
    }

    public void gerarNotificacao(Context context, Intent intent, CharSequence ticker, CharSequence titulo, CharSequence descricao){
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(titulo);
        builder.setContentText(descricao);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground));
        builder.setContentIntent(p);

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.ic_launcher_foreground, n);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        }
        catch(Exception e){}
    }
}
