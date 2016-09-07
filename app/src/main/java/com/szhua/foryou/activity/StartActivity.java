package com.szhua.foryou.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.szhua.foryou.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import top.wefor.circularanim.CircularAnim;

public class StartActivity extends AppCompatActivity {

    @InjectView(R.id.content)
    TextView content;
    @InjectView(R.id.image)
    ImageView image;
    @InjectView(R.id.author)
    TextView author;
    @InjectView(R.id.foryou)
    TextView foryou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_start);
        ButterKnife.inject(this);
        //Font path
        String fontPath = "fonts/leilei.ttf";
        // text view label
        TextView txtGhost = (TextView) findViewById(R.id.content);
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        // Applying font
        txtGhost.setTypeface(tf);
        Picasso.with(this).load(R.drawable.foryou2).into(image);

        foryou.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/title.ttf"));
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 先将图片展出铺满，然后启动新的Activity
                            CircularAnim.fullActivity(StartActivity.this, foryou)
                                    .colorOrImageRes(R.color.colorstart)
                                    .go(new CircularAnim.OnAnimationEndListener() {
                                        @Override
                                        public void onAnimationEnd() {
                                            startActivity(new Intent(StartActivity.this, HomeActivity.class));
                                            finish();
                                        }
                                    });
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
