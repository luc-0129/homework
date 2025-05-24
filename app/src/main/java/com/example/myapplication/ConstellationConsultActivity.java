package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConstellationConsultActivity extends AppCompatActivity {

    private EditText monthEditText, dateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constellation_consult);

        monthEditText = findViewById(R.id.month);
        dateEditText = findViewById(R.id.date);
        Button showResultButton = findViewById(R.id.button6);

        showResultButton.setOnClickListener(v -> {
            try {
                int month = Integer.parseInt(monthEditText.getText().toString());
                int day = Integer.parseInt(dateEditText.getText().toString());

                String constellation = getConstellation(month, day);
                showResult(constellation);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "请输入有效的月份和日期", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getConstellation(int month, int day) {
        if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) return "白羊座";
        if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) return "金牛座";
        if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) return "双子座";
        if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) return "巨蟹座";
        if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) return "狮子座";
        if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) return "处女座";
        if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) return "天秤座";
        if ((month == 10 && day >= 24) || (month == 11 && day <= 22)) return "天蝎座";
        if ((month == 11 && day >= 23) || (month == 12 && day <= 21)) return "射手座";
        if ((month == 12 && day >= 22) || (month == 1 && day <= 19)) return "摩羯座";
        if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) return "水瓶座";
        if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) return "双鱼座";
        return "未知星座";
    }

    private void showResult(String constellation) {
        Intent intent = new Intent(this, ConstellationResultActivity.class);
        intent.putExtra("CONSTELLATION_RESULT", constellation);
        startActivity(intent);
    }
}