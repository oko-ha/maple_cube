package com.example.maple_cube.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.maple_cube.R;

public class CategoryActivity extends Activity {
    private int temp_id = -1;
    private int select = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_category);

        findViewById(R.id.btn_OK).setOnClickListener(onClickListener);
        findViewById(R.id.btn_Cancel).setOnClickListener(onClickListener);
        // 카테고리
        findViewById(R.id.btn_weapon).setOnClickListener(onClickListener);
        findViewById(R.id.btn_extra_weapon).setOnClickListener(onClickListener);
        findViewById(R.id.btn_emblem).setOnClickListener(onClickListener);
        findViewById(R.id.btn_hat).setOnClickListener(onClickListener);
        findViewById(R.id.btn_top).setOnClickListener(onClickListener);
        findViewById(R.id.btn_suits).setOnClickListener(onClickListener);
        findViewById(R.id.btn_pants).setOnClickListener(onClickListener);
        findViewById(R.id.btn_shoes).setOnClickListener(onClickListener);
        findViewById(R.id.btn_glove).setOnClickListener(onClickListener);
        findViewById(R.id.btn_cape).setOnClickListener(onClickListener);
        findViewById(R.id.btn_shoulder).setOnClickListener(onClickListener);
        findViewById(R.id.btn_belt).setOnClickListener(onClickListener);
        findViewById(R.id.btn_face).setOnClickListener(onClickListener);
        findViewById(R.id.btn_eye).setOnClickListener(onClickListener);
        findViewById(R.id.btn_earring).setOnClickListener(onClickListener);
        findViewById(R.id.btn_ring).setOnClickListener(onClickListener);
        findViewById(R.id.btn_pendant).setOnClickListener(onClickListener);
        findViewById(R.id.btn_machine_heart).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.btn_OK:
                    if (select != -1) {
                        Intent intent = new Intent();
                        intent.putExtra("select", select);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                    } else {
                        Toast.makeText(CategoryActivity.this, "장비를 선택하세요.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_Cancel:
                    finish();
                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                    break;
                // 카테고리
                case R.id.btn_weapon:
                    selected(R.id.btn_weapon);
                    select = 0;
                    break;
                case R.id.btn_extra_weapon:
                    selected(R.id.btn_extra_weapon);
                    select = 1;
                    break;
                case R.id.btn_emblem:
                    selected(R.id.btn_emblem);
                    select = 2;
                    break;
                case R.id.btn_hat:
                    selected(R.id.btn_hat);
                    select = 3;
                    break;
                case R.id.btn_top:
                    selected(R.id.btn_top);
                    select = 4;
                    break;
                case R.id.btn_suits:
                    selected(R.id.btn_suits);
                    select = 5;
                    break;
                case R.id.btn_pants:
                    selected(R.id.btn_pants);
                    select = 6;
                    break;
                case R.id.btn_shoes:
                    selected(R.id.btn_shoes);
                    select = 7;
                    break;
                case R.id.btn_glove:
                    selected(R.id.btn_glove);
                    select = 8;
                    break;
                case R.id.btn_cape:
                    selected(R.id.btn_cape);
                    select = 9;
                    break;
                case R.id.btn_shoulder:
                    selected(R.id.btn_shoulder);
                    select = 10;
                    break;
                case R.id.btn_belt:
                    selected(R.id.btn_belt);
                    select = 11;
                    break;
                case R.id.btn_face:
                    selected(R.id.btn_face);
                    select = 12;
                    break;
                case R.id.btn_eye:
                    selected(R.id.btn_eye);
                    select = 13;
                    break;
                case R.id.btn_earring:
                    selected(R.id.btn_earring);
                    select = 14;
                    break;
                case R.id.btn_ring:
                    selected(R.id.btn_ring);
                    select = 15;
                    break;
                case R.id.btn_pendant:
                    selected(R.id.btn_pendant);
                    select = 16;
                    break;
                case R.id.btn_machine_heart:
                    selected(R.id.btn_machine_heart);
                    select = 17;
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

    // 화면 밖 터치 통제
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction()!=MotionEvent.ACTION_OUTSIDE;
    }
}
