package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WishWallActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WishAdapter adapter;
    private String constellation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("WishWall", "Activity启动"); // 确保基础日志能输出
        setContentView(R.layout.activity_wish_wall);
        Log.d("WishWall", "星座参数: " + getIntent().getStringExtra("constellation"));

        constellation = getIntent().getStringExtra("constellation");
        setupRecyclerView();
        loadWishes();

    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.wish_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WishAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void loadWishes() {
        // 移除错误的类型转换 (DatabaseReference)
        Query ref = FirebaseDatabase.getInstance()
                .getReference("wishes")
                .orderByChild("constellation")
                .equalTo(constellation);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Firebase", "数据快照数量: " + snapshot.getChildrenCount());
                for (DataSnapshot data : snapshot.getChildren()) {
                    Log.d("Firebase", "数据键: " + data.getKey());
                    Log.d("Firebase", "数据值: " + data.getValue());
                }
                List<Wish> wishes = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Wish wish = data.getValue(Wish.class);
                    wish.setId(data.getKey());
                    wishes.add(wish);
                }
                // 按时间倒序排列
                Collections.sort(wishes, (w1, w2) -> Long.compare(w2.getTimestamp(), w1.getTimestamp()));
                adapter.setWishes(wishes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "错误代码: " + error.getCode() + ", 详情: " + error.getMessage());
                Toast.makeText(WishWallActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}