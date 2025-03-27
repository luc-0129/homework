package com.example.myapplication;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private  static final String TAG="tag3333";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public void myclick(View v){
        Log.i(TAG,"myclick:hello");
        EditText input_1=findViewById(R.id.height);
        EditText input_2=findViewById(R.id.weight);
        TextView out = findViewById(R.id.massage);
        String str_1=input_1.getText().toString();
        String str_2=input_2.getText().toString();
        try {
            double num_1 = Double.parseDouble(str_1);
            double num_2 = Double.parseDouble(str_2);
            double bmi=num_2/(num_1*num_1);
            String Massage;
            if(bmi<18.5){
                Massage="体重过轻";
            } else if (bmi>=18.5||bmi<24.9) {
                Massage="正常体重";
            } else{
                Massage="超重";
            }
            String Bmi = String.format("%.2f",bmi);
            out.setText("BMI为"+Bmi+","+Massage);
        }catch(NumberFormatException e){
            Log.e(TAG,"myclick:Invalid input",e);
            out.setText("Invalid input");
        }
    }
}