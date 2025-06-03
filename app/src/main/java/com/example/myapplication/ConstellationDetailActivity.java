package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.DeepSeekApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.ApiRequest;
import com.example.myapplication.model.ApiResponse;
import com.example.myapplication.model.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConstellationDetailActivity extends AppCompatActivity {
    // 视图组件
    private TextView tvTitle, tvDesc1, tvDesc2, tvDesc3, tvAnswer;
    private EditText etQuestion;
    private ProgressBar progressBar;
    private Button btnAsk;

    // API服务
    private DeepSeekApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constellation_detail);

        // 初始化视图
        initViews();

        // 初始化Retrofit
        initRetrofit();

        // 设置星座数据
        setupConstellationData();

        // 设置按钮事件
        setupButtonListeners();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvDesc1 = findViewById(R.id.tvDesc1);
        tvDesc2 = findViewById(R.id.tvDesc2);
        tvDesc3 = findViewById(R.id.tvDesc3);
        tvAnswer = findViewById(R.id.tvAnswer);
        etQuestion = findViewById(R.id.etQuestion);
        progressBar = findViewById(R.id.progressBar);
        btnAsk = findViewById(R.id.btnAsk);
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(DeepSeekApiService.class);
    }

    private void setupConstellationData() {
        String constellationName = getIntent().getStringExtra("constellationName");
        tvTitle.setText(constellationName);
        loadConstellationData(constellationName);
    }

    private void setupButtonListeners() {
        // 提问按钮
        btnAsk.setOnClickListener(v -> {
            String question = etQuestion.getText().toString().trim();
            if (question.isEmpty()) {
                Toast.makeText(this, "请输入问题", Toast.LENGTH_SHORT).show();
                return;
            }
            askQuestion(question);
        });

        // 其他按钮
        findViewById(R.id.post_wish_btn).setOnClickListener(v ->
                startActivity(new Intent(this, PostWishActivity.class)
                        .putExtra("constellation", getIntent().getStringExtra("constellationName")))
        );

        findViewById(R.id.view_wishes_btn).setOnClickListener(v ->
                startActivity(new Intent(this, WishWallActivity.class)
                        .putExtra("constellation", getIntent().getStringExtra("constellationName")))
        );
    }

    private void askQuestion(String question) {
        progressBar.setVisibility(View.VISIBLE);
        btnAsk.setEnabled(false);
        tvAnswer.setVisibility(View.GONE);

        List<Message> messages = new ArrayList<>();
        messages.add(new Message(
                "system",
                "你是一个星座专家，请用专业但易懂的语言回答关于" +
                        tvTitle.getText().toString() + "的问题。"
        ));
        messages.add(new Message("user", question));

        DeepSeekApiService apiService = RetrofitClient.getApiService();
        ApiRequest request = new ApiRequest(messages);

        Call<ApiResponse> call = apiService.getCompletion(
                "Bearer " + ApiConfig.DEEPSEEK_API_KEY,
                "application/json",
                request
        );

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnAsk.setEnabled(true);
                });

                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getChoices() != null && !apiResponse.getChoices().isEmpty()) {
                        String answer = apiResponse.getChoices().get(0).getMessage().getContent();
                        runOnUiThread(() -> showAnswer(answer));
                    } else {
                        runOnUiThread(() -> showError("未收到有效回答"));
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "未知错误";
                        runOnUiThread(() -> showError("API请求失败: " + response.code() + " - " + errorBody));
                    } catch (IOException e) {
                        runOnUiThread(() -> showError("API请求失败: " + response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnAsk.setEnabled(true);

                    if (t instanceof SocketTimeoutException) {
                        showError("请求超时，请稍后再试");
                    } else {
                        showError("网络错误: " + t.getMessage());
                    }
                });
            }
        });
    }
    private void showAnswer(String answer) {
        tvAnswer.setText(answer);
        tvAnswer.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        tvAnswer.setText(message);
        tvAnswer.setVisibility(View.VISIBLE);
    }

    // 原有的网页解析方法保持不变
    private void loadConstellationData(String name) {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://www.ip138.com/xingzuo/")
                        .timeout(10000)
                        .get();

                String desc1 = parseGuardianInfo(doc, name);
                String desc2 = parseCharacterInfo(doc, name);
                String desc3 = parseAstroInfo(doc, name);

                runOnUiThread(() -> {
                    tvDesc1.setText(desc1);
                    tvDesc2.setText(desc2);
                    tvDesc3.setText(desc3);
                });

            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this,
                        "星座数据加载失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private String parseGuardianInfo(Document doc, String name) {
        Element dt = doc.select("dt:containsOwn(" + name + ")").first();
        return dt != null ? dt.text() + "\n" + dt.nextElementSibling().text()
                : "暂无守护神信息";
    }

    private String parseCharacterInfo(Document doc, String name) {
        Elements dts = doc.select("dt:containsOwn(" + name + ")");
        return dts.size() > 1 ? dts.get(1).text() + "\n" +
                dts.get(1).nextElementSibling().text()
                : "暂无性格特征信息";
    }

    private String parseAstroInfo(Document doc, String name) {
        Element dt = doc.select("dt:containsOwn(" + name + ")").last();
        if (dt == null) return "暂无星座花语信息";

        StringBuilder sb = new StringBuilder();
        for (Element p : dt.nextElementSibling().select("p")) {
            sb.append(p.text()).append("\n");
        }
        return sb.toString();
    }
}