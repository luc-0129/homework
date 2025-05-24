package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class ConstellationDetailActivity extends AppCompatActivity {
    private static final String TAG = "ConstellationDetail";
    private TextView tvTitle, tvDesc1, tvDesc2, tvDesc3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constellation_detail);

        // 初始化视图
        tvTitle = findViewById(R.id.tvTitle);
        tvDesc1 = findViewById(R.id.tvDesc1);
        tvDesc2 = findViewById(R.id.tvDesc2);
        tvDesc3 = findViewById(R.id.tvDesc3);

        // 获取传递的星座名称
        String constellationName = getIntent().getStringExtra("constellationName");
        tvTitle.setText(constellationName);

        // 根据星座名称加载不同数据
        loadConstellationData(constellationName);
    }

    private void loadConstellationData(String name) {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://www.ip138.com/xingzuo/")
                        .timeout(10000)
                        .get();

                // 解析三种信息
                String desc1 = parseGuardianInfo(doc, name);      // 守护神信息
                String desc2 = parseCharacterInfo(doc, name);     // 性格特征
                String desc3 = parseAstroInfo(doc, name);         // 星座花语

                runOnUiThread(() -> {
                    tvDesc1.setText(desc1);
                    tvDesc2.setText(desc2);
                    tvDesc3.setText(desc3);
                });

            } catch (IOException e) {
                Log.e(TAG, "加载详情失败", e);
                runOnUiThread(() -> Toast.makeText(this,
                        "加载详情失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private String parseGuardianInfo(Document doc, String name) {
        // 解析守护神信息（示例：白羊座：马鲁斯）
        Element dt = doc.select("dt:containsOwn(" + name + ")").first();
        if (dt != null) {
            return dt.text() + "\n" + dt.nextElementSibling().text();
        }
        return "暂无守护神信息";
    }

    private String parseCharacterInfo(Document doc, String name) {
        // 解析性格特征（示例：白羊座说话很直接...）
        Element dt = doc.select("dt:containsOwn(" + name + ")").get(1); // 第二个dt
        if (dt != null) {
            return dt.text() + "\n" + dt.nextElementSibling().text() + "\n" +
                    dt.parent().nextElementSibling().text();
        }
        return "暂无性格特征信息";
    }

    private String parseAstroInfo(Document doc, String name) {
        // 解析星座花语（示例：守护星：火星...）
        Element dt = doc.select("dt:containsOwn(" + name + ")").last();
        if (dt != null) {
            StringBuilder sb = new StringBuilder();
            Elements ps = dt.nextElementSibling().select("p");
            for (Element p : ps) {
                sb.append(p.text()).append("\n");
            }
            return sb.toString();
        }
        return "暂无星座花语信息";
    }
}