package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class ConstellationResultActivity extends AppCompatActivity {

    private static final Map<String, Integer> constellationImages = new HashMap<String, Integer>() {{
        put("白羊座", R.drawable.aries);
        put("金牛座", R.drawable.taurus);
        put("狮子座", R.drawable.leo);
        put("射手座", R.drawable.sagittarius);
        put("水瓶座", R.drawable.aquarius);
        put("处女座", R.drawable.virgo);
        put("双子座", R.drawable.gemini);
        put("摩羯座", R.drawable.capricorn);
        put("天蝎座", R.drawable.scorpio);
        put("天秤座", R.drawable.libra);
        put("双鱼座", R.drawable.pisces);
        put("巨蟹座", R.drawable.cancer);
    }};

    private static final Map<String, String> constellationDates = new HashMap<String, String>() {{
        put("白羊座", "3月21日～4月19日");
        put("金牛座", "4月20日～5月20日");
        put("狮子座", "7月23日～8月22日");
        put("射手座", "11月23日～12月21日");
        put("水瓶座", "1月20日～2月18日");
        put("处女座", "8月23日～9月22日");
        put("双子座", "5月21日～6月21日");
        put("魔羯座", "12月22日～1月19日");
        put("天蝎座", "10月24日～11月22日");
        put("天秤座", "9月23日～10月23日");
        put("双鱼座", "2月19日～3月20日");
        put("巨蟹座", "6月22日～7月22日");

    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constellation_result);

        // 绑定按钮
        Button btnConstellationList = findViewById(R.id.btn_1);

        // 设置点击事件
        btnConstellationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建跳转Intent
                Intent intent = new Intent(ConstellationResultActivity.this, ConstellationListActivity.class);

                // 启动目标Activity
                startActivity(intent);
            }
        });
        // 绑定按钮
        Button btnConstellationFortuneQuery = findViewById(R.id.btn_2);

        // 设置点击事件
        btnConstellationFortuneQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建跳转Intent
                Intent intent = new Intent(ConstellationResultActivity.this, ConstellationFortuneQueryActivity.class);

                // 启动目标Activity
                startActivity(intent);
            }
        });

        String constellation = getIntent().getStringExtra("CONSTELLATION_RESULT");

        TextView title = findViewById(R.id.resultTitle);
        ImageView image = findViewById(R.id.constellationImage);
        TextView date = findViewById(R.id.constellationDate);
        TextView desc = findViewById(R.id.constellationDesc);

        title.setText(constellation);
        date.setText(constellationDates.get(constellation));

        // 设置星座图片
        if (constellationImages.containsKey(constellation)) {
            image.setImageResource(constellationImages.get(constellation));
        }

        // 加载星座描述（可以改为网络请求）
        loadConstellationDescription(constellation, desc);
    }

    private void loadConstellationDescription(String constellation, TextView descView) {
        // 这里可以改为从网络或数据库获取详细描述
        String description = "请点击以下按钮获得" + constellation + "的详细描述\n";
        descView.setText(description);
    }
}