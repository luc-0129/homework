package com.example.myapplication;

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

public class MainActivity_1 extends AppCompatActivity {
    private  static final String TAG="tag3333";
    private TextView tv;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        TextView tv=findViewById(R.id.btn);
//        Date currentTime=new Date();
//        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy:MM:dd", Locale.getDefault());
//        String formattedTime=dateFormat.format(currentTime);
//        tv.setText(formattedTime);

//        System.out.println("this is System.out.print()");
//        Log.i(TAG, "onCreate: ");
//        Log.e(TAG,"onCreate: sss");
//
//        EditText input=findViewById(R.id.editText);
//        String inputStr=input.getText().toString();
//
//        Button btn=findViewById(R.id.btn);
////        btn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Log.i(TAG,"onClick: bbbbbbbbbb");
////            }
////        });
//        btn.setOnClickListener((V)->{
//            Log.i(TAG,"onClick: cccccccccc");
//            tv.setText("clicked");
//        });
    }
    public void myclick(View v){
        Log.i(TAG,"myclick:hello");
        EditText input=findViewById(R.id.temperature);
        TextView out = findViewById(R.id.temperature);
        String str=input.getText().toString();
        try {
            double num = Double.parseDouble(str);
            num = num * 9 / 5 + 32;
            String str_1 = Double.toString(num);
            out.setText(str_1);
        }catch(NumberFormatException e){
            Log.e(TAG,"myclick:Invalid input",e);
            out.setText("Invalid input");
        }
    }
}