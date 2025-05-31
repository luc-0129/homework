package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PostWishActivity extends AppCompatActivity {
    private EditText wishContent, qqNumber;
    private String currentConstellation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_wish);

        currentConstellation = getIntent().getStringExtra("constellation");
        wishContent = findViewById(R.id.wish_content);
        qqNumber = findViewById(R.id.qq_number);

        findViewById(R.id.submit_btn).setOnClickListener(v -> submitWish());
    }

    private void submitWish() {
        String content = wishContent.getText().toString().trim();
        String qq = qqNumber.getText().toString().trim();

        if (currentConstellation == null) {
            Toast.makeText(this, "星座信息丢失，请返回重试", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建心愿对象
        Map<String, Object> wish = new HashMap<>();
        wish.put("content", content);
        wish.put("constellation", currentConstellation);
        wish.put("qq", qq.isEmpty() ? null : qq);
        wish.put("timestamp", System.currentTimeMillis() / 1000);

        // 保存到数据库
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("wishes")
                .push();

        Log.d("FirebaseDebug", "尝试写入数据: " + wish.toString());

        ref.setValue(wish)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirebaseDebug", "写入成功");
                    Toast.makeText(this, "✨ 心愿已上达星空", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseError", "写入失败", e);
                    Toast.makeText(this, "发布失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}