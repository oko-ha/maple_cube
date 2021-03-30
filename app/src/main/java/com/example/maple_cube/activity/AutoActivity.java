package com.example.maple_cube.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.maple_cube.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private int option_index;
    private int[] selected_option = {-1, -1, -1};

    private int number;
    private Button btn_OK;

    private static final String[][] ITEMS_STRING = {{"공격력", "마력", "몬스터 방어율 무시"},
            {"보스 몬스터 공격 시 데미지"},
            {"올스텟", "Hp", "STR", "DEX", "INT", "LUK"},
            {"스킬 재사용 대기시간 감소"},
            {"크리티컬 데미지"},
            {"아이템 드롭률 or 메소 획득량"},
            {"공격력", "마력"}};
    private static final Integer[][] ITEMS_INTEGER = {{2, 3, 4},
            {1},
            {5, 6, 7, 8, 9, 10},
            {11},
            {12},
            {13},
            {2, 3}};
    private static final String[] AUTO_OPTION =
            {"상관 없음", "보스 몬스터 공격 시 데미지", "공격력", "마력", "몬스터 방어율 무시",
                    "올스텟", "Hp", "STR", "DEX", "INT", "LUK", "스킬 재사용 대기시간 감소",
                    "크리티컬 데미지", "아이템 드롭률 or 메소 획득량"};

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

    // view_auto_option
    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
            // String selectedItem = (String) adapterView.getSelectedItem();
            switch (adapterView.getId()) {
                case R.id.spinner_0:
                    selected_option[0] = getSelectedOption(option_index, pos, false);
                    break;
                case R.id.spinner_1:
                    selected_option[1] = getSelectedOption(option_index, pos, true);
                    break;
                case R.id.spinner_2:
                    if (option_index == 1)
                        selected_option[2] = getSelectedOption(6, pos, true);
                    else
                        selected_option[2] = getSelectedOption(option_index, pos, true);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

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
                            final int[] CATEGORY_POTENTIAL_INDEX = {1, 1, 0, 3, 2, 2, 2, 4, 2, 5, 2};
                            final int[] CATEGORY_BONUS_INDEX = {1, 1, 0, 3, 2, 2, 2, 4, 2, 2, 2};
                            option_index = selected_cube != BONUS_CUBE ? CATEGORY_POTENTIAL_INDEX[category] : CATEGORY_BONUS_INDEX[category];
                            ArrayList<String> items_spinner_0 = setArrayList(option_index, false);
                            ArrayList<String> items_spinner_1 = setArrayList(option_index, true);
                            ArrayList<String> items_spinner_2 = option_index == 1 ? setArrayList(6, true) : setArrayList(option_index, true);

                            Spinner[] spinner = new Spinner[3];
                            for (int i = 0; i < 3; i++) {
                                spinner[i] = findViewById(getResources().getIdentifier("spinner_" + i, "id", getPackageName()));
                                spinner[i].setOnItemSelectedListener(onItemSelectedListener);
                                try {
                                    Field popup = Spinner.class.getDeclaredField("mPopup");
                                    popup.setAccessible(true);
                                    ListPopupWindow window = (ListPopupWindow) popup.get(spinner[i]);
                                    assert window != null;
                                    window.setHeight(850);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            MySpinnerAdapter adapter_0 = new MySpinnerAdapter(getApplicationContext(),
                                    android.R.layout.simple_spinner_item,
                                    items_spinner_0);
                            adapter_0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner[0].setAdapter(adapter_0);
                            spinner[0].setSelection(adapter_0.getCount());

                            MySpinnerAdapter adapter_1 = new MySpinnerAdapter(getApplicationContext(),
                                    android.R.layout.simple_spinner_item,
                                    items_spinner_1);
                            adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner[1].setAdapter(adapter_1);
                            spinner[1].setSelection(adapter_1.getCount());

                            MySpinnerAdapter adapter_2 = new MySpinnerAdapter(getApplicationContext(),
                                    android.R.layout.simple_spinner_item,
                                    items_spinner_2);
                            adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner[2].setAdapter(adapter_2);
                            spinner[2].setSelection(adapter_2.getCount());
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
                if (hasMinusValue(selected_option)) {
                    Intent intent = new Intent();
                    intent.putExtra("selected_cube", selected_cube);
                    intent.putExtra("selected_setting", selected_setting);
                    intent.putExtra("selected_option", selected_option);
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

    // 선택 테두리 설정
    protected void selected(int id) {
        if (temp_id != -1) {
            findViewById(temp_id).setBackgroundColor(Color.TRANSPARENT);
        }
        findViewById(id).setBackground(ContextCompat.getDrawable(this, R.drawable.border_selected));
        temp_id = id;
    }

    protected boolean hasMinusValue(int[] list) {
        for (int i : list)
            if (i < 0)
                return false;
        return true;
    }

    // spinner를 위한 ArrayList에 item 추가
    protected ArrayList<String> setArrayList(int index, boolean check) {
        ArrayList<String> items = new ArrayList<>();
        switch (index) {
            case 0:
                items.addAll(Arrays.asList(ITEMS_STRING[0]));
                break;
            case 1:
                items.addAll(Arrays.asList(ITEMS_STRING[1]));
                items.addAll(Arrays.asList(ITEMS_STRING[0]));
                break;
            case 2:
                items.addAll(Arrays.asList(ITEMS_STRING[2]));
                break;
            case 3:
                items.addAll(Arrays.asList(ITEMS_STRING[3]));
                items.addAll(Arrays.asList(ITEMS_STRING[2]));
                break;
            case 4:
                items.addAll(Arrays.asList(ITEMS_STRING[4]));
                items.addAll(Arrays.asList(ITEMS_STRING[2]));
                break;
            case 5:
                items.addAll(Arrays.asList(ITEMS_STRING[5]));
                items.addAll(Arrays.asList(ITEMS_STRING[2]));
                break;
            case 6:
                items.addAll(Arrays.asList(ITEMS_STRING[6]));
                break;
        }
        if (check)
            items.add("상관 없음");
        items.add("옵션");
        return items;
    }

    protected int getSelectedOption(int index, int pos, boolean check) {
        int selected_option;
        ArrayList<Integer> items = new ArrayList<>();
        switch (index) {
            case 0:
                items.addAll(Arrays.asList(ITEMS_INTEGER[0]));
                break;
            case 1:
                items.addAll(Arrays.asList(ITEMS_INTEGER[1]));
                items.addAll(Arrays.asList(ITEMS_INTEGER[0]));
                break;
            case 2:
                items.addAll(Arrays.asList(ITEMS_INTEGER[2]));
                break;
            case 3:
                items.addAll(Arrays.asList(ITEMS_INTEGER[3]));
                items.addAll(Arrays.asList(ITEMS_INTEGER[2]));
                break;
            case 4:
                items.addAll(Arrays.asList(ITEMS_INTEGER[4]));
                items.addAll(Arrays.asList(ITEMS_INTEGER[2]));
                break;
            case 5:
                items.addAll(Arrays.asList(ITEMS_INTEGER[5]));
                items.addAll(Arrays.asList(ITEMS_INTEGER[2]));
                break;
            case 6:
                items.addAll(Arrays.asList(ITEMS_INTEGER[6]));
                break;
        }
        if (check)
            items.add(0);
        items.add(-1);
        selected_option = items.get(pos);
        return selected_option;
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

    // spinner adapter
    protected static class MySpinnerAdapter extends ArrayAdapter<String> {
        public MySpinnerAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (position == getCount()) {
                ((TextView) view.findViewById(android.R.id.text1)).setText("");
                ((TextView) view.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
            }
            return view;
        }

        @Override
        public int getCount() {
            return super.getCount() - 1;
        }
    }

    // 화면 밖 터치 통제
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction()!=MotionEvent.ACTION_OUTSIDE;
    }
}
