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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewUser extends RecyclerView.Adapter<RecycleViewUser.ViewHolder2>{

    private List<String> dates = new ArrayList<>();
    private Context context;

    public RecycleViewUser(Context context, List<String> dates){
        this.dates=dates;
        this.context=context;
    }

    @Override
    public RecycleViewUser.ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_user,parent,false);
        CardView cardView = (CardView) view.findViewById(R.id.cardView2);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            cardView.getBackground().setAlpha(0);
        }else{
            cardView.setBackgroundColor(Color.TRANSPARENT);
        }
        return new ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder2 holder, final int position) {
        holder.textView.setText(dates.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivity.goal = dates.get(position);
                Toast.makeText(context,"Cielový počet nastavený na "+dates.get(position),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder2(View view){
            super(view);
            textView = view.findViewById(R.id.textView35);
        }

    }

}