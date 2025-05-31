package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class XZ_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xz);

        FirebaseApp.initializeApp(this);

        // 获取按钮并设置点击监听
        Button btnConstellationList = findViewById(R.id.chance_1);
        btnConstellationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建跳转到ConstellationListActivity的Intent
                Intent intent = new Intent(XZ_activity.this, ConstellationListActivity.class);
                startActivity(intent);
            }
        });

        Button btnConstellationConsult = findViewById(R.id.chance_2);
        btnConstellationConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建跳转到ConstellationConsultActivity的Intent
                Intent intent = new Intent(XZ_activity.this, ConstellationConsultActivity.class);
                startActivity(intent);
            }
        });

        Button btnConstellationFortuneQuery = findViewById(R.id.chance_3);
        btnConstellationFortuneQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建跳转到ConstellationFortuneQueryActivity的Intent
                Intent intent = new Intent(XZ_activity.this, ConstellationFortuneQueryActivity.class);
                startActivity(intent);
            }
        });

    }
}