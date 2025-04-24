package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DirectAction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Documented;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.Result;

public class MainActivity_ExchangeRate extends AppCompatActivity implements  Runnable {

    private static final String TAG = "Rate";
    private EditText inputRmb;
    private TextView tvResult;
    private TextView show;
    private float dollarRate = 0.13f;
    private float euroRate = 0.12f;
    private float wonRate = 185.19f;

    Handler handler;


    @SuppressLint({"MissingInflatedId", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_exchange_rate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputRmb = findViewById(R.id.RMB);
        tvResult = findViewById(R.id.result);
        show = findViewById(R.id.Show);

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage:接收消息");
                if (msg.what == 5) {
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: str=" + str);
                    show.setText(str);
                }
                super.handleMessage(msg);
            }
        };

    }

    public void my_click(View btn) {
        Log.i(TAG, "my_click:hello");
        String str_1 = inputRmb.getText().toString();
        double Result = 0;
        try {
            double num_1 = Double.parseDouble(str_1);
            if (btn.getId() == R.id.button1) {
                Result = dollarRate * num_1;
            }
            if (btn.getId() == R.id.button2) {
                Result = euroRate * num_1;
            }
            if (btn.getId() == R.id.button3) {
                Result = wonRate * num_1;
            }
            String result = String.format("%.2f", Result);
            tvResult.setText(result);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确数据", Toast.LENGTH_LONG).show();
        }

    }

    public void click_new(View btn) {
        Log.i(TAG, "启动线程");
        Thread t = new Thread(this);
        t.start();
    }

    public void clickOpen(View btn) {
        //打开新窗口
        Intent config = new Intent(this, MainActivity_RateShow.class);
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);

        Log.i(TAG, "clickOpen:dollarRate=" + dollarRate);
        Log.i(TAG, "clickOpen:euroRate=" + euroRate);
        Log.i(TAG, "clickOpen:wonRate=" + wonRate);

        startActivityForResult(config, 3);

    }

    //接收返回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 3 && resultCode == 6) {
            //汇率修改页面返回的数据，说明data中包含Bundle对象
            Bundle bdl = data.getExtras();
            //拆分放入的数据
            dollarRate = bdl.getFloat("key_dollar2");
            euroRate = bdl.getFloat("key_euro2");
            wonRate = bdl.getFloat("key_won2");
            Log.i(TAG, "onActivityResult:dollarRate=" + dollarRate);
            Log.i(TAG, "onActivityResult:euroRate=" + euroRate);
            Log.i(TAG, "onActivityResult:wonRate=" + wonRate);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {
        Log.i(TAG, "run:run......");

        URL url = null;
        StringBuilder html = new StringBuilder();
        try {
            url=new URL("https://chl.cn/huilv/?jinri");
            HttpURLConnection http=(HttpURLConnection) url.openConnection();
            InputStream in =http.getInputStream();

            html= new StringBuilder(inputStream2String(in));
            Log.i(TAG,"run:html="+html);
//            Document doc = Jsoup.connect("https://chl.cn/huilv/?jinri").timeout(10000).get();
//            Log.i(TAG, "run:title=" + doc.title());
//            Elements tables = doc.getElementsByTag("table");
//            if (tables.isEmpty()) {
//                throw new IOException("No tables found on the page");
//            }
//            Element table = tables.get(0);
//            Elements rows = table.getElementsByTag("tr");
//            for (Element row : rows) {
//                Elements tds = row.getElementsByTag("td");
//                //Log.i(TAG,"run:row="+row);
//                Element td1 = tds.first();
//                Element td2 = tds.get(1);
//                Log.i(TAG, "run:td1=" + td1.text() + "->" + td2.text());
//                //Log.i(TAG,"run:td1="+td1.html()+"->"+td2.html());
//
//            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(5, html.toString());
        handler.sendMessage(msg);
    }

    private String inputStream2String(InputStream inputStream)
            throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}