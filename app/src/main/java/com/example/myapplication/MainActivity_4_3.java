package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity_4_3 extends AppCompatActivity {

    private TextView scoring1;
    private TextView scoring2;
    private int teams1,teams2;
    private Button btn6, btn5, btn4, btn3, btn2, btn1,reset;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_4_3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoring1 = findViewById(R.id.s1);
        scoring2 = findViewById(R.id.s2);
        btn6 = findViewById(R.id.btn6);
        btn5 = findViewById(R.id.btn5);
        btn4 = findViewById(R.id.btn4);
        btn3 = findViewById(R.id.btn3);
        btn2 = findViewById(R.id.btn2);
        btn1 = findViewById(R.id.btn1);
        reset= findViewById(R.id.reset);
        reset.setOnClickListener((v) -> {
            teams1=0;
            teams2=0;
        });
    }
    public void click(View btn) {
        Log.i(TAG, "click:111");

        int score1 = Integer.parseInt(scoring1.getText().toString());
        int score2 = Integer.parseInt(scoring2.getText().toString());

        if (btn.getId() == R.id.btn4) {
            score2 += 0;
        } else if (btn.getId() == R.id.btn5) {
            score2 += 3;
        } else if (btn.getId() == R.id.btn6) {
            score2 += 2;
        } else if (btn.getId() == R.id.btn3) {
            score1 += 0;
        } else if (btn.getId() == R.id.btn2) {
            score1 += 2;
        } else if (btn.getId() == R.id.btn1) {
            score1 += 3;
        }else if(btn.getId()==R.id.reset){
            score1=0;
            score2=0;
        }

        scoring1.setText(String.valueOf(score1));
        scoring2.setText(String.valueOf(score2));
    }
    @Override
    protected  void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("key1",teams1);
        outState.putInt("key2",teams2);
    }
    @Override
    protected  void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        teams1=savedInstanceState.getInt("key1");
        teams2=savedInstanceState.getInt("key2");
        scoring1.setText(String.valueOf(teams1));
        scoring2.setText(String.valueOf(teams2));
    }
}