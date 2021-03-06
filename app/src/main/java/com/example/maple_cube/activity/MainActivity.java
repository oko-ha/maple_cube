package com.example.maple_cube.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.maple_cube.R;
import com.example.maple_cube.database.BonusDB;
import com.example.maple_cube.database.PotentialDB;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime = 0;

    private TextView tv_potential;
    private TextView[] tv_potential_option;
    private int[] potential_auto_option;
    private TextView tv_bonus;
    private TextView[] tv_bonus_option;
    private int[] bonus_auto_option;

    private Button btn_potential;
    private Button btn_bonus;
    private ImageButton btn_select_category;

    private static final String[] CLASS_LIST = {"normal_", "rare_", "epic_", "unique_", "legendary_"};
    private int potential_class;
    private int bonus_class;
    private int cube;
    private static final int RED_CUBE = 0;
    private static final int BLACK_CUBE = 1;
    private static final int BONUS_CUBE = 2;
    //  0  /   1   /  2   / 3 /     4     / 5  / 6 / 7  /        8        /  9   /  10
    // 무기/보조무기/엠블렘/모자/상의,한벌옷/하의/신발/장갑/망토,벨트,어깨장식/장신구/기계심장
    //0: 무기류, 1: 무기, 2: 보조무기, 3: 장비류, 4: 방어구, 5: 모자, 6: 상의, 7: 하의, 8: 신발, 9: 장갑, 10: 장신구
    private static final int[][] CATEGORIES = {{0, 1}, {0, 2}, {0}, {3, 4, 5}, {3, 4, 6}, {3, 4, 7}, {3, 4, 8}, {3, 4, 9}, {3, 4}, {3, 10}, {3}};
    private int category;
    private int category_image;

    private int black_count = 0;
    private int red_count = 0;
    private int bonus_count = 0;
    private TextView tv_red_count;
    private TextView tv_black_count;
    private TextView tv_bonus_count;

    private Context mContext;
    private PotentialDB potentialDB;
    private BonusDB bonusDB;

    private RelativeLayout menu_potential_class;
    private RelativeLayout menu_bonus_class;
    private boolean menu_flag;

    private TextView tv_auto;
    private LottieAnimationView btn_auto;
    private boolean autoStop;

    private ArrayList<Integer> potentialExceptions;
    private int potentialTempException;
    private ArrayList<Integer> bonusExceptions;
    private int bonusTempException;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        setDB("potential.db");
        setDB("bonus.db");

        potentialDB = new PotentialDB(mContext);
        bonusDB = new BonusDB(mContext);

        tv_potential = findViewById(R.id.tv_potential);
        tv_potential_option = new TextView[3];
        for (int i = 0; i < 3; i++)
            tv_potential_option[i] = findViewById(getResources().getIdentifier("tv_potential_" + i, "id", getPackageName()));
        potential_auto_option = new int[3];

        tv_bonus = findViewById(R.id.tv_bonus);
        tv_bonus_option = new TextView[3];
        for (int i = 0; i < 3; i++)
            tv_bonus_option[i] = findViewById(getResources().getIdentifier("tv_bonus_" + i, "id", getPackageName()));
        bonus_auto_option = new int[3];

        tv_red_count = findViewById(R.id.tv_red_count);
        tv_black_count = findViewById(R.id.tv_black_count);
        tv_bonus_count = findViewById(R.id.tv_bonus_count);

        btn_select_category = findViewById(R.id.btn_select_category);
        btn_potential = findViewById(R.id.btn_potential);
        btn_bonus = findViewById(R.id.btn_bonus);

        menu_potential_class = findViewById(R.id.menu_potential_class);
        menu_bonus_class = findViewById(R.id.menu_bonus_class);

        btn_auto = findViewById(R.id.btn_auto);
        tv_auto = findViewById(R.id.tv_auto);

        btn_select_category.setOnClickListener(onClickListener);
        btn_potential.setOnClickListener(onClickListener);
        btn_bonus.setOnClickListener(onClickListener);
        btn_auto.setOnClickListener(onClickListener);
        findViewById(R.id.btn_black_cube).setOnClickListener(onClickListener);
        findViewById(R.id.btn_red_cube).setOnClickListener(onClickListener);
        findViewById(R.id.btn_bonus_cube).setOnClickListener(onClickListener);
        findViewById(R.id.potential_class_rare).setOnClickListener(onClickListener);
        findViewById(R.id.potential_class_epic).setOnClickListener(onClickListener);
        findViewById(R.id.potential_class_unique).setOnClickListener(onClickListener);
        findViewById(R.id.potential_class_legendary).setOnClickListener(onClickListener);
        findViewById(R.id.bonus_class_rare).setOnClickListener(onClickListener);
        findViewById(R.id.bonus_class_epic).setOnClickListener(onClickListener);
        findViewById(R.id.bonus_class_unique).setOnClickListener(onClickListener);
        findViewById(R.id.bonus_class_legendary).setOnClickListener(onClickListener);

        // Initialize
        cube = BLACK_CUBE;
        potential_class = 1;
        bonus_class = 1;
        category_image = 0;
        menu_flag = true;
        setCategoryImage(category_image);
        setPotentialDefault(1);
        setBonusDefault(1);
        setCount();
        setColor();

        potentialExceptions = new ArrayList<>();
        potentialTempException = 0;
        bonusExceptions = new ArrayList<>();
        bonusTempException = 0;

        autoStop = false;
        btn_auto.setProgress(1f);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                // 카테고리 선택
                case R.id.btn_select_category:
                    intent = new Intent(getApplicationContext(), CategoryActivity.class);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
                // 윗잠재 등급 선택
                case R.id.btn_potential:
                    setMenu(true);
                    break;
                // 밑잠재 등급 선택
                case R.id.btn_bonus:
                    setMenu(false);
                    break;
                // 등급 선택
                case R.id.potential_class_rare:
                    selectPotentialClass(1);
                    break;
                case R.id.potential_class_epic:
                    selectPotentialClass(2);
                    break;
                case R.id.potential_class_unique:
                    selectPotentialClass(3);
                    break;
                case R.id.potential_class_legendary:
                    selectPotentialClass(4);
                    break;
                case R.id.bonus_class_rare:
                    selectBonusClass(1);
                    break;
                case R.id.bonus_class_epic:
                    selectBonusClass(2);
                    break;
                case R.id.bonus_class_unique:
                    selectBonusClass(3);
                    break;
                case R.id.bonus_class_legendary:
                    selectBonusClass(4);
                    break;
                // 레드 큐브
                case R.id.btn_red_cube:
                    cube = RED_CUBE;
                    red_count++;
                    rollPotentialCube();
                    break;
                // 블랙 큐브
                case R.id.btn_black_cube:
                    cube = BLACK_CUBE;
                    black_count++;
                    rollPotentialCube();
                    break;
                // 에디셔널 큐브
                case R.id.btn_bonus_cube:
                    cube = BONUS_CUBE;
                    bonus_count++;
                    rollBonusCube();
                    break;
                // AUTO
                case R.id.btn_auto:
                    if (autoStop) {
                        setAuto(-1, -1, -1, null);
                    } else {
                        intent = new Intent(getApplicationContext(), AutoActivity.class);
                        intent.putExtra("potential_class", potential_class);
                        intent.putExtra("bonus_class", bonus_class);
                        intent.putExtra("category", category);
                        intent.putExtra("category_image", category_image);
                        startActivityForResult(intent, 1);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                    break;
            }
            setColor();
            setCount();
        }
    };

    // 등급 선택 메뉴
    protected void setMenu(boolean potential) {
        if (menu_flag) {
            if (potential) {
                menu_potential_class.setVisibility(View.VISIBLE);
                btn_bonus.setEnabled(false);
                menu_potential_class.bringToFront();
            } else {
                menu_bonus_class.setVisibility(View.VISIBLE);
                btn_potential.setEnabled(false);
                menu_bonus_class.bringToFront();
            }
            menu_flag = false;
        } else {
            if (potential) {
                menu_potential_class.setVisibility(View.GONE);
                btn_bonus.setEnabled(true);
            } else {
                menu_bonus_class.setVisibility(View.GONE);
                btn_potential.setEnabled(true);
            }
            menu_flag = true;
        }
    }

    protected void selectPotentialClass(int selected_class) {
        switch (selected_class) {
            case 1:
                potential_class = 1;
                break;
            case 2:
                potential_class = 2;
                break;
            case 3:
                potential_class = 3;
                break;
            case 4:
                potential_class = 4;
                break;
        }
        menu_flag = true;
        btn_bonus.setEnabled(true);
        menu_potential_class.setVisibility(View.GONE);
        setPotentialDefault(potential_class);
    }

    protected void selectBonusClass(int selected_class) {
        switch (selected_class) {
            case 1:
                bonus_class = 1;
                break;
            case 2:
                bonus_class = 2;
                break;
            case 3:
                bonus_class = 3;
                break;
            case 4:
                bonus_class = 4;
                break;
        }
        menu_flag = true;
        btn_potential.setEnabled(true);
        menu_bonus_class.setVisibility(View.GONE);
        setBonusDefault(bonus_class);
    }

    // 화면 밖 터치 시 메뉴 가리기
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        menu_flag = true;
        btn_potential.setEnabled(true);
        btn_bonus.setEnabled(true);
        menu_potential_class.setVisibility(View.GONE);
        menu_bonus_class.setVisibility(View.GONE);
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    // Auto 기능
    protected void setAuto(final int selected_cube, final int selected_setting, final int selected_final, final int[] selected_option) {
        // 큐브 선택 runnable
        final Runnable rollCube = () -> {
            switch (selected_cube) {
                case RED_CUBE:
                    cube = RED_CUBE;
                    red_count++;
                    rollPotentialCube();
                    break;
                case BLACK_CUBE:
                    cube = BLACK_CUBE;
                    black_count++;
                    rollPotentialCube();
                    break;
                case BONUS_CUBE:
                    bonus_count++;
                    rollBonusCube();
                    break;
            }
            setColor();
            setCount();
        };

        // 스레드가 중지될 때 실행되는 runnable
        final Runnable threadStop = () -> {
            changeAuto();
            autoStop = false;
        };

        // auto_option thread
        class autoThread_1 implements Runnable {
            @Override
            public void run() {
                switch (selected_cube) {
                    case RED_CUBE:
                    case BLACK_CUBE:
                        setPotential();
                        break;
                    case BONUS_CUBE:
                        setBonus();
                        break;
                }
                runOnUiThread(threadStop);
            }

            protected void setPotential() {
                ArrayList<Integer> list = getOptionList(selected_option);
                while (isInPotentialList(list, list.size()) && autoStop) {
                    list = getOptionList(selected_option);
                    running();
                }
            }

            protected void setBonus() {
                ArrayList<Integer> list = getOptionList(selected_option);
                while (isInBonusList(list, list.size()) && autoStop) {
                    list = getOptionList(selected_option);
                    running();
                }
            }

            protected void running() {
                runOnUiThread(rollCube);
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            protected boolean isInPotentialList(ArrayList<Integer> list, int number) {
                ArrayList<Integer> tempList = new ArrayList<>(list);
                int num = 0;
                for (int i = 0; i < 3; i++) {
                    if (tempList.contains(potential_auto_option[i])) {
                        tempList.remove((Integer) potential_auto_option[i]);
                        num++;
                    }
                }
                return num != number;
            }

            protected boolean isInBonusList(ArrayList<Integer> list, int number) {
                ArrayList<Integer> tempList = new ArrayList<>(list);
                int num = 0;
                for (int i = 0; i < 3; i++) {
                    if (tempList.contains(bonus_auto_option[i])) {
                        tempList.remove((Integer) bonus_auto_option[i]);
                        num++;
                    }
                }
                return num != number;
            }
        }

        // auto_number thread
        class autoThread_2 implements Runnable {
            @Override
            public void run() {
                int i = 0;
                while (i < selected_final && autoStop) {
                    // 스레드에 runnable 전달
                    runOnUiThread(rollCube);
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                runOnUiThread(threadStop);
            }
        }

        // auto_class thread
        class autoThread_3 implements Runnable {
            @Override
            public void run() {
                switch (selected_cube) {
                    case RED_CUBE:
                    case BLACK_CUBE:
                        setPotential();
                        break;
                    case BONUS_CUBE:
                        setBonus();
                        break;
                }
                runOnUiThread(threadStop);
            }

            protected void setPotential() {
                while (potential_class < selected_final && autoStop)
                    running();
            }

            protected void setBonus() {
                while (bonus_class < selected_final && autoStop)
                    running();
            }

            protected void running() {
                runOnUiThread(rollCube);
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        autoThread_1 autoThread_1 = new autoThread_1();
        autoThread_2 autoThread_2 = new autoThread_2();
        autoThread_3 autoThread_3 = new autoThread_3();
        Thread thread = new Thread();
        switch (selected_setting) {
            case 0: // auto_option
                thread = new Thread(autoThread_1);
                break;
            case 1: // auto_number
                thread = new Thread(autoThread_2);
                break;
            case 2: // auto_class
                thread = new Thread(autoThread_3);
                break;
        }

        if (autoStop) {
            autoStop = false;
        } else {
            autoStop = true;
            changeAuto();
            thread.start();
        }
    }

    // Auto 바꾸기
    protected void changeAuto() {
        if (btn_auto.isAnimating()) {
            btn_auto.setProgress(1f);
            btn_auto.pauseAnimation();
            tv_auto.setTextColor(getResources().getColor(R.color.black));
        } else {
            btn_auto.playAnimation();
            tv_auto.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    // auto_option 설정 값에 따라 분류
    protected ArrayList<Integer> getOptionList(int[] selected_option) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i : selected_option)
            if (i != 0)
                list.add(i);

        return list;
    }


    // 타 Activity에서 data 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: // CategoryActivity
                if (resultCode == Activity.RESULT_OK) {
                    category_image = data.getIntExtra("select", -1);
                    setCategoryImage(category_image);
                    setPotentialDefault(1);
                    setBonusDefault(1);
                    setColor();
                    setCount();
                }
                break;
            case 1: // AutoActivity
                if (resultCode == Activity.RESULT_OK) {
                    int selected_cube;
                    int selected_setting;
                    int selected_final;
                    int[] selected_option;
                    selected_cube = data.getIntExtra("selected_cube", -1);
                    selected_setting = data.getIntExtra("selected_setting", -1);
                    selected_final = data.getIntExtra("selected_final", -1);
                    selected_option = data.getIntArrayExtra("selected_option");
                    setAuto(selected_cube, selected_setting, selected_final, selected_option);
                }
                break;
        }
    }

    // 선택된 카테고리로 이미지 설정
    protected void setCategoryImage(int select) {
        switch (select) {
            case -1:
                Toast.makeText(this, "Error with select value :-1", Toast.LENGTH_SHORT).show();
                break;
            case 0: // 무기
                btn_select_category.setImageResource(R.drawable.weapon);
                category = 0;
                break;
            case 1: // 보조무기
                btn_select_category.setImageResource(R.drawable.extra_weapon);
                category = 1;
                break;
            case 2: // 엠블렘
                btn_select_category.setImageResource(R.drawable.emblem);
                category = 2;
                break;
            case 3: // 모자
                btn_select_category.setImageResource(R.drawable.hat);
                category = 3;
                break;
            case 4: // 상의
                btn_select_category.setImageResource(R.drawable.top);
                category = 4;
                break;
            case 5: // 한벌옷
                btn_select_category.setImageResource(R.drawable.suits);
                category = 4;
                break;
            case 6: // 하의
                btn_select_category.setImageResource(R.drawable.pants);
                category = 5;
                break;
            case 7: // 신발
                btn_select_category.setImageResource(R.drawable.shoes);
                category = 6;
                break;
            case 8: // 장갑
                btn_select_category.setImageResource(R.drawable.glove);
                category = 7;
                break;
            case 9: // 망토
                btn_select_category.setImageResource(R.drawable.cape);
                category = 8;
                break;
            case 10: // 어깨장식
                btn_select_category.setImageResource(R.drawable.shoulder);
                category = 8;
                break;
            case 11: // 벨트
                btn_select_category.setImageResource(R.drawable.belt);
                category = 8;
                break;
            case 12: // 얼굴장식
                btn_select_category.setImageResource(R.drawable.face);
                category = 9;
                break;
            case 13: // 눈장식
                btn_select_category.setImageResource(R.drawable.eye);
                category = 9;
                break;
            case 14: // 귀고리
                btn_select_category.setImageResource(R.drawable.earring);
                category = 9;
                break;
            case 15: // 반지
                btn_select_category.setImageResource(R.drawable.ring);
                category = 9;
                break;
            case 16: // 펜던트
                btn_select_category.setImageResource(R.drawable.pendant);
                category = 9;
                break;
            case 17: // 기계심장
                btn_select_category.setImageResource(R.drawable.machine_heart);
                category = 10;
                break;
        }
    }

    // 윗잠재를 초기 레어 값으로 설정
    protected void setPotentialDefault(int class_number) {
        potential_class = class_number;
        black_count = 0;
        red_count = 0;
        int num;
        for (int i = 0; i < 3; i++) {
            num = i + 1;
            tv_potential_option[i].setText("잠재능력 " + num);
        }
        potential_auto_option = new int[3];
    }

    // 밑잠재를 초기 레어 값으로 설정
    protected void setBonusDefault(int class_number) {
        bonus_class = class_number;
        bonus_count = 0;
        int num;
        for (int i = 0; i < 3; i++) {
            num = i + 1;
            tv_bonus_option[i].setText("잠재능력 " + num);
        }
        bonus_auto_option = new int[3];
    }

    // 개수 설정
    protected void setCount() {
        tv_red_count.setText("x" + red_count);
        tv_black_count.setText("x" + black_count);
        tv_bonus_count.setText("x" + bonus_count);
    }

    // 색깔 설정
    protected void setColor() {
        switch (potential_class) {
            case 1:
                btn_potential.setText("R");
                btn_potential.setTextColor(getResources().getColor(R.color.rare));
                tv_potential.setTextColor(getResources().getColor(R.color.rare));
                btn_select_category.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border_rare));
                break;
            case 2:
                btn_potential.setText("E");
                btn_potential.setTextColor(getResources().getColor(R.color.epic));
                tv_potential.setTextColor(getResources().getColor(R.color.epic));
                btn_select_category.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border_epic));
                break;
            case 3:
                btn_potential.setText("U");
                btn_potential.setTextColor(getResources().getColor(R.color.unique));
                tv_potential.setTextColor(getResources().getColor(R.color.unique));
                btn_select_category.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border_unique));
                break;
            case 4:
                btn_potential.setText("L");
                btn_potential.setTextColor(getResources().getColor(R.color.legendary));
                tv_potential.setTextColor(getResources().getColor(R.color.legendary));
                btn_select_category.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border_legendary));
                break;
        }
        switch (bonus_class) {
            case 1:
                btn_bonus.setText("R");
                btn_bonus.setTextColor(getResources().getColor(R.color.rare));
                tv_bonus.setTextColor(getResources().getColor(R.color.rare));
                break;
            case 2:
                btn_bonus.setText("E");
                btn_bonus.setTextColor(getResources().getColor(R.color.epic));
                tv_bonus.setTextColor(getResources().getColor(R.color.epic));
                break;
            case 3:
                btn_bonus.setText("U");
                btn_bonus.setTextColor(getResources().getColor(R.color.unique));
                tv_bonus.setTextColor(getResources().getColor(R.color.unique));
                break;
            case 4:
                btn_bonus.setText("L");
                btn_bonus.setTextColor(getResources().getColor(R.color.legendary));
                tv_bonus.setTextColor(getResources().getColor(R.color.legendary));
                break;
        }
    }

    // 등급 업 이펙트
    protected void class_up() {
        final RelativeLayout layout_class_up = findViewById(R.id.layout_class_up);
        final LottieAnimationView lottie_class_up = findViewById(R.id.lottie_class_up);
        layout_class_up.setVisibility(View.VISIBLE);
        lottie_class_up.setVisibility(View.VISIBLE);
        lottie_class_up.playAnimation();
        lottie_class_up.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                layout_class_up.setVisibility(View.GONE);
                lottie_class_up.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    // 윗잠재 큐브 굴리기
    protected void rollPotentialCube() {
        final int[][] probability = {{60, 18, 3, 10, 1}, {150, 35, 10, 20, 5}};
        final int rare_class_up = 0;
        final int epic_class_up = 1;
        final int unique_class_up = 2;
        final int second_option_up = 3;
        final int third_option_up = 4;
        int tempClass;
        potentialExceptions.clear();
        potentialTempException = 0;

        // 등급업
        tempClass = potential_class;
        int randValue = (int) (Math.random() * 1000) + 1;
        switch (potential_class) {
            case 1:
                if (randValue <= probability[cube][rare_class_up])
                    potential_class = 2;
                break;
            case 2:
                if (randValue <= probability[cube][epic_class_up])
                    potential_class = 3;
                break;
            case 3:
                if (randValue <= probability[cube][unique_class_up])
                    potential_class = 4;
                break;
        }
        if (tempClass != potential_class) {
            class_up();
        }

        int[][] potential = new int[3][2];

        // 첫 번째 옵션
        potential[0][0] = potential_class;
        potential[0][1] = getPotentialResult(potential_class);
        // 두 번째 옵션 이탈율 적용
        randValue = (int) (Math.random() * 1000) + 1;
        if (randValue <= probability[cube][second_option_up]) {
            potential[1][0] = potential_class;
            potential[1][1] = getPotentialResult(potential_class);
        } else {
            potential[1][0] = potential_class - 1;
            potential[1][1] = getPotentialResult(potential_class - 1);
        }
        randValue = (int) (Math.random() * 1000) + 1;
        // 세 번째 옵션 이탈율 적용
        if (randValue <= probability[cube][third_option_up]) {
            potential[2][0] = potential_class;
            potential[2][1] = getPotentialResult(potential_class);
        } else {
            potential[2][0] = potential_class - 1;
            potential[2][1] = getPotentialResult(potential_class - 1);
        }
        for (int i = 0; i < 3; i++) {
            potential_auto_option[i] = potentialDB.getAutoOption(CLASS_LIST[potential[i][0]], potential[i][1]);
            tv_potential_option[i].setText(convertPotentialText(potential[i][0], potential[i][1]));
        }
    }

    // 밑잠재 큐브 굴리기
    protected void rollBonusCube() {
        final int[] probability = {47619, 19608, 4975};
        final int rare_class_up = 0;
        final int epic_class_up = 1;
        final int unique_class_up = 2;
        final int rare_option_up = 1;
        final int epic_option_up = 0;
        final int unique_option_up = 1;
        final int legendary_option_up = 2;
        int option_up = -1;
        int tempClass;
        bonusExceptions.clear();
        bonusTempException = 0;

        // 등급업
        tempClass = bonus_class;
        int randValue = (int) (Math.random() * 1000000) + 1;
        switch (bonus_class) {
            case 1:
                if (randValue <= probability[rare_class_up])
                    bonus_class = 2;
                break;
            case 2:
                if (randValue <= probability[epic_class_up])
                    bonus_class = 3;
                break;
            case 3:
                if (randValue <= probability[unique_class_up])
                    bonus_class = 4;
                break;
        }
        if (tempClass != bonus_class) {
            class_up();
        }

        switch (bonus_class) {
            case 1:
                option_up = rare_option_up;
                break;
            case 2:
                option_up = epic_option_up;
                break;
            case 3:
                option_up = unique_option_up;
                break;
            case 4:
                option_up = legendary_option_up;
                break;
        }

        int[][] bonus = new int[3][2];

        // 첫 번째 옵션
        bonus[0][0] = bonus_class;
        bonus[0][1] = getBonusResult(bonus_class);
        // 두 번째 옵션 이탈율 적용
        randValue = (int) (Math.random() * 1000000) + 1;
        if (randValue <= probability[option_up]) {
            bonus[1][0] = bonus_class;
            bonus[1][1] = getBonusResult(bonus_class);
        } else {
            bonus[1][0] = bonus_class - 1;
            bonus[1][1] = getBonusResult(bonus_class - 1);
        }
        // 세 번째 옵션 이탈율 적용
        randValue = (int) (Math.random() * 1000000) + 1;
        if (randValue <= probability[option_up]) {
            bonus[2][0] = bonus_class;
            bonus[2][1] = getBonusResult(bonus_class);
        } else {
            bonus[2][0] = bonus_class - 1;
            bonus[2][1] = getBonusResult(bonus_class - 1);
        }
        for (int i = 0; i < 3; i++) {
            bonus_auto_option[i] = bonusDB.getAutoOption(CLASS_LIST[bonus[i][0]], bonus[i][1]);
            tv_bonus_option[i].setText(convertBonusText(bonus[i][0], bonus[i][1]));
        }
    }

    // potential.db에서 현재 등급에 맞는 option id를 가져옴
    protected int getPotentialResult(int current_class) {
        ArrayList<Integer[]> list = potentialDB.getList(CLASS_LIST[current_class], CATEGORIES[category]);
        Map<Integer, Double> map = new HashMap<>();
        int exception;
        int result;
        // exception 검사
        for (int i = 0; i < list.size(); i++) {
            if (potentialExceptions.size() != 0) {
                for (int j = 0; j < potentialExceptions.size(); j++)
                    if (!list.get(i)[2].equals(potentialExceptions.get(j)))
                        map.put(list.get(i)[0], (double) list.get(i)[1]);
            } else {
                map.put(list.get(i)[0], (double) list.get(i)[1]);
            }
        }
        result = getWeightedRandom(map);
        exception = potentialDB.getException(CLASS_LIST[current_class], result);

        // 1개의 option과 2개의 option exception 분리
        if (exception > 0) {
            if (exception < 3)
                potentialExceptions.add(exception);
            else if (potentialTempException != exception)
                potentialTempException = exception;
            else
                potentialExceptions.add(exception);
        }
        return result;
    }

    // bonus.db에서 현재 등급에 맞는 option id를 가져옴
    protected int getBonusResult(int current_class) {
        ArrayList<Integer[]> list = bonusDB.getList(CLASS_LIST[current_class], CATEGORIES[category]);
        Map<Integer, Double> map = new HashMap<>();
        int exception;
        int result;
        // exception 검사
        for (int i = 0; i < list.size(); i++) {
            if (bonusExceptions.size() != 0) {
                for (int j = 0; j < bonusExceptions.size(); j++)
                    if (!list.get(i)[2].equals(bonusExceptions.get(j)))
                        map.put(list.get(i)[0], (double) list.get(i)[1]);
            } else {
                map.put(list.get(i)[0], (double) list.get(i)[1]);
            }
        }
        result = getWeightedRandom(map);
        exception = bonusDB.getException(CLASS_LIST[current_class], result);

        // 1개의 option과 2개의 option exception 분리
        if (exception > 0) {
            if (exception < 3)
                bonusExceptions.add(exception);
            else if (bonusTempException != exception)
                bonusTempException = exception;
            else
                bonusExceptions.add(exception);
        }
        return result;
    }

    // id 값을 potential.db에서 id를 option의 text로 변환
    protected String convertPotentialText(int current_class, int result) {
        return potentialDB.getOption(CLASS_LIST[current_class], result);
    }

    // id 값을 bonus.db에서 id를 option의 text로 변환
    protected String convertBonusText(int current_class, int result) {
        return bonusDB.getOption(CLASS_LIST[current_class], result);
    }

    // DB 설정
    protected void setDB(String db_name) {
        try {
            if (!getApplicationContext().getDatabasePath(db_name).exists()) {   //DB가 없으면 복사
                copyDB(db_name);
            }
        } catch (Exception e) {
            Log.e("setDB:", "Error with ", e);
        }
    }

    // DB 복사
    // assets의 /databases/db 파일을 설치된 프로그램의 내부 DB공간으로 복사하기
    protected void copyDB(String db_name) {
        Log.d("copyDB:", "COPY START !!!");
        AssetManager manager = getResources().getAssets();
        File file = getApplicationContext().getDatabasePath(db_name);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            InputStream is = manager.open("databases/" + db_name);
            BufferedInputStream bis = new BufferedInputStream(is);

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();

            bos.close();
            fos.close();
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("copyDB:", "Error with ", e);
        }
    }

    // 가중치 랜덤 연산
    protected static <E> E getWeightedRandom(Map<E, Double> weights) {
        E result = null;
        Random random = new Random();
        double bestValue = Double.MAX_VALUE;
        for (E element : weights.keySet()) {
            double value = -Math.log(random.nextDouble()) / weights.get(element);
            if (value < bestValue) {
                bestValue = value;
                result = element;
            }
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        final long FINISH_INTERVAL_TIME = 2000;

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "'이전' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}