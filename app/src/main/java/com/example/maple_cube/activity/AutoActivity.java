package com.example.maple_cube.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.maple_cube.R;

public class AutoActivity extends Activity {
    private static final int RED_CUBE = 0;
    private static final int BLACK_CUBE = 1;
    private static final int BONUS_CUBE = 2;

    private int potential_class;
    private int bonus_class;
    private int category;

    private int temp_id;
    private int select;
    private int viewIndex;

    private static final String[][] OPTION_STRING = {
            {"보보○", "공공○", "마마○", "보보방", "보보공", "보공공", "보보마", "보마마"},
            {"공공○", "마마○", "공공공", "마마마", "방공공", "방마마", "", ""},
            {"올올올", "HpHpHp", "힘힘힘", "덱덱덱", "인인인", "럭럭럭", "", ""},
            {"올올올", "HpHpHp", "힘힘힘", "덱덱덱", "인인인", "럭럭럭", "쿨쿨○", "쿨쿨쿨"},
            {"크크○", "크크힘", "크크덱", "크크인", "크크럭", "크크크", "", ""},
            {"올올올", "HpHpHp", "힘힘힘", "덱덱덱", "인인인", "럭럭럭", "쌍드메", "트리플 드메"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_auto);
        changeView(0);

        Intent intent = getIntent();
        potential_class = intent.getIntExtra("potential_class", -1);
        bonus_class = intent.getIntExtra("bonus_class", -1);
        category = intent.getIntExtra("category", -1);

        // activity_auto
        findViewById(R.id.btn_OK).setOnClickListener(onClickListener);
        findViewById(R.id.btn_Cancel).setOnClickListener(onClickListener);
        // view_auto_cube
        findViewById(R.id.btn_red_cube).setOnClickListener(onClickListener);
        findViewById(R.id.btn_black_cube).setOnClickListener(onClickListener);
        findViewById(R.id.btn_bonus_cube).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                // activity_auto
                case R.id.btn_OK:
                    onViewCreate(select);
                    break;
                case R.id.btn_Cancel:
                    finish();
                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                    break;
                // view_auto_cube
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
                // view_auto_setting
                case R.id.btn_to_option:
                    selected(R.id.btn_to_option);
                    select = 0;
                    break;
                case R.id.btn_to_number:
                    selected(R.id.btn_to_number);
                    select = 1;
                    break;
                case R.id.btn_to_class:
                    selected(R.id.btn_to_class);
                    select = 2;
                    break;
                // view_auto_option
                case R.id.btn_option_1:
                    selected(R.id.btn_option_1);
                    break;
                case R.id.btn_option_2:
                    selected(R.id.btn_option_2);
                    break;
                case R.id.btn_option_3:
                    selected(R.id.btn_option_3);
                    break;
                case R.id.btn_option_4:
                    selected(R.id.btn_option_4);
                    break;
                case R.id.btn_option_5:
                    selected(R.id.btn_option_5);
                    break;
                case R.id.btn_option_6:
                    selected(R.id.btn_option_6);
                    break;
                case R.id.btn_option_7:
                    selected(R.id.btn_option_7);
                    break;
                case R.id.btn_option_8:
                    selected(R.id.btn_option_8);
                    break;
            }
        }
    };

    protected void onViewCreate(int selection) {
        switch (viewIndex) {
            case 0: // view_auto_cube
                if (selection != -1) {
                    changeView(1);
                    TextView tv_selected_cube = findViewById(R.id.tv_selected_cube);
                    ImageView iv_selected_cube = findViewById(R.id.iv_selected_cube);
                    switch (selection) {
                        case RED_CUBE:
                            tv_selected_cube.setText("현재 선택된 큐브 : 레드 큐브");
                            iv_selected_cube.setImageResource(R.drawable.red_cube);
                            break;
                        case BLACK_CUBE:
                            tv_selected_cube.setText("현재 선택된 큐브 : 블랙 큐브");
                            iv_selected_cube.setImageResource(R.drawable.black_cube);
                            break;
                        case BONUS_CUBE:
                            tv_selected_cube.setText("현재 선택된 큐브 : 에디셔널 큐브");
                            iv_selected_cube.setImageResource(R.drawable.bonus_cube);
                            break;
                    }
                    findViewById(R.id.btn_to_option).setOnClickListener(onClickListener);
                    findViewById(R.id.btn_to_number).setOnClickListener(onClickListener);
                    findViewById(R.id.btn_to_class).setOnClickListener(onClickListener);
                } else {
                    Toast.makeText(getApplicationContext(), "큐브를 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1: // view_auto_setting
                if (selection != -1) {
                    switch (selection) {
                        case 0: // auto_option
                            changeView(2);
                            // text 변환
                            int index = -1;
                            findViewById(R.id.btn_option_1).setOnClickListener(onClickListener);
                            findViewById(R.id.btn_option_2).setOnClickListener(onClickListener);
                            findViewById(R.id.btn_option_3).setOnClickListener(onClickListener);
                            findViewById(R.id.btn_option_4).setOnClickListener(onClickListener);
                            findViewById(R.id.btn_option_5).setOnClickListener(onClickListener);
                            findViewById(R.id.btn_option_6).setOnClickListener(onClickListener);
                            switch (category){
                                case 0: case 1:
                                    index = 0;
                                    findViewById(R.id.btn_option_7).setOnClickListener(onClickListener);
                                    findViewById(R.id.btn_option_8).setOnClickListener(onClickListener);
                                    break;
                                case 2:
                                    index = 1;
                                    break;
                                case 4: case 5: case 6: case 8: case 10:
                                    index = 2;
                                    break;
                                case 3:
                                    index = 3;
                                    findViewById(R.id.btn_option_7).setOnClickListener(onClickListener);
                                    findViewById(R.id.btn_option_8).setOnClickListener(onClickListener);
                                    break;
                                case 7:
                                    index = 4;
                                    break;
                                case 9:
                                    index = 5;
                                    findViewById(R.id.btn_option_7).setOnClickListener(onClickListener);
                                    findViewById(R.id.btn_option_8).setOnClickListener(onClickListener);
                                    break;
                            }
                            TextView tv_option_1 = findViewById(R.id.tv_option_1);
                            TextView tv_option_2 = findViewById(R.id.tv_option_2);
                            TextView tv_option_3 = findViewById(R.id.tv_option_3);
                            TextView tv_option_4 = findViewById(R.id.tv_option_4);
                            TextView tv_option_5 = findViewById(R.id.tv_option_5);
                            TextView tv_option_6 = findViewById(R.id.tv_option_6);
                            TextView tv_option_7 = findViewById(R.id.tv_option_7);
                            TextView tv_option_8 = findViewById(R.id.tv_option_8);

                            tv_option_1.setText(OPTION_STRING[index][0]);
                            tv_option_2.setText(OPTION_STRING[index][1]);
                            tv_option_3.setText(OPTION_STRING[index][2]);
                            tv_option_4.setText(OPTION_STRING[index][3]);
                            tv_option_5.setText(OPTION_STRING[index][4]);
                            tv_option_6.setText(OPTION_STRING[index][5]);
                            tv_option_7.setText(OPTION_STRING[index][6]);
                            tv_option_8.setText(OPTION_STRING[index][7]);
                            break;
                        case 1: // auto_number
                            // changeView(3);
                            // TODO
                            break;
                        case 2: // auto_class
                            // changeView(4);
                            // TODO
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "조건을 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // frame 내의 View 변경
    protected void changeView(int index) {
        // LayoutInflater 초기화.
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FrameLayout frame = findViewById(R.id.auto_frame);
        if (frame.getChildCount() > 0) {
            // FrameLayout에서 뷰 삭제.
            frame.removeViewAt(0);
        }

        // XML에 작성된 레이아웃을 View 객체로 변환.
        View view = null;
        viewIndex = index;
        temp_id = -1;
        select = -1;
        switch (viewIndex) {
            case 0: // view_auto_cube
                view = inflater.inflate(R.layout.view_auto_cube, frame, false);
                break;
            case 1: // view_auto_setting
                view = inflater.inflate(R.layout.view_auto_setting, frame, false);
                break;
            case 2: // view_auto_option
                view = inflater.inflate(R.layout.view_auto_option, frame, false);
                break;
        }

        // FrameLayout에 뷰 추가.
        if (view != null) {
            frame.addView(view);
        }
    }

    protected void selected(int id) {
        if (temp_id != -1) {
            findViewById(temp_id).setBackgroundColor(Color.TRANSPARENT);
        }
        findViewById(id).setBackground(ContextCompat.getDrawable(this, R.drawable.border_selected));
        temp_id = id;
    }
}
