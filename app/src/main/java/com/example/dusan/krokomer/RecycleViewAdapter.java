package com.example.dusan.krokomer;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dusan.krokomer.Views.MyViewGraph;

import java.util.ArrayList;
import java.util.List;


public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{

    private List<String> dates = new ArrayList<>();
    private Context context;
    private CardView cardView;

    public RecycleViewAdapter(Context context, List<String> dates){
        this.dates=dates;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        cardView = (CardView) view.findViewById(R.id.cardView);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            cardView.getBackground().setAlpha(0);
        }else{
            cardView.setBackgroundColor(Color.TRANSPARENT);
        }
        String[] data = dates.get(dates.size()-1).split(";");
        MainActivity.TAB2.setData(data[0],dates.size()-1);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String[] data = dates.get(position).split(";");

        if(data[0].substring(0,2).equals("01")){
            holder.textView.setText(data[0].substring(0,5));
        }else{
            holder.textView.setText(data[0].substring(0,2));
        }
        MyViewGraph myView = new MyViewGraph(context);
        holder.myView = myView;

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.TAB2.setData(data[0], position);
            }
        });
        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.TAB2.setData(data[0], position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView  textView;
        View myView;

        public ViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.textView17);
            myView = view.findViewById(R.id.myview);
        }

    }

}
