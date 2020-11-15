package com.delombaertdamien.go4lunch.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.delombaertdamien.go4lunch.ui.activity.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class ConfigureAlarmNotify {

    private static final DateFormat dfTime = new SimpleDateFormat("dd:HH:mm");


    public static void configureAlarmManager(Context activity){
        Log.d("AlarmManager", "Start configuring");
        Intent alarmIntent = new Intent(activity, MyAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        long now = System.currentTimeMillis();
        long tenSecond = 360 * 1000; // 3000 = s

        Calendar calendar = getCalendarAlarm();

        long timeAlarm = calendar.getTimeInMillis();
        //Log.d("ConfigureNotify", "now :" + now + " alarm :" + timeAlarm + " time attempt s : " + ((timeAlarm - now) / 1000));
        long interval = ((timeAlarm - now));
        manager.set(AlarmManager.RTC_WAKEUP, now + tenSecond, pendingIntent);
        //manager.setRepeating(AlarmManager.RTC_WAKEUP, timeAlarm, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        Toast.makeText(activity, "Notify in interval " + (interval / 3000) + " s", Toast.LENGTH_SHORT).show();


    }
    public static Calendar getCalendarAlarm (){
        Calendar cal = Calendar.getInstance();
        Log.d("ConfigureAlarmNotify", "cal now :" + dfTime.format(cal.getTime()));
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set( Calendar.MINUTE, 42);
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.MILLISECOND, 0 );
        if(System.currentTimeMillis() > cal.getTimeInMillis()){
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d("ConfigureAlarmNotify", "cal alarm :" + dfTime.format(cal.getTime()));
        return cal;
    }

}
