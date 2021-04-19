package com.example.dusan.krokomer.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dusan.krokomer.Database.DBHelper;
import com.example.dusan.krokomer.MainActivity;
import com.example.dusan.krokomer.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class Tab1Fragment extends Fragment{

    private DBHelper DBH;
    private TextView tv_StepCount;
    private TextView tv_StepGoal;
    private TextView tv_Minutes;
    private TextView tv_CountKM;
    private TextView tv_Kcal;
    public static long stepsInDay = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);
        DBH = MainActivity.DBH;
        HashMap<String,String> map = DBH.getUserData();

        tv_StepCount = view.findViewById(R.id.textView4);
        tv_StepGoal = view.findViewById(R.id.textView5);
        tv_CountKM = view.findViewById(R.id.textView8);
        tv_Kcal = view.findViewById(R.id.textView16);
        tv_Minutes = view.findViewById(R.id.textView19);

        tv_StepGoal.setText("/"+map.get("goal"));

        refreshData();
        setTextViews(MainActivity.COUNT_OF_STEPS);

        GraphView graphView = (GraphView) view.findViewById(R.id.graphView);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(getDataPoint());
        series.setColor(Color.parseColor("#8bc24a"));
        series.setSpacing(5);


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"0","6","12","18","24"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView.getGridLabelRenderer().setHumanRounding(false);
        graphView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphView.addSeries(series);
        return view;
    }

    private DataPoint[] getDataPoint(){

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        Date Date = new Date();
        String date = dateFormat.format(Date);

        DataPoint[] dp = new DataPoint[48];
        String[] times = {"00:30","01:00","01:30","02:00","02:30","03:00","03:30","04:00","04:30","05:00","05:30","06:00","06:30","07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00",
                "11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30","23:00","23:30","24:00"};
        double[] points = {0.5 , 1 , 1.5 , 2.0 , 2.5 , 3.0 , 3.5 , 4.0 , 4.5 , 5.0 , 5.5 , 6.0 , 6.5 ,7.0 , 7.5 , 8.0 , 8.5 , 9.0 , 9.5 , 10.0 , 10.5 , 11.0 ,
                11.5,12.0,12.5,13.0,13.5,14.0,14.5,15.0,15.5,16.0,16.5,17.0,17.5,18.0,18.5,19.0,19.5,20.0,20.5,21.0,21.5,22.0,22.5,23.0,23.5,24.0};
        List<HashMap<String,String>> list = DBH.getStepsPerHours(date);
        for(int i = 0; i<list.size();i++){
            HashMap<String,String> map = list.get(i);
            String min = map.get("date").substring(11,16);
            for(int j=0; j< times.length;j++){
                if(min.equals(times[j])){
                    times[j]="-";
                    dp[j]= new DataPoint(points[j],Integer.parseInt(map.get("steps")));
                    break;
                }
            }
        }
        for(int i=0;i<times.length;i++){
            if(!times[i].equals("-")){
                dp[i] = new DataPoint(points[i],0);
                times[i]="-";
            }
        }
        return dp;
    }

    public void setTextViews(long steps){
        if(tv_StepCount==null){
           return;
        }
        HashMap<String,String> map = DBH.getUserData();
        long s = stepsInDay+(steps-Integer.parseInt(map.get("steps")));

        tv_StepCount.setText(s+"");
        double km2 = Math.round((s*0.000762)*100)/100.0;
        tv_CountKM.setText(km2+" km");
        long kcal2 = Math.round(s/15);
        tv_Kcal.setText(kcal2+" kcal");
        tv_Minutes.setText(s/80+" min");

        Log.i("PREPISUJEM STEPS","S: "+MainActivity.COUNT_OF_STEPS+" M: "+map.get("steps")+" D: "+stepsInDay);
    }

    public static void refreshData(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        Date Date = new Date();
        String date = dateFormat.format(Date);
        stepsInDay = MainActivity.DBH.getStepsInDay(date);
    }
}
