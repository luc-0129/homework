package com.example.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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
        adapter = new SimpleAdapter(this, constellationList,
                R.layout.constellation_item,
                new String[]{"name", "date"},
                new int[]{R.id.constellationName, R.id.constellationDate});
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

            // 配置JSoup连接
            Document doc = Jsoup.connect("https://www.ip138.com/xingzuo/")
                    .timeout(15000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            Log.d(TAG, "网页标题: " + doc.title());

            // 根据提供的HTML结构定位星座数据
            Elements rows = doc.select("div.mod-panel table tbody tr");

            for (Element row : rows) {
                Elements tds = row.children();
                if (tds.size() >= 4) { // 确保有足够的列
                    String name = tds.get(1).text().trim(); // 第二列为星座名称
                    String dateRange = tds.get(3).text().trim(); // 第四列为日期范围

                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", name);
                    map.put("date", dateRange);
                    list.add(map);

                    Log.d(TAG, "解析到星座: " + name + " (" + dateRange + ")");
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