package com.example.dusan.krokomer.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dusan.krokomer.Database.DBHelper;
import com.example.dusan.krokomer.MainActivity;
import com.example.dusan.krokomer.Views.MyViewTag;
import com.example.dusan.krokomer.R;
import com.example.dusan.krokomer.RecycleViewAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Tab2Fragment extends Fragment{

    private List<String> dates;
    private DBHelper DBH;
    private View view;
    private TextView datum;
    private TextView kroky;
    private TextView goal;
    private TextView km;
    private TextView kcal;
    private TextView min;
    private HashMap<String,String> map;

    private LinearLayoutManager linearLayoutManager;
    private BarGraphSeries<DataPoint> series;
    private GraphView graphView;
    private int oldPos=-1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);
        this.view = view;
        DBH = MainActivity.DBH;
        map = DBH.getUserData();

        dates = MainActivity.DBH.getAllDays();
        datum = view.findViewById(R.id.textView20);
        kroky = view.findViewById(R.id.textView24);
        goal = view.findViewById(R.id.textView25);
        km = view.findViewById(R.id.textView28);
        kcal = view.findViewById(R.id.textView16);
        min = view.findViewById(R.id.textView29);
        initRecyclerView();

        graphView = (GraphView) view.findViewById(R.id.graphView2);
        series = new BarGraphSeries<>();
        series.setColor(Color.parseColor("#8bc24a"));
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"0","6","12","18","24"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView.getGridLabelRenderer().setHumanRounding(false);
        graphView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphView.addSeries(series);

        return view;
    }

    public void getDataPoint(String date){
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
            }
        }

        series = new BarGraphSeries<>(dp);
        series.setColor(Color.parseColor("#8bc24a"));
        series.setSpacing(5);
        graphView.removeAllSeries();
        graphView.addSeries(series);
    }

    public void setData(String date, int position){
        long steps = DBH.getStepsInDay(date);
        getDataPoint(date);
        datum.setText(getNameDay(date));
        kroky.setText(steps+"");
        km.setText(Math.round((steps*0.000762)*100)/100.0+" km");
        kcal.setText(Math.round(steps/15)+" kcal");
        min.setText(steps/80+" min");
        goal.setText("/"+map.get("goal"));


        if(oldPos!=-1 && oldPos>position){
            linearLayoutManager.scrollToPosition(position+3);
        }else{
            linearLayoutManager.scrollToPosition(position-3);
        }
        oldPos=position;
    }


    private String getNameDay(String aaa){
        Date date = new SimpleDateFormat("dd/MM/yyy").parse(aaa.substring(0,10),new ParsePosition(0));
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        switch (dayOfWeek){
            case "Monday":
                return "po "+aaa.substring(0,10);
            case "Tuesday":
                return "ut "+aaa.substring(0,10);
            case "Wednesday":
                return "st "+aaa.substring(0,10);
            case "Thursday":
                return "št "+aaa.substring(0,10);
            case "Friday":
                return "pi "+aaa.substring(0,10);
            case "Saturday":
                return "so "+aaa.substring(0,10);
            case "Sunday":
                return "ne "+aaa.substring(0,10);
            default:
                return "";
        }
    }

    private void initRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclelistview);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecycleViewAdapter adapter = new RecycleViewAdapter(getContext(), dates);
        recyclerView.setAdapter(adapter);
    }
}
