package com.example.myapplication;

import static kotlin.text.Typography.dollar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity_RateShow extends AppCompatActivity {

    private static  final String TAG="Rate";
    private EditText dollarText,euroText,wonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_rate_show);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//接收传入的数据
        Intent intent=getIntent();
        float dollar=intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro=intent.getFloatExtra("euro_rate_key",0.0f);
        float won=intent.getFloatExtra("won_rate_key",0.0f);


    Log.i(TAG,"onCreate:dollar="+dollar);
    Log.i(TAG,"onCreate:euro="+euro);
    Log.i(TAG,"onCreate:won="+won);
//把数据放入页面控件中，供用户修改
    dollarText=findViewById(R.id.dollar_rate);
    euroText=findViewById(R.id.euro_rate);
    wonText=findViewById(R.id.won_rate);

    dollarText.setText(String.valueOf(dollar));
    euroText.setText(String.valueOf(euro));
    wonText.setText(String.valueOf(won));
}
    public void save(View btn){
        Log.i(TAG,"save:");
        //重新获取用户输入值
        String dollarStr=dollarText.getText().toString();
        String euroStr=euroText.getText().toString();
        String wonStr=wonText.getText().toString();

        Log.i(TAG,"save:dollarStr="+dollarStr);
        Log.i(TAG,"save:euroStr="+euroStr);
        Log.i(TAG,"save:wonStr="+wonStr);

        //带回数据
        Intent intent=getIntent();
        //放入数据到intent
        Bundle bundle=new Bundle();
        bundle.putFloat("key_dollar2",Float.parseFloat(dollarStr));
        bundle.putFloat("key_euro2",Float.parseFloat(euroStr));
        bundle.putFloat("key_won2",Float.parseFloat(wonStr));
        intent.putExtras(bundle);

        setResult(6,intent);

        finish();
    }

}