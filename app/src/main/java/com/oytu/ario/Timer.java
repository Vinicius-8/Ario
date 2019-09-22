package com.oytu.ario;

import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oytu.ario.viewTimer.MyTimePickerDialog;
import com.oytu.ario.viewTimer.TimePicker;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Timer extends AppCompatActivity {
    private TextView  tv_hour,tv_second,tv_minute;
    private Button btn_iniTimer;
    private LinearLayout linear;
    private Ringtone r;
    boolean isRunning;
    long tempo;

    //popup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(toolbar!=null){
            toolbar.setTitle("Timer");
            toolbar.setNavigationIcon(R.drawable.botao_voltar_menu);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        isRunning = false;
        linear = (LinearLayout) findViewById(R.id.timer_linear);
        tv_hour = (TextView) findViewById(R.id.timer_hours);
        tv_minute = (TextView) findViewById(R.id.timer_minutes);
        tv_second = (TextView) findViewById(R.id.timer_seconds);

        tempo = 0;
        btn_iniTimer = (Button) findViewById(R.id.btn_iniTimer);


        //toque
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        this.r = RingtoneManager.getRingtone(getApplicationContext(), notification);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        btn_iniTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if(tempo == 0 && !isRunning)
                    return;
                else if(isRunning)
                    finish();
                isRunning = true;
                btn_iniTimer.setText("Cancelar");
                new CountDownTimer(tempo, 1000){

                    @Override
                    public void onTick(long millisUntilFinished) {
                        /*            converting the milliseconds into days, hours, minutes and seconds and displaying it in textviews             */

                        //days.setText(TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))+"");
                        long temp = (TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)));
                        String aux = temp<10?"0"+temp:""+temp;
                        tv_hour.setText(aux);

                        temp = (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)));
                        aux = temp<10?"0"+temp:""+temp;
                        tv_minute.setText(aux);

                        temp = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                        aux = temp<10?"0"+temp:""+temp;
                        tv_second.setText(aux);
                    }

                    @Override

                    public void onFinish() {
                        /*            clearing all fields and displaying countdown finished message             */

                        //days.setText("Count down completed");
                        tv_hour.setText("00");
                        tv_minute.setText("00");
                        tv_second.setText("00");
                        btn_iniTimer.setText("iniciar");
                        isRunning = false;
                        tempo = 0;
                        try {
                            r.play();
                            showPopUp();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }.start();
        }
        });


        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker(view);
            }
        });


    //popup


        ///fim oncreate
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        r.stop();
    }

    public void showPicker(View v){
        Calendar now = Calendar.getInstance();
        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                // TODO Auto-generated method stub
                tv_hour.setText( (hourOfDay<10? "0"+hourOfDay:hourOfDay).toString());
                tv_minute.setText( (minute<10? "0"+minute:minute).toString());
                tv_second.setText( (seconds<10? "0"+seconds:seconds).toString());

                System.out.println("hourOfDay: "+hourOfDay+"| minute: "+minute+"| seconds: "+seconds);

                tempo = (hourOfDay * 3600000)+(minute*60000)+(seconds*1000);///////issonaumfunciona

            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
        mTimePicker.show();
    }

    public void showPopUp(){
        AlertDialog.Builder window = new AlertDialog.Builder(Timer.this);
        window.setTitle("Tempo esgotado!");
        LayoutInflater inflater  = this.getLayoutInflater();
        View pop = inflater.inflate(R.layout.popup_window, null);
        window.setView(pop);
        window.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                r.stop();
            }
        });
        window.show();
    }

}

