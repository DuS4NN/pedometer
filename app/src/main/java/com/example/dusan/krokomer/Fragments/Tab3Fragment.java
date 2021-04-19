package com.example.dusan.krokomer.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dusan.krokomer.Database.DBHelper;
import com.example.dusan.krokomer.MainActivity;
import com.example.dusan.krokomer.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tab3Fragment extends Fragment{

    DBHelper DBH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment,container,false);
        DBH = MainActivity.DBH;

        TextView TV_Count = (TextView) view.findViewById(R.id.textView11);
        TextView TV_Record = (TextView) view.findViewById(R.id.textView13);
        TextView TV_Date = (TextView) view.findViewById(R.id.textView15);

        TV_Count.setText(DBH.getCountOfAchievements()+" splnených cieľov");
        String[] r = DBH.getMaxSteps().split(";");
        String Date="";
        Date date = new SimpleDateFormat("dd/MM/yyy").parse(r[0].substring(0,10),new ParsePosition(0));
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        switch (dayOfWeek){
            case "Monday":
                Date="po "+r[0].substring(0,5);
                break;
            case "Tuesday":
                Date="ut "+r[0].substring(0,5);
                break;
            case "Wednesday":
                Date="st "+r[0].substring(0,5);
                break;
            case "Thursday":
                Date="št "+r[0].substring(0,5);
                break;
            case "Friday":
                Date="pi "+r[0].substring(0,5);
                break;
            case "Saturday":
                Date="so "+r[0].substring(0,5);
                break;
            case "Sunday":
                Date="ne "+r[0].substring(0,5);
                break;
        }
        TV_Date.setText(Date);
        TV_Record.setText(r[1]);




        return view;
    }
}
