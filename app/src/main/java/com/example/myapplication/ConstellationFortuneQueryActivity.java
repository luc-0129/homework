package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConstellationFortuneQueryActivity extends AppCompatActivity {

    private AutoCompleteTextView constellationSpinner;
    private AutoCompleteTextView durationSpinner;
    private Button btnQuery;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constellation_fortune_query);

        // 初始化视图
        constellationSpinner = findViewById(R.id.constellation_spinner);
        durationSpinner = findViewById(R.id.duration_spinner);
        btnQuery = findViewById(R.id.btn_query);


        // 设置星座下拉列表
        String[] constellations = getResources().getStringArray(R.array.constellations);
        ArrayAdapter<String> constellationAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_item, constellations);
        constellationSpinner.setAdapter(constellationAdapter);

        // 设置运势时长下拉列表
        String[] durations = getResources().getStringArray(R.array.fortune_durations);
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_item, durations);
        durationSpinner.setAdapter(durationAdapter);

        // 查询按钮点击事件
        btnQuery.setOnClickListener(v -> {
            String selectedConstellation = constellationSpinner.getText().toString();
            String selectedDuration = durationSpinner.getText().toString();

            if(selectedConstellation.isEmpty() || selectedDuration.isEmpty()) {
                Toast.makeText(this, "请完整选择星座和运势时长", Toast.LENGTH_SHORT).show();
                return;
            }

            // 执行查询逻辑
            queryFortune(selectedConstellation, selectedDuration);
        });
    }

    private void queryFortune(String constellation, String duration) {
        // 显示加载状态
        btnQuery.setText("查询中...");
        btnQuery.setEnabled(false);

        // 模拟网络请求延迟
        new Handler().postDelayed(() -> {

            displayResult(constellation, duration);

            btnQuery.setText("查询运势");
            btnQuery.setEnabled(true);
        }, 1500);
    }

    private void displayResult(String constellation, String duration) {
        // 跳转到结果页
        Intent intent = new Intent(this, ConstellationFortuneResultActivity.class);
        intent.putExtra("CONSTELLATION", constellation);
        intent.putExtra("DURATION", duration);
        startActivity(intent);
    }
}