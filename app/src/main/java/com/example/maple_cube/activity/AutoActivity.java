package com.example.maple_cube.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
    private int category_image;

    private int temp_id;
    private int select;
    private int viewIndex;

    private int selected_cube;
    private int selected_setting;
    private int selected_final;

    private int number;
    private Button btn_OK;

    private static final String[][] OPTION_STRING = {
            {"보보○", "공공○", "마마○", "보보방", "보보공", "보공공", "보보마", "보마마"},
            {"공공○", "마마○", "공공공", "마마마", "방공공", "방마마", "", ""},
            {"크크○", "크크힘", "크크덱", "크크인", "크크럭", "크크크", "", ""},
            {"올올올", "HpHpHp", "힘힘힘", "덱덱덱", "인인인", "럭럭럭", "", ""},
            {"올올올", "HpHpHp", "힘힘힘", "덱덱덱", "인인인", "럭럭럭", "쿨쿨○", "쿨쿨쿨"},
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
        category_image = intent.getIntExtra("category_image", -1);

        btn_OK = findViewById(R.id.btn_OK);

        // activity_auto
        btn_OK.setOnClickListener(onClickListener);
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
                case R.id.btn_option_0:
                    selected(R.id.btn_option_0);
                    break;
                case R.id.btn_option_1:
                    selected(R.id.btn_option_1);
                    select += 1;
                    break;
                case R.id.btn_option_2:
                    selected(R.id.btn_option_2);
                    select += 2;
                    break;
                case R.id.btn_option_3:
                    selected(R.id.btn_option_3);
                    select += 3;
                    break;
                case R.id.btn_option_4:
                    selected(R.id.btn_option_4);
                    select += 4;
                    break;
                case R.id.btn_option_5:
                    selected(R.id.btn_option_5);
                    select += 5;
                    break;
                case R.id.btn_option_6:
                    selected(R.id.btn_option_6);
                    select += 6;
                    break;
                case R.id.btn_option_7:
                    selected(R.id.btn_option_7);
                    select += 7;
                    break;
                // view_auto_number
                case R.id.btn_plus_10:
                    plusNumber(10);
                    break;
                case R.id.btn_plus_100:
                    plusNumber(100);
                    break;
                case R.id.btn_plus_1000:
                    plusNumber(1000);
                    break;
                // view_auto_class
                case R.id.btn_epic:
                    selected(R.id.btn_epic);
                    setFutureBorder(2);
                    select = 2;
                    break;
                case R.id.btn_unique:
                    selected(R.id.btn_unique);
                    setFutureBorder(3);
                    select = 3;
                    break;
                case R.id.btn_legendary:
                    selected(R.id.btn_legendary);
                    setFutureBorder(4);
                    select = 4;
                    break;
            }
        }
    };

    protected void onViewCreate(int selection) {
        switch (viewIndex) {
            case 0: // view_auto_cube
                if (selection != -1) {
                    selected_cube = selection;
                    changeView(1);
                    TextView tv_selected_cube = findViewById(R.id.tv_selected_cube);
                    ImageView iv_selected_cube = findViewById(R.id.iv_selected_cube);
                    switch (selected_cube) {
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
                    selected_setting = selection;
                    btn_OK.setText("확인");
                    switch (selection) {
                        case 0: // go to auto_option
                            changeView(2);
                            final int[] CATEGORY_INDEX = {0, 0, 1, 4, 3, 3, 3, 2, 3, 5, 3};
                            int index = CATEGORY_INDEX[category];
                            select = index * 10;
                            int max_i = 6;
                            // set OnClickListener
                            if (index == 0 || index == 4 || index == 5) {
                                max_i = 8;
                            }
                            for (int i = 0; i < max_i; i++)
                                findViewById(getResources().getIdentifier("btn_option_" + i, "id", getPackageName())).setOnClickListener(onClickListener);
                            // set TextView
                            TextView[] tv_option = new TextView[8];
                            for (int i = 0; i < max_i; i++) {
                                tv_option[i] = findViewById(getResources().getIdentifier("tv_option_" + i, "id", getPackageName()));
                                tv_option[i].setText(OPTION_STRING[index][i]);
                            }
                            break;
                        case 1: // go to auto_number
                            changeView(3);
                            ImageView iv_selected_cube = findViewById(R.id.iv_selected_cube);
                            switch (selected_cube) {
                                case RED_CUBE:
                                    iv_selected_cube.setImageResource(R.drawable.red_cube);
                                    break;
                                case BLACK_CUBE:
                                    iv_selected_cube.setImageResource(R.drawable.black_cube);
                                    break;
                                case BONUS_CUBE:
                                    iv_selected_cube.setImageResource(R.drawable.bonus_cube);
                                    break;
                            }
                            number = 0;
                            findViewById(R.id.btn_plus_10).setOnClickListener(onClickListener);
                            findViewById(R.id.btn_plus_100).setOnClickListener(onClickListener);
                            findViewById(R.id.btn_plus_1000).setOnClickListener(onClickListener);
                            break;
                        case 2: // go to auto_class
                            changeView(4);
                            setCategoryImage();
                            findViewById(R.id.btn_epic).setOnClickListener(onClickListener);
                            findViewById(R.id.btn_unique).setOnClickListener(onClickListener);
                            findViewById(R.id.btn_legendary).setOnClickListener(onClickListener);
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "조건을 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2: // view_auto_option
                if (selection != -1) {
                    selected_final = selection;
                    Intent intent = new Intent();
                    intent.putExtra("selected_cube", selected_cube);
                    intent.putExtra("selected_setting", selected_setting);
                    intent.putExtra("selected_final", selected_final);
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                } else {
                    Toast.makeText(getApplicationContext(), "옵션을 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3: // view_auto_number
                if (number > 0) {
                    selected_final = number;
                    Intent intent = new Intent();
                    intent.putExtra("selected_cube", selected_cube);
                    intent.putExtra("selected_setting", selected_setting);
                    intent.putExtra("selected_final", selected_final);
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                } else {
                    Toast.makeText(getApplicationContext(), "개수를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4: // view_auto_class
                if (selection != -1) {
                    selected_final = selection;
                    Intent intent = new Intent();
                    intent.putExtra("selected_cube", selected_cube);
                    intent.putExtra("selected_setting", selected_setting);
                    intent.putExtra("selected_final", selected_final);
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                } else {
                    Toast.makeText(getApplicationContext(), "등급을 선택하세요.", Toast.LENGTH_SHORT).show();
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
            case 3: // view_auto_number
                view = inflater.inflate(R.layout.view_auto_number, frame, false);
                break;
            case 4:// view_auto_class
                view = inflater.inflate(R.layout.view_auto_class, frame, false);
                break;
        }

        // FrameLayout에 뷰 추가.
        if (view != null) {
            frame.addView(view);
        }
    }

    // btn_plus로 number 제어
    protected void plusNumber(int plus) {
        EditText et_number = findViewById(R.id.et_number);
        if (!et_number.getText().toString().equals("")) {
            number = Integer.parseInt(et_number.getText().toString());
        }
        number += plus;
        if (number > 9999) {
            Toast.makeText(getApplicationContext(), "1 ~ 9999 사이의 값만 가능합니다. ", Toast.LENGTH_SHORT).show();
            number -= plus;
        }
        String text = Integer.toString(number);
        et_number.setText(text);
    }

    // 선택 테두리 설정
    protected void selected(int id) {
        if (temp_id != -1) {
            findViewById(temp_id).setBackgroundColor(Color.TRANSPARENT);
        }
        findViewById(id).setBackground(ContextCompat.getDrawable(this, R.drawable.border_selected));
        temp_id = id;
    }

    // view_auto_class에서 image 처리
    protected void setCategoryImage() {
        ImageView current_image = findViewById(R.id.current_image);
        ImageView future_image = findViewById(R.id.future_image);

        // 테두리 설정
        int current_class;
        if (selected_cube == BONUS_CUBE)
            current_class = bonus_class;
        else
            current_class = potential_class;

        switch (current_class) {
            case 1:
                current_image.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_rare));
                break;
            case 2:
                current_image.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_epic));
                break;
            case 3:
                current_image.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_unique));
                break;
            case 4:
                current_image.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_legendary));
                break;
        }

        // 이미지 설정
        switch (category_image) {
            case -1:
                Toast.makeText(this, "Error with select value :-1", Toast.LENGTH_SHORT).show();
                break;
            case 0: // 무기
                current_image.setImageResource(R.drawable.weapon);
                future_image.setImageResource(R.drawable.weapon);
                break;
            case 1: // 보조무기
                current_image.setImageResource(R.drawable.extra_weapon);
                future_image.setImageResource(R.drawable.extra_weapon);
                break;
            case 2: // 엠블렘
                current_image.setImageResource(R.drawable.emblem);
                future_image.setImageResource(R.drawable.emblem);
                break;
            case 3: // 모자
                current_image.setImageResource(R.drawable.hat);
                future_image.setImageResource(R.drawable.hat);
                break;
            case 4: // 상의
                current_image.setImageResource(R.drawable.top);
                future_image.setImageResource(R.drawable.top);
                break;
            case 5: // 한벌옷
                current_image.setImageResource(R.drawable.suits);
                future_image.setImageResource(R.drawable.suits);
                break;
            case 6: // 하의
                current_image.setImageResource(R.drawable.pants);
                future_image.setImageResource(R.drawable.pants);
                break;
            case 7: // 신발
                current_image.setImageResource(R.drawable.shoes);
                future_image.setImageResource(R.drawable.shoes);
                break;
            case 8: // 장갑
                current_image.setImageResource(R.drawable.glove);
                future_image.setImageResource(R.drawable.glove);
                break;
            case 9: // 망토
                current_image.setImageResource(R.drawable.cape);
                future_image.setImageResource(R.drawable.cape);
                break;
            case 10: // 어깨장식
                current_image.setImageResource(R.drawable.shoulder);
                future_image.setImageResource(R.drawable.shoulder);
                break;
            case 11: // 벨트
                current_image.setImageResource(R.drawable.belt);
                future_image.setImageResource(R.drawable.belt);
                break;
            case 12: // 얼굴장식
                current_image.setImageResource(R.drawable.face);
                future_image.setImageResource(R.drawable.face);
                break;
            case 13: // 눈장식
                current_image.setImageResource(R.drawable.eye);
                future_image.setImageResource(R.drawable.eye);
                break;
            case 14: // 귀고리
                current_image.setImageResource(R.drawable.earring);
                future_image.setImageResource(R.drawable.earring);
                break;
            case 15: // 반지
                current_image.setImageResource(R.drawable.ring);
                future_image.setImageResource(R.drawable.ring);
                break;
            case 16: // 펜던트
                current_image.setImageResource(R.drawable.pendant);
                future_image.setImageResource(R.drawable.pendant);
                break;
            case 17: // 기계심장
                current_image.setImageResource(R.drawable.machine_heart);
                future_image.setImageResource(R.drawable.machine_heart);
                break;
        }
    }

    // future_image 테두리 설정
    protected void setFutureBorder(int id) {
        ImageView future_image = findViewById(R.id.future_image);
        switch (id) {
            case 2:
                future_image.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_epic));
                break;
            case 3:
                future_image.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_unique));
                break;
            case 4:
                future_image.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_legendary));
                break;
        }
    }
}
