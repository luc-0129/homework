package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConvertActivity extends Activity {

    private TextView currencyTitle;
    private TextView exchangeRate;
    private EditText rmbInput;
    private Button calculateBtn;
    private TextView resultText;
    private Button backBtn;

    private String currencyName;
    private double rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction);

        // 获取传递过来的数据
        Intent intent = getIntent();
        currencyName = intent.getStringExtra("currencyName");
        String rateStr = intent.getStringExtra("rate");
        rate = Double.parseDouble(rateStr);

        // 初始化视图
        currencyTitle = findViewById(R.id.currencyTitle);
        exchangeRate = findViewById(R.id.exchangeRate);
        rmbInput = findViewById(R.id.rmbInput);
        calculateBtn = findViewById(R.id.calculateBtn);
        resultText = findViewById(R.id.resultText);
        backBtn = findViewById(R.id.backBtn);

        // 设置货币名称和汇率
        currencyTitle.setText("货币: " + currencyName);
        exchangeRate.setText("汇率: 1人民币 = " + rateStr + currencyName);

        // 计算按钮点击事件
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateExchange();
            }
        });

        // 返回按钮点击事件
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void calculateExchange() {
        String inputStr = rmbInput.getText().toString();
        if (!inputStr.isEmpty()) {
            try {
                double rmbAmount = Double.parseDouble(inputStr);
                double exchangeAmount = rmbAmount * rate;
                resultText.setText(String.format("兑换结果: %.2f人民币 = %.2f%s", rmbAmount, exchangeAmount, currencyName));
            } catch (NumberFormatException e) {
                resultText.setText("请输入有效的数字");
            }
        } else {
            resultText.setText("请输入人民币金额");
        }
    }
}