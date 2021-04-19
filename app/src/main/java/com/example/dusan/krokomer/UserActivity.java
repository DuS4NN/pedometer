package com.example.dusan.krokomer;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dusan.krokomer.Database.DBHelper;
import com.example.dusan.krokomer.Database.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserActivity extends AppCompatActivity {

    HashMap<String,String> user;
    DBHelper DBH = MainActivity.DBH;
    ArrayList<String> list = new ArrayList<>();
    public static String goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        naplnList();
        user = DBH.getUserData();
        goal = user.get("goal");
        setButton();
        initRecyclerView();
    }

    public void setButton() {
        Button button = (Button) findViewById(R.id.button3);
        button.setText("ULOŽIŤ DENNÝ CIEĽ");
    }

    public void setSteps(View view){
        DBH.changeUser("1",goal);
        finish();
    }

    private void naplnList(){
        int x = 1000;
        for(int i=0; i<29100;i+=100){
            list.add(x+i+"");
        }
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclelistview2);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecycleViewUser adapter = new RecycleViewUser(this,list);
        recyclerView.setAdapter(adapter);
    }

    public void back(View view){
        finish();
    }
}
