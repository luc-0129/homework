package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomListActivity extends ListActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private static final String TAG="CustomListActivity";
    private ArrayList<HashMap<String,String>>listItems;
    private SimpleAdapter listItemAdapter;
    private Handler handler;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initListView();

        setListAdapter(listItemAdapter);

        handler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg){
                if(msg.what==9){
                    Log.i(TAG,"handleMessage:what="+msg.what);
                    listItems=(ArrayList<HashMap<String,String>>)msg.obj ;
//                    listItemAdapter=new SimpleAdapter(CustomListActivity.this,listItems,R.layout.list_item,new String[]{"ItemTitle","Price"},new int[]{R.id.itemTitle,R.id.price});
                    adapter=new MyAdapter(CustomListActivity.this,R.layout.list_item,listItems);
                    setListAdapter(adapter);

                }
                super.handleMessage(msg);
            }
        };

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);

        //定义一个线程获取数据
        Thread t=new Thread(()->{
            ArrayList<HashMap<String,String>>list=new ArrayList<HashMap<String,String>>();
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

                            Log.i(TAG,"run:td1="+currencyName+"->"+buyPrice);
                            HashMap<String,String> map=new HashMap<String,String>();
                            map.put("ItemTitle",currencyName);
                            map.put("Price",buyPrice);
                            list.add(map);
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            Message msg=handler.obtainMessage(9,list);
            handler.sendMessage(msg);
        });
        t.start();

    }

    private void initListView(){
        listItems=new ArrayList<HashMap<String,String>>();
        for(int i=0;i<10;i++){
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("ItemTitle","Rate: "+i);
            map.put("Price","detail"+i);
            listItems.add(map);
        }
        listItemAdapter=new SimpleAdapter(this,listItems,R.layout.list_item,new String[]{"ItemTitle","Price"},new int[]{R.id.itemTitle,R.id.price});
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAtPosition=getListView().getItemAtPosition(position);
        HashMap<String,String>map=(HashMap<String, String>) itemAtPosition;
        String titleStr=map.get("ItemTitle");
        String priceStr=map.get("Price");
        Log.i(TAG,"onItemClick:titleStr="+titleStr);
        Log.i(TAG,"onItemClick:detailStr="+priceStr);
        Log.i(TAG,"onItemClick:position="+position);
        // 新增跳转逻辑
        Intent intent = new Intent(CustomListActivity.this, ConvertActivity.class);
        intent.putExtra("currencyName", titleStr);
        intent.putExtra("rate", priceStr);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "长按操作", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick: 对话框事件处理 - 开始");
                        listItems.remove(position);
                        adapter.notifyDataSetChanged();
                        Log.i(TAG, "onClick: 对话框事件处理 - 结束");
                    }
                })
                .setNegativeButton("否", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        Log.i(TAG, "onItemLongClick: size=" + listItems.size());
        return true;
    }
}