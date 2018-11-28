package alarmcerdas.feri_setyo.com.alarmcerdas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button setAlarm,btn_reset;
    TimePicker tp_time;
    TextView tv_display;
    String hasil;
    Context context = this;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_display = (TextView) findViewById(R.id.tv_display);
        tp_time = (TimePicker) findViewById(R.id.tp_time);
        setAlarm = (Button) findViewById(R.id.setAlarm);
        btn_reset = (Button) findViewById(R.id.btn_reset);

        registerReceiver(broadcastReceiver,new IntentFilter("AlarmUp"));

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                if (Build.VERSION.SDK_INT >= 23) {

                    calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            tp_time.getHour(),
                            tp_time.getMinute(),
                            0
                    );


                } else {
                    calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            tp_time.getCurrentHour(),
                            tp_time.getCurrentMinute(),
                            0
                    );
                }


                setAlarm(calendar.getTimeInMillis(), calendar);
            }


            private void setAlarm(long timeInMillis, Calendar c) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(MainActivity.this, AlarmAdapter.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                alarmManager.setRepeating(AlarmManager.RTC, timeInMillis, 1000*60*15, pendingIntent);

                Toast.makeText(MainActivity.this, "Alarm Set", Toast.LENGTH_SHORT).show();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int ampm = c.get(Calendar.AM_PM);
                String day = "";
                if (ampm == Calendar.AM) {
                    day = "AM";
                } else if (ampm == Calendar.PM) {
                    day = "PM";
                }
                String timeText = "Alarm set for: ";
                timeText += hour + ": " + minute + " " + day;
                tv_display.setText(timeText);
            }
        });


        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(MainActivity.this, AlarmAdapter.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                alarmManager.cancel(pendingIntent);

                tv_display.setText("Alarm not set");
            }
        });



    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pertanyaan();
        }
    };

    public void pertanyaan(){
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.dududu);
        mediaPlayer.start();

        Random rdm=new Random();
        int angka1=rdm.nextInt(100);
        int angka2=rdm.nextInt(100);
        this.hasil=angka1+angka2+"";

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        final View dialogview = this.inflater.inflate(R.layout.formdialog, null);

        final EditText input_a =(EditText) dialogview.findViewById(R.id.angka);

        final AlertDialog builder=new AlertDialog.Builder(context)
                .setTitle("Kuis Aritmatika")
                .setMessage("Berapa hasil "+angka1+" + "+angka2)
                .setCancelable(false)
                .setPositiveButton("submit",null)
                .setView(dialogview)
                .show();

        Button positiveButton= builder.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_a.getText().toString().equals(hasil)){
                    btn_reset.callOnClick();
                    mediaPlayer.stop();
                    Toast.makeText(context, "jawaban benar, alarm berhenti" ,Toast.LENGTH_SHORT).show();
                    builder.dismiss();
                }else{
                    Toast.makeText(context,"jawaban salah",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}
