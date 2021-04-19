package com.example.dusan.krokomer.Views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.dusan.krokomer.MainActivity;

import java.util.HashMap;

public class MyViewTag extends View{
    public MyViewTag(Context context) {
        super(context);
    }

    public MyViewTag(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewTag(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        HashMap<String,String> map = MainActivity.DBH.getUserData();
        String[] data = MainActivity.DBH.getMaxSteps().split(";");
        int max = Integer.parseInt(data[1]);
        if(max==0)max=1;
        int goal = Integer.parseInt(map.get("goal"));
        int y = (getHeight()-20)-((goal*(getHeight()-20))/max);

        Path path = new Path();
        path.moveTo(0,y);
        path.quadTo(getWidth()/2,y,getWidth(),y);
        Paint paint = new Paint();
        paint.setColor(Color.argb(150,155,155,155));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setPathEffect(new DashPathEffect(new float[]{4,4,4,4},0));
        canvas.drawPath(path,paint);

        Paint paint1 = new Paint();
        paint1.setTextSize(40);
        paint1.setColor(Color.argb(255,100,100,120));
        RectF rect = new RectF();
        rect.set(getWidth()-180,y-30,getWidth()-70,y+30);
        canvas.drawRoundRect(rect,10,10,paint1);
        paint1.setColor(Color.WHITE);
        canvas.drawText(map.get("goal"),getWidth()-170,y+15,paint1);

        if(goal>=2000){
            int y2 = (getHeight()-20)-(((goal/2)*(getHeight()-20))/max);

            path.moveTo(0,y2);
            path.quadTo(getWidth()/2,y2,getWidth()-180,y2);
            canvas.drawPath(path,paint);

            path.moveTo(getWidth()-70,y2);
            path.quadTo((getWidth()-70),y2,getWidth(),y2);
            canvas.drawPath(path,paint);


            paint1.setColor(Color.argb(255,250,250,250));
            paint1.setTextSize(30);
            paint1.setColor(Color.argb(255,100,100,120));
            canvas.drawText(goal/2+"",getWidth()-160,(y2+5),paint1);
        }

    }
}
