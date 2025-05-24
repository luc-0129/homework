package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MyTask implements Runnable {
    private  static  final  String TAG="Rata";
    Handler handler;
    public MyTask(Handler handler) {
        this.handler=handler;
    }

    @Override
    public void run() {
        Log.i(TAG, "run: 子线程run()……");
        URL url=null;
        String html= "";
        Bundle retbundle=new Bundle();
        ArrayList<String>list=new ArrayList<String>();
        try {
            Document doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").get();
            Elements tables = doc.getElementsByTag("table");
            if (tables.size() > 0) {
                Element table = tables.get(0); // 获取第一个表格
                Elements rows = table.getElementsByTag("tr");
                rows.remove(0); // 移除表头
                for (Element row : rows) {
                    Elements tds = row.children();
                    if (tds.size() > 1) { // 确保有足够的列
                        String currencyName = tds.get(0).text().trim();
                        String buyPrice = tds.get(1).text().trim();
                        String sellPrice = tds.size() > 2 ? tds.get(2).text().trim() : "N/A"; // 检查是否存在卖出价
                        Log.i(TAG, "run:td1=" + currencyName + "==>" + buyPrice + "/" + sellPrice);
                        list.add(currencyName + "=>" + buyPrice + (sellPrice.equals("N/A") ? "" : "/" + sellPrice));

                        if (currencyName.equals("美元")) {
                            retbundle.putFloat("dollar", 100 / Float.parseFloat(sellPrice));
                        } else if (currencyName.equals("欧元")) {
                            retbundle.putFloat("euro", 100 / Float.parseFloat(sellPrice));
                        } else if (currencyName.equals("韩元")) {
                            retbundle.putFloat("win", 100 / Float.parseFloat(sellPrice));
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        Log.i(TAG, "run: MyTask bundle="+retbundle);
        retbundle.putStringArrayList("mylist",list);
        Message msg=handler.obtainMessage(5,retbundle);
        handler.sendMessage(msg);

    }
}