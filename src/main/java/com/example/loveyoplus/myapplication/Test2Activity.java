package com.example.loveyoplus.myapplication;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by loveyoplus on 2017/2/16.
 */

public class Test2Activity extends AppCompatActivity implements View.OnClickListener {
    Button[] btn;
    TextView tv,timer,tvAnswer,tvResult;
    RelativeLayout r ;
    int[] viewSize;
    Button chgbtn;
    String[] answer;
    int[] result;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t2);

        result = new int[2];
        chgbtn = (Button)findViewById(R.id.button);
        btn = new Button[10];
        btn[0]=(Button)findViewById(R.id.button1);
        btn[1]=(Button)findViewById(R.id.button2);
        btn[2]=(Button)findViewById(R.id.button3);
        btn[3]=(Button)findViewById(R.id.button4);
        btn[4]=(Button)findViewById(R.id.button5);
        btn[5]=(Button)findViewById(R.id.button6);
        btn[6]=(Button)findViewById(R.id.button7);
        btn[7]=(Button)findViewById(R.id.button8);
        btn[8]=(Button)findViewById(R.id.button9);
        tv =(TextView)findViewById(R.id.textView);
        timer=(TextView)findViewById(R.id.tvtimer);
        tvAnswer = (TextView)findViewById(R.id.textView3);
        tvResult = (TextView)findViewById(R.id.textView4);
        r = (RelativeLayout) findViewById(R.id.r1);
        for(int i = 0 ;i<9;i++){

            btn[i].setOnClickListener(this);
        }

        //get layout size
        viewSize = new int[2];
        viewSize[1]=getViewSize()[0];
        viewSize[0]=getViewSize()[1];
        /*
                    set button position
                    set button num
                */
        setBtn(viewSize);
        setQuestion();
        mHandler = new Handler();
        mHandler.post(countdowntimer);



    }
    //timer
    private Runnable countdowntimer = new Runnable() {
        public void run() {
            new CountDownTimer(10000, 1000) {

                @Override

                public void onTick(long millisUntilFinished) {
                    //倒數秒數中要做的事

                    timer.setText("倒數時間:"+new SimpleDateFormat("m").format(millisUntilFinished)+":"+ new SimpleDateFormat("s").format(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    timer.setText("倒數時間:結束");
                    disableBtn();

                }
            }.start();

        }
    };
    //鎖住所有按鈕
    public void disableBtn(){
        for(int i = 0 ;i<9;i++){
            btn[i].setVisibility(View.INVISIBLE);
        }
    }
    //設定答案
    public void setQuestion(){
        String randomNum[]={"一","二","三","四","五","六","七","八","九"};
        answer = new String[3];
        tv.setText("限時時間內找出數字:");
        String s="";
        for(int i = 0 ;i<3;i++){
            Random random = new Random();
            int selected= random.nextInt(9-i);
            answer[i]=randomNum[selected];
            randomNum[selected]=randomNum[8-i];
            if (i>0 )
                s+=","+answer[i];
            else
                s+=answer[0];


        }
        tvAnswer.setText(s);
    }
    //設定按鈕位置及按鈕數字
    public void setBtn(int[] viewSize){

        int hdiv5=viewSize[0]/5;
        int hdiv3=viewSize[0]/3;
        int wdiv3=viewSize[1]/4;
        int wdiv5=viewSize[1]/5;
        int widRandom;

        //setPosition
        RelativeLayout.LayoutParams params;
        for(int i = 0 ;i<9;i++){
            btn[i].setVisibility(View.VISIBLE);
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.width=hdiv5;
            params.height=hdiv5;
            int j=i+1;
            int wRan=/* 0;*/ (int)(Math.random()*(wdiv3-hdiv5));
            int hRan=/* 0;*/ (int)(Math.random()*(hdiv3-hdiv5));
            if (i<3)
                params.setMargins(i%3*wdiv3+wRan,0+hRan,0,0);
            else if(i<6)
                params.setMargins(i%3*wdiv3+wRan,hdiv3+hRan,0,0);
            else
                params.setMargins(i%3*wdiv3+wRan,hdiv3*2+hRan,0,0);
            btn[i].setLayoutParams(params);
        }
        String randomNum[]={"一","二","三","四","五","六","七","八","九"};
        for(int i = 0 ;i<9;i++){
            Random random = new Random();
            int selected= random.nextInt(9-i);
            btn[i].setText(randomNum[selected]+"");
            randomNum[selected]=randomNum[8-i];

        }


    }
    //得到relativelayout大小
    public int[] getViewSize(){

        r.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = r.getMeasuredWidth();
        int height = r.getMeasuredHeight();
        int[] viewSize={width,height};
        return viewSize;

    }
    //testing button
    public void click(View view) {


        //setBtn(viewSize);
        //setQuestion();
    }

    @Override
    public void onClick(View v) {
        for(int i=0;i<9;i++) {
            if ( v==btn[i]){
                btn[i].setVisibility(View.INVISIBLE);
                result[0]++;
                for(int j=0;j<3;j++) {
                    if (answer[j].equals(btn[i].getText().toString())) {
                        result[1]++;
                        result[0]--;
                    }

                }
                if ((result[0]+result[1])%3==0){
                    setBtn(viewSize);
                    setQuestion();
                }
                tvResult.setText("O:"+result[1]+"\nX:"+result[0]);
            }
        }
    }
}
