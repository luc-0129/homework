package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConstellationFortuneResultActivity extends AppCompatActivity {

    private static final String TAG = "FortuneResult";
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constellation_fortune_result);

        tvResult = findViewById(R.id.tv_fortune_result);

        String constellation = getIntent().getStringExtra("CONSTELLATION");
        String duration = getIntent().getStringExtra("DURATION");

        setTitle(constellation + " " + duration + "运势");
        fetchFortuneData(constellation, duration);
    }

    private void fetchFortuneData(String constellation, String duration) {
        new Thread(() -> {
            try {
                String constellationPath = convertConstellationToPath(constellation);
                String durationPath = convertDurationToPath(duration);

                String url = "https://www.1212.com/luck/" + constellationPath + "/" + durationPath + ".html";
                Log.d(TAG, "Fetching URL: " + url);

                Document doc = Jsoup.connect(url)
                        .timeout(15000)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .get();

                StringBuilder resultBuilder = new StringBuilder();

                // 提取综合运势
                Element summaryElement = doc.selectFirst("div.luck-detail-text h3.title:contains(综合) + div.text");
                if (summaryElement != null) {
                    String summaryText = summaryElement.text()
                            .replace("\"", "")
                            .trim();
                    resultBuilder.append("【综合运势】\n").append(summaryText).append("\n\n");
                }

                // 提取各项详细运势
                Elements detailItems = doc.select("div.luck-detail-text d1.item");
                for (Element item : detailItems) {
                    String category = item.selectFirst("dt").text();
                    String content = item.selectFirst("dd").text().trim();
                    resultBuilder.append("【").append(category).append("】\n")
                            .append(content).append("\n\n");
                }

                if (resultBuilder.length() > 0) {
                    final String resultText = resultBuilder.toString().trim();
                    runOnUiThread(() -> tvResult.setText(resultText));
                } else {
                    runOnUiThread(() -> {
                        tvResult.setText("未能从页面中找到运势内容");
                        Toast.makeText(this, "请检查页面结构是否变化", Toast.LENGTH_SHORT).show();
                    });
                }

            } catch (IOException e) {
                Log.e(TAG, "Error fetching fortune data", e);
                runOnUiThread(() -> {
                    tvResult.setText("获取运势失败: " + e.getMessage());
                    Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private String convertConstellationToPath(String constellation) {
        String cleaned = constellation.split(" ")[0]; // 取空格前的部分

        switch (cleaned) {
            case "白羊座": return "aries";
            case "金牛座": return "taurus";
            case "双子座": return "gemini";
            case "巨蟹座": return "cancer";
            case "狮子座": return "leo";
            case "处女座": return "virgo";
            case "天秤座": return "libra";
            case "天蝎座": return "scorpio";
            case "射手座": return "sagittarius";
            case "摩羯座": return "capricorn";
            case "水瓶座": return "aquarius";
            case "双鱼座": return "pisces";
            default: return null;
        }
    }

    private String convertDurationToPath(String duration) {
        if (duration.contains("今日")) {
            return new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date());
        } else if (duration.contains("明日")) {
            return new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date(System.currentTimeMillis() + 86400000));
        } else if (duration.contains("一周")) {
            return "2"; // 根据HTML中的链接结构
        } else if (duration.contains("本月")) {
            return new SimpleDateFormat("yyyyMM", Locale.CHINA).format(new Date());
        } else if (duration.contains("今年")) {
            return new SimpleDateFormat("yyyy", Locale.CHINA).format(new Date());
        }
        return null;
    }
}