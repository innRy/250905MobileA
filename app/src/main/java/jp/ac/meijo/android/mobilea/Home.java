package jp.ac.meijo.android.mobilea;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextClock;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.TimePicker;

public class Home extends AppCompatActivity {

    private TimePicker timePicker;
    private Button btnSetting,btnSave,btnDelete;
    private LinearLayout buttonArea;
    private TextClock textClock;
    private SharedPreferences prefs;

    private boolean isVisible = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        timePicker = findViewById(R.id.timePicker);
        btnSetting = findViewById(R.id.btnSetting);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        buttonArea = findViewById(R.id.buttonArea);
        textClock = findViewById(R.id.textClock);

        prefs = getSharedPreferences("TimeData",MODE_PRIVATE);

        //初期状態
        timePicker.setVisibility(View.GONE);
        buttonArea.setVisibility(View.GONE);

        //保存済みの時間の表示
        int hour = prefs.getInt("hour",-1);
        int minute = prefs.getInt("minute",-1);

        if(hour != -1){
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
            textClock.setText("起きる時間："+hour+"時"+minute+"分");
        }

        //起きる時間ボタン
        btnSetting.setOnClickListener(v -> {
            if(isVisible){
                timePicker.setVisibility(View.GONE);
                buttonArea.setVisibility(View.GONE);
                isVisible = false;
            }
            else{
                timePicker.setVisibility(View.VISIBLE);
                buttonArea.setVisibility(View.VISIBLE);
                isVisible = true;
            }
        });

        btnSave.setOnClickListener(v ->{
            int h = timePicker.getHour();
            int m = timePicker.getMinute();

            prefs.edit()
                    .putInt("hour",h)
                    .putInt("minute",m)
                    .apply();

            textClock.setText("起きる時間："+h+"時"+m+"分");

            timePicker.setVisibility(View.GONE);
            buttonArea.setVisibility(View.GONE);
            isVisible = false;
        });


        btnDelete.setOnClickListener(v ->{
            prefs.edit().clear().apply();
            textClock.setText("起きる時間：なし");
            timePicker.setHour(7);
            timePicker.setMinute(0);
        });

    }
}