package com.szhua.foryou.activity;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.szhua.foryou.R;
import com.szhua.foryou.blurredview.BubbleView;
import com.szhua.foryou.entity.ForYouEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailActivity extends AppCompatActivity {

    @InjectView(R.id.back_bt)
    ImageView backBt;
    @InjectView(R.id.photo)
    ImageView photo;
    @InjectView(R.id.date)
    TextView date;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.content)
    TextView content;
    @InjectView(R.id.scrollView)
    ScrollView scrollView;
    @InjectView(R.id.header)
    TextView head;
    @InjectView(R.id.BubbleView)
    com.szhua.foryou.blurredview.BubbleView bubbleView;

    private ForYouEntity forYouEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        forYouEntity = (ForYouEntity) getIntent().getSerializableExtra("foryou");
        Picasso.with(this).load(forYouEntity.getFile().getUrl()).placeholder(R.drawable.default_hp_image).into(photo);
        content.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/leilei.ttf"));
        content.setText(forYouEntity.getContentString());
        title.setText(forYouEntity.getName());
        date.setText(forYouEntity.getDate());
        head.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/title.ttf"));

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

      // bubbleView.bringToFront();
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
        timer.schedule(task, 500, 400); // 1s后执行task,经过1s再次执行

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
