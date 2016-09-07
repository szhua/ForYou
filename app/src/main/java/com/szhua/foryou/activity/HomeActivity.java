package com.szhua.foryou.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.szhua.foryou.R;
import com.szhua.foryou.blurredview.BubbleView;
import com.szhua.foryou.entity.ForYouEntity;
import com.szhua.foryou.fragment.ContentFragment;
import com.szhua.foryou.lib.RefreshCallBack;
import com.szhua.foryou.sparbutton.SparkButton;
import com.szhua.foryou.utils.ProgressHUD;
import com.szhua.foryou.utils.UiUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import rx.Subscriber;

public class HomeActivity extends AppCompatActivity implements RefreshCallBack {

    @InjectView(R.id.more)
    TextView more;
    @InjectView(R.id.header)
    RelativeLayout header;
    @InjectView(R.id.star_button2)
    SparkButton starButton2;
    private long waitTime = 2000;
    private long touchTime = 0;

    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.divider)
    View divider;
    @InjectView(R.id.fla)
    FloatingActionButton fla;

    @InjectView(R.id.BubbleView)
    BubbleView bubbleView;

    private ArrayList<ForYouEntity> forYouEntityArrayList = new ArrayList<ForYouEntity>();
    private SampleAdapter sampleAdapter;
    private ProgressHUD mProgressHUD;
    private Timer timer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
  //      showProgress(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/title.ttf"));
        sampleAdapter = new SampleAdapter(getSupportFragmentManager(), forYouEntityArrayList);
        sampleAdapter.setForYouEntityArrayList(forYouEntityArrayList);
        viewpager.setAdapter(sampleAdapter);
        // viewpager.setOffscreenPageLimit(2);
//        refreshLayout.setRefreshMode(HorizontalRefreshLayout.MODE_ABOVE);
//        //define your RefreshHeader
//        refreshLayout.setRefreshHeader(new MaterialRefreshHeader(HorizontalRefreshLayout.START),
//                HorizontalRefreshLayout.START);
//        refreshLayout.setRefreshHeader(new MaterialRefreshHeader(HorizontalRefreshLayout.END),
//                HorizontalRefreshLayout.END);
//        refreshLayout.setRefreshCallback(this);
        playAnimation();
        fla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UpLoadActivity.class);
                startActivity(intent);
            }
        });

        List<Drawable> drawableList = new ArrayList<>();
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_2));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_3));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_4));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_5));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_6));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_7));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_8));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_black_24dp_10));
        bubbleView.setDrawableList(drawableList);


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ForYouListActivity.class);
                startActivity(intent);
            }
        });


        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queryObjects();
            }
        }.start();
    }



    public void showFla() {
        fla.show();
    }

    ;

    public void hideFla() {
        fla.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onLeftRefreshing() {
        //     refreshLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                queryObjects();
//            }
//        }, 1000);
    }

    @Override
    public void onRightRefreshing() {
//        refreshLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //   adapter.onLoadMore();
//                refreshLayout.onRefreshComplete();
//            }
//        }, 1000);
    }


    class SampleAdapter extends FragmentPagerAdapter {

        private ArrayList<ForYouEntity> forYouEntityArrayList = new ArrayList<ForYouEntity>();

        public void setForYouEntityArrayList(ArrayList<ForYouEntity> forYouEntityArrayList) {
            this.forYouEntityArrayList = forYouEntityArrayList;
            notifyDataSetChanged();
        }

        public SampleAdapter(FragmentManager fm, ArrayList<ForYouEntity> forYouEntityArrayList) {
            super(fm);
            this.forYouEntityArrayList = forYouEntityArrayList;
        }

        @Override
        public Fragment getItem(int position) {
            return ContentFragment.getInstance(forYouEntityArrayList.get(position));
        }

        @Override
        public int getCount() {
            return forYouEntityArrayList.size();
        }
    }

    public void queryObjects() {
        final BmobQuery<ForYouEntity> bmobQuery = new BmobQuery<ForYouEntity>();
        bmobQuery.setLimit(10);
        bmobQuery.order("-time");
        bmobQuery.findObjectsObservable(ForYouEntity.class)
                .subscribe(new Subscriber<List<ForYouEntity>>() {
                    @Override
                    public void onCompleted() {
                        starButton2.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Throwable e) {
                        // loge(e);
                    }
                    @Override
                    public void onNext(List<ForYouEntity> persons) {
                        //   refreshLayout.onRefreshComplete();
                        forYouEntityArrayList.clear();
                        forYouEntityArrayList.addAll(persons);
                        sampleAdapter = new SampleAdapter(getSupportFragmentManager(), forYouEntityArrayList);
                        viewpager.setAdapter(sampleAdapter);
                        showToast("刷新成功");
                 //       showProgress(false);
                        timer.schedule(task, 500, 400); // 1s后执行task,经过1s再次执行
                        stopAnimation();
                        starButton2.setVisibility(View.GONE);
                    }
                });
    }

    Toast mToast;

    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }


    /**
     * 显示加载进度条
     *
     * @param show
     */
    public void showProgress(boolean show) {
        if (show) {
            mProgressHUD = ProgressHUD.show(this, "", true, true, null);
        } else {
            if (mProgressHUD != null) {
                mProgressHUD.dismiss();
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());
            }
            super.handleMessage(msg);
        }

        ;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 两次返回键，退出程序
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                showToast("再按一次退出程序");
                touchTime = currentTime;
            } else {
                System.exit(0);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void playAnimation() {
        timer1 =new Timer() ;
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        starButton2.setVisibility(View.VISIBLE);
                        starButton2.playAnimation();
                    }
                });
            }
        },100,1200);}

    private void stopAnimation(){
        timer1.cancel();
        starButton2.setVisibility(View.GONE);
        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        starButton2.setVisibility(View.GONE);
                    }
                });
            }
        }.start();
    }

}
