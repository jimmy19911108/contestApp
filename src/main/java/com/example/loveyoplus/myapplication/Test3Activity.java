package com.example.loveyoplus.myapplication;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.icu.text.DateFormat;
import android.icu.text.NumberFormat;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;

/**
 * Created by loveyoplus on 2017/2/16.
 */

public class Test3Activity extends AppCompatActivity implements View.OnTouchListener {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Handler mHandler;

    RelativeLayout r1;
    ImageView iv[];
    int[][] tag;
    int[] result;
    String answer[];
    TextView timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t3);

        r1 = (RelativeLayout)findViewById(R.id.r1);
        timer = (TextView) findViewById(R.id.textView2);
        iv =new ImageView[2];
        iv[0] = (ImageView) findViewById(R.id.iv1) ;

        //抓取資源tag[x][0]=獲取圖片id
        tag = new int[9][2];
        //t3_1~t3_9
        for(int i = 0;i<9;i++){
            tag[i][0] = getResources().getIdentifier("t3_"+(i+1),"drawable",getPackageName());
        }
        //作答後對錯結果
        result = new int[2];


        //answer tag[x][1]=可作答數量 每touch一次 tag[x][1]--
        answer = new String[9];
        answer[0]="5,245;357,4;534,295;443,444";tag[0][1]=4;
        answer[1]="43,315;272,524";tag[1][1]=2;
        answer[2]="190,92;296,-84;478,-98;504,40";tag[2][1]=4;
        answer[3]="102,86;504,-4;490,459";tag[3][1]=3;
        answer[4]="140,-92;308,283;387,500";tag[4][1]=3;
        answer[5]="49,286;404,236;542,10";tag[5][1]=3;
        answer[6]="202,-122;419,565";tag[6][1]=2;
        answer[7]="34,19;128,201;425,330";tag[7][1]=3;
        answer[8]="67,116;472,107;554,-43";tag[8][1]=3;

        mHandler = new Handler();
        mHandler.post(countdowntimer);


        iv[0].setImageResource(tag[0][0]);
        iv[0].setTag(tag[0][0]);
        iv[0].setOnTouchListener(this);



    }
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
                    //iv[0].setVisibility(View.INVISIBLE);


                }
            }.start();

        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {

            result[0]++;

            int[] viewCoords = new int[2];
            int touchX = (int) event.getX();
            int touchY = (int) event.getY();
            int imageX ; // viewCoords[0] is the X coordinate
            int imageY ; // viewCoords[1] is the y coordinate

            //Log.e("resource");
            iv[0].getLocationOnScreen(viewCoords);
            imageX = touchX - viewCoords[0];
            imageY = touchY - viewCoords[1];
            //Log.v("Touch x >>>",touchX+"");
            //Log.v("Touch y >>>",touchY+"\n");
            Log.v("Image x >>>",imageX+"");
            Log.v("Image y >>>",imageY+"\n");

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            ImageView ivX = new ImageView(this);

            //圈選大小
            params.width = 200;
            params.height = 200;
            ivX = new ImageView(this.getApplicationContext());
            ivX.setImageResource(R.drawable.circle);
            //圈圈位置

            for(int i = 0 ;i<9;i++) {
                //Log.d("\tv.getTag", v.getTag() + "");
                //Log.d("\ttag", tag[i][0] + "");
                if (v.getTag().equals(tag[i][0])) {
                    tag[i][1]--;
                    //判斷是否為答案IsAnswer((int)第幾題,x,y)
                    int p[] = IsAnswer(i, imageX, imageY);
                    if (p != null) {
                        //Log.e("p", p[0] + " " + p[1]);
                        params.setMargins(p[0] - 100, p[1] + 100, 0, 0);
                        ivX.setLayoutParams(params);
                        r1.addView(ivX);
                        result[1]++;
                        result[0]--;

                    }
                    //if作答次數歸零 換下一題refresh UI
                    if (tag[i][1] == 0) {

                        ((ViewGroup)iv[0].getParent()).removeAllViews();
                        if(i<8) {
                            iv[0].setImageResource(tag[i + 1][0]);
                            iv[0].setTag(tag[i + 1][0]);
                            r1.addView(iv[0]);
                        }
                        break;
                    }
                }
            }
        ((TextView)findViewById(R.id.textView5)).setText("O:"+result[1]+"\nX:"+result[0]);
        return false;
    }
    //
    public int[] IsAnswer(int answerIndex,int x,int y){
        String a[]=answer[answerIndex].split(";");
        String position=x+","+y;
        for(int i=0;i<a.length;i++){
            int tx = Integer.parseInt(a[i].split(",")[0]);
            int ty = Integer.parseInt(a[i].split(",")[1]);
            if(x<tx+100&&x>tx-100&&y<ty+100&&y>ty-100){
                a[i].replace(position,"");
                int[] p = {tx,ty};
                return p;
            }
        }
        return null;

    }

}
