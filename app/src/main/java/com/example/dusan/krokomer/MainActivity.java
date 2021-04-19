package com.example.dusan.krokomer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.dusan.krokomer.Database.DBHelper;
import com.example.dusan.krokomer.Database.Steps;
import com.example.dusan.krokomer.Fragments.SectionPageAdapter;
import com.example.dusan.krokomer.Fragments.Tab1Fragment;
import com.example.dusan.krokomer.Fragments.Tab2Fragment;
import com.example.dusan.krokomer.Fragments.Tab3Fragment;
import com.example.dusan.krokomer.Service.Service;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements  SensorEventListener{

    private SensorManager sManager;
    private Sensor stepCounterSensor;
    private Intent ServiceIntent;
    private ViewPager mViewPager;
    public static DBHelper DBH;
    public static long COUNT_OF_STEPS;
    private Tab1Fragment TAB1;
    public static Tab2Fragment TAB2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Nastavenie databázy
        DBH = new DBHelper(this);
        //Nastavenie fragmentov
        mViewPager = (ViewPager) findViewById(R.id.container);
        TAB1 = new Tab1Fragment();
        TAB2 = new Tab2Fragment();
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Nastavenie senzorov
        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Nastavenie služby
        ServiceIntent = new Intent(MainActivity.this, Service.class);
        if(!isMyServiceRunning(this.getClass())){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                startForegroundService(ServiceIntent);
            }else{
                startService(ServiceIntent);
            }
        }

        HashMap<String,String> map = DBH.getUserData();
        if(Integer.parseInt(map.get("steps"))>COUNT_OF_STEPS){
             DBH.changeSteps(COUNT_OF_STEPS);
        }
        DBH.changeSteps(Integer.parseInt(map.get("steps")));

        //DATA NA PRIDANIE ---------------------
        //pridajData();
    }


    public void pridajData(){
        String[] times = {"00:30","01:00","01:30","02:00","02:30","03:00","03:30","04:00","04:30","05:00","05:30","06:00","06:30","07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00",
                "11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30","23:00","23:30","24:00"};
        for(int i=0; i<48;i++){
            DBH.createNewStep(new Steps(1,"25/04/2018 "+times[i]+":00",(int)(Math.random()*500)+""));
        }
        for(int i=0; i<47;i++){
            DBH.createNewStep(new Steps(1,"26/04/2018 "+times[i]+":00",(int)(Math.random()*500)+""));
        }
        for(int i=0; i<47;i++){
            DBH.createNewStep(new Steps(1,"27/04/2018 "+times[i]+":00",(int)(Math.random()*500)+""));
        }
        for(int i=0; i<47;i++){
            DBH.createNewStep(new Steps(1,"28/04/2018 "+times[i]+":00",(int)(Math.random()*500)+""));
        }

    }

    public void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(TAB1, "SLEDOVANIE");
        adapter.addFragment(TAB2, "TRENDY");
        adapter.addFragment(new Tab3Fragment(), "ODMENY");
        viewPager.setAdapter(adapter);
    }

    public void onClick(View view){
        startActivity(new Intent(MainActivity.this, UserActivity.class));
    }

    @Override
    protected void onDestroy() {
        stopService(ServiceIntent);
        super.onDestroy();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.dusan.krokomer.Service.Service".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) value = (int) values[0];

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            COUNT_OF_STEPS = value;
            TAB1.setTextViews(COUNT_OF_STEPS);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener((SensorEventListener) this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

}
