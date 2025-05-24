package com.example.myapplication;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateList extends ListActivity {

    private static final String TAG = "RateActivity";
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("list", "lastRateDateStr=" + logDate);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                // 接收返回的数据项
                if (msg.what == 9) {
                    List<String> list2 = (List<String>) msg.obj;
                    ListAdapter adapter2 = new ArrayAdapter<String>(RateList.this, android.R.layout.simple_list_item_1, list2);
                    setListAdapter(adapter2);
                }
                super.handleMessage(msg);
            }
        };

        new Thread(this::run).start();
    }
    public void run() {
        Log.i("list", "run...");
        List<String> retList = new ArrayList<>();
        Message msg = handler.obtainMessage();
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i("run", "curDateStr:" + curDateStr + " logDate:" + logDate);
        if (!curDateStr.equals(logDate)) {
            // 如果日期不相等，从网络中获取数据
            Log.i("run", "日期不相等，从网络中获取在线数据");
            try {
                List<RateItem> rateList = new ArrayList<>();
                URL url = new URL("https://chl.cn/huilv/?jinri");
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                InputStream in = httpConn.getInputStream();
                String retStr = IOUtils.toString(in, "UTF-8");
                Document doc = Jsoup.parse(retStr);
                Elements tables = doc.getElementsByTag("table");
                Element retTable = tables.get(0); // 获取第一个表格
                Elements trs = retTable.getElementsByTag("tr");
                for (Element tr : trs) {
                    Elements tds = tr.getElementsByTag("td");
                    if (tds.size() > 5) {
                        String curName = tds.get(0).text();
                        String curRate = tds.get(4).text();
                        retList.add(curName + "->" + curRate);
                        RateItem rateItem = new RateItem(curName, curRate);
                        rateList.add(rateItem);
                    }
                }
                RateManager dbManager = new RateManager(RateList.this);
                dbManager.deleteAll();
                Log.i("db", "删除所有记录");
                dbManager.addAll(rateList);
                Log.i("db", "添加新记录集");

                // 更新记录日期
                SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(DATE_SP_KEY, curDateStr);
                edit.commit();
                Log.i("run", "更新日期结束: " + curDateStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 如果日期相等，从数据库中获取数据
            Log.i("run", "日期相等，从数据库中获取数据");
            RateManager dbManager = new RateManager(RateList.this);
            for (RateItem rateItem : dbManager.listAll()) {
                retList.add(rateItem.getCurName() + " -> " + rateItem.getCurRate());
            }
        }

        msg.obj = retList;
        msg.what = 9;
        handler.sendMessage(msg);
    }
}