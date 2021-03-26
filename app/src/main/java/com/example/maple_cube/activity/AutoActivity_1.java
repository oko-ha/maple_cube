package com.example.maple_cube.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.maple_cube.R;


public class AutoActivity_1 extends Activity {
    private int temp_id = -1;
    private int select = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_auto_1);

        findViewById(R.id.btn_red_cube).setOnClickListener(onClickListener);
        findViewById(R.id.btn_black_cube).setOnClickListener(onClickListener);
        findViewById(R.id.btn_bonus_cube).setOnClickListener(onClickListener);
        findViewById(R.id.btn_OK).setOnClickListener(onClickListener);
        findViewById(R.id.btn_Cancel).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_OK:
                    if (select != -1) {
                        Intent intent = new Intent();
                        intent.putExtra("select", select);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(AutoActivity_1.this, "큐브를 선택하세요.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_Cancel:
                    finish();
                    break;
                case R.id.btn_red_cube:
                    selected(R.id.btn_red_cube);
                    select = 0;
                    break;
                case R.id.btn_black_cube:
                    selected(R.id.btn_black_cube);
                    select = 1;
                    break;
                case R.id.btn_bonus_cube:
                    selected(R.id.btn_bonus_cube);
                    select = 2;
                    break;
            }
        }
    };

    protected void selected(int id){
        if (temp_id != -1) {
            findViewById(temp_id).setBackgroundColor(Color.TRANSPARENT);
        }
        findViewById(id).setBackground(ContextCompat.getDrawable(this, R.drawable.border_selected));
        temp_id = id;
    }
}
