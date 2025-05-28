package com.example.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstellationListActivity extends ListActivity {
    private static final String TAG = "ConstellationList";
    private ArrayList<HashMap<String, String>> constellationList;
    private SimpleAdapter adapter;

    // 星座英文名映射
    private static final HashMap<String, String> CONSTELLATION_MAP = new HashMap<String, String>() {{
        put("白羊座", "aries");
        put("金牛座", "taurus");
        put("双子座", "gemini");
        put("巨蟹座", "cancer");
        put("狮子座", "leo");
        put("处女座", "virgo");
        put("天秤座", "libra");
        put("天蝎座", "scorpio");
        put("射手座", "sagittarius");
        put("摩羯座", "capricorn");
        put("水瓶座", "aquarius");
        put("双鱼座", "pisces");
    }};

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                constellationList.clear();
                constellationList.addAll((ArrayList<HashMap<String, String>>) msg.obj);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化列表
        constellationList = new ArrayList<>();

        // 修改适配器以支持图标
        adapter = new SimpleAdapter(this, constellationList,
                R.layout.constellation_item,
                new String[]{"name", "date", "icon"},
                new int[]{R.id.constellationName, R.id.constellationDate, R.id.constellationIcon}) {

            @Override
            public void setViewImage(ImageView v, String value) {
                // 重写图片设置方法
                int resId = getResources().getIdentifier(value, "drawable", getPackageName());
                if (resId != 0) {
                    v.setImageResource(resId);
                } else {
                    v.setImageResource(R.drawable.default_constellation);
                }
            }
        };

        setListAdapter(adapter);

        // 启动数据抓取线程
        new Thread(this::fetchConstellationData).start();

        getListView().setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, String> item = constellationList.get(position);
            Intent intent = new Intent(ConstellationListActivity.this,
                    ConstellationDetailActivity.class);
            intent.putExtra("constellationName", item.get("name"));
            startActivity(intent);
        });
    }

    private void fetchConstellationData() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            Log.d(TAG, "开始请求星座数据...");

            Document doc = Jsoup.connect("https://www.ip138.com/xingzuo/")
                    .timeout(15000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            Log.d(TAG, "网页标题: " + doc.title());

            Elements rows = doc.select("div.mod-panel table tbody tr");

            for (Element row : rows) {
                Elements tds = row.children();
                if (tds.size() >= 4) {
                    String name = tds.get(1).text().trim();
                    String dateRange = tds.get(3).text().trim();

                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", name);
                    map.put("date", dateRange);

                    // 添加图标资源名称
                    String englishName = CONSTELLATION_MAP.getOrDefault(name, "default_constellation");
                    map.put("icon", englishName);

                    list.add(map);

                    Log.d(TAG, "解析到星座: " + name + " (" + dateRange + "), 图标: " + englishName);
                }
            }

        } catch (IOException e) {
            Log.e(TAG, "数据获取失败", e);
            runOnUiThread(() -> Toast.makeText(this,
                    "星座数据加载失败: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }

        handler.sendMessage(handler.obtainMessage(1, list));
    }
}