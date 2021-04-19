package com.example.dusan.krokomer.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.dusan.krokomer.MainActivity;

import java.util.HashMap;
import java.util.List;


public class MyViewGraph extends View {

    private static int id=0;
    private final List<String> list = MainActivity.DBH.getAllDays();
    private final HashMap<String,String> user = MainActivity.DBH.getUserData();

    public MyViewGraph(Context context) {
        super(context);
    }

    public MyViewGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    protected void onDraw(Canvas canvas) {
        float x;
        String[] listData = list.get(id).split(";");
        int steps = Integer.parseInt(listData[1]);
        if(id!=list.size()-1){
            id++;
        }else{
            id=0;
        }

        long maxSteps=-1;
        if(maxSteps==-1){
            maxSteps = getMaxSteps();
        }

        if(steps>300){
            x = (getHeight()-20)-((steps*(getHeight()-20))/maxSteps)+5;
        }else{
            x = (getHeight()-20)-((steps*(getHeight()-20))/maxSteps)+8;
        }

        Paint paint = new Paint();

        if(steps>Integer.parseInt(user.get("goal"))-1){
            paint.setColor(Color.argb(255,139,194,74));
        }else{
            paint.setColor(Color.argb(255,180,180,180));
        }

        if(steps>0){
            if(steps>300){
                RectF rect = new RectF();
                rect.set(getWidth()/2-15,x ,getWidth()/2+15,getHeight()-20);
                canvas.drawRoundRect(rect,25,25,paint);
                canvas.drawCircle(getWidth()/2,getHeight()-30,15,paint);
                canvas.drawCircle(getWidth()/2, x+10,15,paint);
            }else{
                canvas.drawCircle(getWidth()/2,getHeight()-30,15,paint);
            }
        }
    }

    public long getMaxSteps(){
        String[] steps = MainActivity.DBH.getMaxSteps().split(";");
        long maxSteps = Integer.parseInt(steps[1]);
        if(maxSteps==0) return 1;
        else return maxSteps;
    }

}
