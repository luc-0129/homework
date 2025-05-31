package com.example.myapplication;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.ViewHolder> {
    private List<Wish> wishes = new ArrayList<>();

    public void setWishes(List<Wish> wishes) {
        this.wishes = wishes != null ? wishes : new ArrayList<>(); // 防null处理
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Wish wish = wishes.get(position);
            holder.content.setText(wish.getContent() != null ? wish.getContent() : "");

            if (!TextUtils.isEmpty(wish.getQq())) {
                holder.qq.setText("QQ: " + wish.getQq());
                holder.qq.setVisibility(View.VISIBLE);
            } else {
                holder.qq.setVisibility(View.GONE);
            }

            // 添加时间戳非空检查
            if (wish.getTimestamp() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String time = sdf.format(new Date(wish.getTimestamp() * 1000L));
                holder.time.setText(time);
            } else {
                holder.time.setText("时间未知");
            }
        } catch (Exception e) {
            Log.e("WishAdapter", "数据绑定错误: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return wishes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content, qq, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.wish_content);
            qq = itemView.findViewById(R.id.qq_number);
            time = itemView.findViewById(R.id.wish_time);
        }
    }
}