package com.example.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity {
    private static final String TAG = "RateListActivity";
    Handler handler;
    private String logDate="";
    private final String DATE_SP_KEY="lastRateDateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String[] data = {"one", "two", "three", "four"};
        List<String> list_data = new ArrayList<String>(100);
        for(int i=1;i<=100;i++){
            list_data.add("Item"+i);
        }

        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==5){
                    Bundle bundle= (Bundle) msg.obj;
                    ArrayList<String>relist=bundle.getStringArrayList("mylist");
                    ListAdapter adapter=new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,relist);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_data);

        setListAdapter(adapter);

        Thread t=new Thread(new MyTask(handler));
        t.start();
        Log.i(TAG, "onCreate: 启动线程");
    }
}















