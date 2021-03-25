package com.example.maple_cube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView tv_potential;
    private TextView tv_potential_1;
    private TextView tv_potential_2;
    private TextView tv_potential_3;

    private TextView tv_bonus;
    private TextView tv_bonus_1;
    private TextView tv_bonus_2;
    private TextView tv_bonus_3;

    private Button btn_potential;
    private Button btn_bonus;
    private ImageButton btn_select_category;

    private static final String[] class_list = {"normal_", "rare_", "epic_", "unique_", "legendary_"};
    private int potential_class;
    private int bonus_class;
    private int cube;
    private static final int red_cube = 0;
    private static final int black_cube = 1;
    private static final int bonus_cube = 2;
    //  0  /   1   /  2   / 3 /     4     / 5  / 6 / 7  /        8        /  9   /  10
    // 무기/보조무기/엠블렘/모자/상의,한벌옷/하의/신발/장갑/망토,벨트,어깨장식/장신구/기계심장
    //0: 무기류, 1: 무기, 2: 보조무기, 3: 장비류, 4: 방어구, 5: 모자, 6: 상의, 7: 하의, 8: 신발, 9: 장갑, 10: 장신구
    private static final int[][] category = {{0, 1}, {0, 2}, {0}, {3, 4, 5}, {3, 4, 6}, {3, 4, 7}, {3, 4, 8}, {3, 4, 9}, {3, 4}, {3, 10}, {3}};
    private int kind;

    private int black_count = 0;
    private int red_count = 0;
    private int bonus_count = 0;
    private TextView tv_red_count;
    private TextView tv_black_count;
    private TextView tv_bonus_count;

    private Context mContext;
    private PotentialDB potentialDB;
    private BonusDB bonusDB;

    private TextView tv_auto;
    private LottieAnimationView btn_auto;
    private boolean autoStop = false;


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
        tv_potential_1 = findViewById(R.id.tv_potential_1);
        tv_potential_2 = findViewById(R.id.tv_potential_2);
        tv_potential_3 = findViewById(R.id.tv_potential_3);

        tv_bonus = findViewById(R.id.tv_bonus);
        tv_bonus_1 = findViewById(R.id.tv_bonus_1);
        tv_bonus_2 = findViewById(R.id.tv_bonus_2);
        tv_bonus_3 = findViewById(R.id.tv_bonus_3);

        tv_red_count = findViewById(R.id.tv_red_count);
        tv_black_count = findViewById(R.id.tv_black_count);
        tv_bonus_count = findViewById(R.id.tv_bonus_count);

        btn_select_category = findViewById(R.id.btn_select_category);
        btn_potential = findViewById(R.id.btn_potential);
        btn_bonus = findViewById(R.id.btn_bonus);

        btn_auto = findViewById(R.id.btn_auto);
        tv_auto = findViewById(R.id.tv_auto);

        btn_select_category.setOnClickListener(onClickListener);
        btn_potential.setOnClickListener(onClickListener);
        btn_bonus.setOnClickListener(onClickListener);
        btn_auto.setOnClickListener(onClickListener);
        findViewById(R.id.btn_black_cube).setOnClickListener(onClickListener);
        findViewById(R.id.btn_red_cube).setOnClickListener(onClickListener);
        findViewById(R.id.btn_bonus_cube).setOnClickListener(onClickListener);

        cube = black_cube;
        potential_class = 1;
        bonus_class = 1;
        kind = 0;

        btn_auto.setProgress(1f);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                // 카테고리 선택
                case R.id.btn_select_category:
                    Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivityForResult(intent, 0);
                    break;
                // 윗잠재 등급 선택
                case R.id.btn_potential:
                    setPotentialDefault();
                    break;
                // 밑잠재 등급 선택
                case R.id.btn_bonus:
                    setBonusDefault();
                    break;
                // 레드 큐브
                case R.id.btn_red_cube:
                    cube = red_cube;
                    red_count++;
                    rollPotentialCube();
                    break;
                // 블랙 큐브
                case R.id.btn_black_cube:
                    cube = black_cube;
                    black_count++;
                    rollPotentialCube();
                    break;
                // 에디셔널 큐브
                case R.id.btn_bonus_cube:
                    cube = bonus_cube;
                    bonus_count++;
                    rollBonusCube();
                    break;
                // AUTO
                case R.id.btn_auto:
                    setAuto(cube);
                    break;
            }
            setColor();
            setCount();
        }
    };

    // Auto로 사용할 큐브 선택
    protected void selectCube(){
        // 0, 1, 2로 데이터를 가져온다고 가정
        // TODO
    }

    // Auto 기능
    protected void setAuto(final int selection) {
        // 큐브 선택 runnable
        final Runnable rollCube = new Runnable() {
            @Override
            public void run() {
                switch (selection){
                    case red_cube:
                        cube = red_cube;
                        red_count++;
                        rollPotentialCube();
                        break;
                    case black_cube:
                        cube = black_cube;
                        black_count++;
                        rollPotentialCube();
                        break;
                    case bonus_cube:
                        bonus_count++;
                        rollBonusCube();
                        break;
                }
                setColor();
                setCount();
            }
        };

        // 스레드가 중지될 때 실행되는 runnable
        final Runnable threadStop = new Runnable() {
            @Override
            public void run() {
                changeAuto();
                autoStop = false;
            }
        };

        // 스레드
        class autoThread implements Runnable {
            @Override
            public void run() {
                while (potential_class < 4 && autoStop) {
                    // 스레드에 runnable 전달
                    runOnUiThread(rollCube);
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(threadStop);
            }
        }

        autoThread autoThread = new autoThread();
        Thread thread = new Thread(autoThread);
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

    // category 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: // CategoryActivity
                if (resultCode == Activity.RESULT_OK) {
                    int select = data.getIntExtra("select", -1);
                    setCategoryImage(select);
                    setPotentialDefault();
                    setBonusDefault();
                    setColor();
                    setCount();
                }
                break;
            case 1: // 등급 선택 Activity
                // TODO
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
                kind = 0;
                break;
            case 1: // 보조무기
                btn_select_category.setImageResource(R.drawable.extra_weapon);
                kind = 1;
                break;
            case 2: // 엠블렘
                btn_select_category.setImageResource(R.drawable.emblem);
                kind = 2;
                break;
            case 3: // 모자
                btn_select_category.setImageResource(R.drawable.hat);
                kind = 3;
                break;
            case 4: // 상의
                btn_select_category.setImageResource(R.drawable.top);
                kind = 4;
                break;
            case 5: // 한벌옷
                btn_select_category.setImageResource(R.drawable.suits);
                kind = 4;
                break;
            case 6: // 하의
                btn_select_category.setImageResource(R.drawable.pants);
                kind = 5;
                break;
            case 7: // 신발
                btn_select_category.setImageResource(R.drawable.shoes);
                kind = 6;
                break;
            case 8: // 장갑
                btn_select_category.setImageResource(R.drawable.glove);
                kind = 7;
                break;
            case 9: // 망토
                btn_select_category.setImageResource(R.drawable.cape);
                kind = 8;
                break;
            case 10: // 어깨장식
                btn_select_category.setImageResource(R.drawable.shoulder);
                kind = 8;
                break;
            case 11: // 벨트
                btn_select_category.setImageResource(R.drawable.belt);
                kind = 8;
                break;
            case 12: // 얼굴장식
                btn_select_category.setImageResource(R.drawable.face);
                kind = 9;
                break;
            case 13: // 눈장식
                btn_select_category.setImageResource(R.drawable.eye);
                kind = 9;
                break;
            case 14: // 귀고리
                btn_select_category.setImageResource(R.drawable.earring);
                kind = 9;
                break;
            case 15: // 반지
                btn_select_category.setImageResource(R.drawable.ring);
                kind = 9;
                break;
            case 16: // 펜던트
                btn_select_category.setImageResource(R.drawable.pendant);
                kind = 9;
                break;
            case 17: // 기계심장
                btn_select_category.setImageResource(R.drawable.machine_heart);
                kind = 10;
                break;
        }
    }

    // 윗잠재를 초기 레어 값으로 설정
    protected void setPotentialDefault() {
        potential_class = 1;
        black_count = 0;
        red_count = 0;
        tv_potential_1.setText("잠재능력 1");
        tv_potential_2.setText("잠재능력 2");
        tv_potential_3.setText("잠재능력 3");
    }

    // 밑잠재를 초기 레어 값으로 설정
    protected void setBonusDefault() {
        bonus_class = 1;
        bonus_count = 0;
        tv_bonus_1.setText("잠재능력 1");
        tv_bonus_2.setText("잠재능력 2");
        tv_bonus_3.setText("잠재능력 3");
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

    // 윗잠재 큐브 굴리기
    protected void rollPotentialCube() {
        final int[][] probability = {{60, 18, 3, 10, 1}, {150, 35, 10, 20, 5}};
        final int rare_class_up = 0;
        final int epic_class_up = 1;
        final int unique_class_up = 2;
        final int second_option_up = 3;
        final int third_option_up = 4;
        // 등급업
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

        // 첫 번째 옵션
        tv_potential_1.setText(convertPotentialText(potential_class));
        // 두 번째 옵션 이탈율 적용
        randValue = (int) (Math.random() * 1000) + 1;
        if (randValue <= probability[cube][second_option_up])
            tv_potential_2.setText(convertPotentialText(potential_class));
        else
            tv_potential_2.setText(convertPotentialText(potential_class - 1));
        randValue = (int) (Math.random() * 1000) + 1;
        // 세 번째 옵션 이탈율 적용
        if (randValue <= probability[cube][third_option_up])
            tv_potential_3.setText(convertPotentialText(potential_class));
        else
            tv_potential_3.setText(convertPotentialText(potential_class - 1));
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

        // 등급업
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

        // 첫 번째 옵션
        tv_bonus_1.setText(convertBonusText(bonus_class));
        // 두 번째 옵션 이탈율 적용
        randValue = (int) (Math.random() * 1000000) + 1;
        if (randValue <= probability[option_up])
            tv_bonus_2.setText(convertBonusText(bonus_class));
        else
            tv_bonus_2.setText(convertBonusText(bonus_class - 1));
        // 세 번째 옵션 이탈율 적용
        randValue = (int) (Math.random() * 1000000) + 1;
        if (randValue <= probability[option_up])
            tv_bonus_3.setText(convertBonusText(bonus_class));
        else
            tv_bonus_3.setText(convertBonusText(bonus_class - 1));
    }

    // id 값을 potential.db에서 option을 text로 변환
    protected String convertPotentialText(int current_class) {
        return potentialDB.getOption(class_list[current_class], getWeightedRandom(potentialDB.getList(class_list[current_class], category[kind])));
    }

    // id 값을 bonus.db에서 option을 text로 변환
    protected String convertBonusText(int current_class) {
        return bonusDB.getOption(class_list[current_class], getWeightedRandom(bonusDB.getList(class_list[current_class], category[kind])));
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
}