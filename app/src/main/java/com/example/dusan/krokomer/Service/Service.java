package com.example.dusan.krokomer.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.dusan.krokomer.Database.DBHelper;
import com.example.dusan.krokomer.Database.Steps;
import com.example.dusan.krokomer.Fragments.Tab1Fragment;
import com.example.dusan.krokomer.MainActivity;
import com.example.dusan.krokomer.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Service extends IntentService{

    private DBHelper DBH ;

    public Service(Context context){
        super("Service");
    }

    public Service(){
        super("Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DBH = new DBHelper(getApplicationContext());
        HashMap<String,String> map = DBH.getUserData();
        startForeground(1, new Notification());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try{
            while (true){
                Log.i("SLUZBA BEZI",getClass().getName());
                createNewStep();
                Thread.sleep(300000); //  300000 - 5 minút
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onDestroy() {
        Intent broadcastIntent = new Intent(Service.this, SensorRestartedBroadcastReceiver.class);
        sendBroadcast(broadcastIntent);
    }

    public void createNewStep() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        Date Date = new Date();
        String date = dateFormat.format(Date);



        if (DBH.getLastDate() == null) {
            if (Integer.parseInt(date.substring(14,16)) >= 30) {
                DBH.createNewStep(new Steps(1,date.substring(0, 14) + "30:00","0"));
                DBH.changeSteps(MainActivity.COUNT_OF_STEPS);
                Tab1Fragment.refreshData();
            } else {
                if(date.substring(11,13).equals("00")){
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE,-1);
                    String newdate = dateFormat.format(cal);
                    DBH.createNewStep(new Steps(1, newdate.substring(0,14)+"24:00","0"));
                }else{
                    DBH.createNewStep(new Steps(1,date.substring(0, 14) + "00:00","0"));
                }
                DBH.changeSteps(MainActivity.COUNT_OF_STEPS);
                Tab1Fragment.refreshData();
            }

        }else{
            String lastDate = DBH.getLastDate();
            if(Integer.parseInt(lastDate.substring(14,16))==30){
                if(Integer.parseInt(date.substring(14,16))>=0 && Integer.parseInt(date.substring(14,16))<30){
                    HashMap<String,String> map = DBH.getUserData();
                    if(MainActivity.COUNT_OF_STEPS-Integer.parseInt(map.get("steps"))>0){
                        if(isLast()){
                            DBH.createNewStep(new Steps(1, DBH.getLastDate().substring(0,11)+"24:00:00",MainActivity.COUNT_OF_STEPS-Integer.parseInt(map.get("steps"))+""));
                        }else{
                            DBH.createNewStep(new Steps(1, date.substring(0,14)+"00:00",MainActivity.COUNT_OF_STEPS-Integer.parseInt(map.get("steps"))+""));
                        }
                        DBH.changeSteps(MainActivity.COUNT_OF_STEPS);
                        Tab1Fragment.refreshData();
                    }
                }
            }else{
                if(Integer.parseInt(date.substring(14,16))>=30){
                    HashMap<String,String> map = DBH.getUserData();
                    if(MainActivity.COUNT_OF_STEPS-Integer.parseInt(map.get("steps"))>=0){
                        DBH.createNewStep(new Steps(1, date.substring(0,14)+"30:00",MainActivity.COUNT_OF_STEPS-Integer.parseInt(map.get("steps"))+""));
                        DBH.changeSteps(MainActivity.COUNT_OF_STEPS);
                        Tab1Fragment.refreshData();
                    }
                }
            }
        }

        //Notifikacia
        HashMap<String,String> map = DBH.getUserData();
        long steps = DBH.getStepsInDay(date);
        long s = steps+(MainActivity.COUNT_OF_STEPS-Integer.parseInt(map.get("steps")));
        if(s>Integer.parseInt(map.get("goal")) && !map.get("achievement").substring(0,10).equals(date.substring(0,10))){
            createNotification();
            DBH.changeAchievement(date);
        }
    }

    public boolean isLast(){
        if(DBH.getLastDate().substring(11,16).equals("23:30")){
            return true;
        }

        return false;
    }

    public void createNotification(){
        int NOTIFICATION_ID = 1;
        String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);


            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{50, 50, 50, 50, 50, 50})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Dosiahli ste denný cieľ")
                .setContentText("Gratulujeme! Dosiahli ste svoj denný cieľový počet krokov.");

        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }
}
