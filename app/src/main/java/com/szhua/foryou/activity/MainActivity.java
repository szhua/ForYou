package com.szhua.foryou.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.szhua.foryou.R;
import com.szhua.foryou.blurredview.BubbleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.bt)
    Button bt;
    private BubbleView bubbleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        bubbleView = (BubbleView) findViewById(R.id.BubbleView);
        List<Drawable> drawableList = new ArrayList<>();
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_2));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_3));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_4));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_5));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_6));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_7));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_8));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_9));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_10));
        bubbleView.setDrawableList(drawableList);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.schedule(task, 1000, 500); // 1s后执行task,经过1s再次执行
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());
            }
            super.handleMessage(msg);
        };
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };
}
