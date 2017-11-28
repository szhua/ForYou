package com.szhua.foryou.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.szhua.foryou.R;
import com.szhua.foryou.blurredview.BubbleView;
import com.szhua.foryou.entity.ForYouEntity;
import com.szhua.foryou.fragment.ContentFragment;
import com.szhua.foryou.sparbutton.SparkButton;
import com.szhua.foryou.utils.UiUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class HomeActivity extends AppCompatActivity   {

    @InjectView(R.id.more)
    TextView more;
    @InjectView(R.id.header)
    RelativeLayout header;
    @InjectView(R.id.star_button2)
    SparkButton starButton2;
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

    private ArrayList<ForYouEntity> forYouEntityArrayList = new ArrayList<>();
    private SampleAdapter sampleAdapter;

    private static final long waitTime = 2000;
    private long touchTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/title.ttf"));
        sampleAdapter = new SampleAdapter(getSupportFragmentManager(), forYouEntityArrayList);
        sampleAdapter.setForYouEntityArrayList(forYouEntityArrayList);
        viewpager.setAdapter(sampleAdapter);
        fla.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, UpLoadActivity.class);
            startActivity(intent);
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

        Flowable.interval(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong ->bubbleView.startAnimation(bubbleView.getWidth(),bubbleView.getHeight()) );
        more.setOnClickListener(view->startActivity(new Intent(this, ForYouListActivity.class)));
        queryObjects();

    }

    public void showFla() {
        fla.show();
    }
    public void hideFla() {
        fla.hide();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
    final class SampleAdapter extends FragmentPagerAdapter {
        private ArrayList<ForYouEntity> forYouEntityArrayList = new ArrayList<>();
        private  void setForYouEntityArrayList(ArrayList<ForYouEntity> forYouEntityArrayList) {
            this.forYouEntityArrayList = forYouEntityArrayList;
            notifyDataSetChanged();
        }
        private  SampleAdapter(FragmentManager fm, ArrayList<ForYouEntity> forYouEntityArrayList) {
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

    private  void queryObjects() {
        final BmobQuery<ForYouEntity> bmobQuery = new BmobQuery<>();
        bmobQuery.setLimit(10);
        bmobQuery.order("-time");
        bmobQuery.findObjectsObservable(ForYouEntity.class)
                 .subscribe(forYouEntities -> {
                     forYouEntityArrayList.clear();
                     forYouEntityArrayList.addAll(forYouEntities);
                     sampleAdapter = new SampleAdapter(getSupportFragmentManager(), forYouEntityArrayList);
                     viewpager.setAdapter(sampleAdapter);
                     UiUtil.showToast(this,"刷新成功");
                     starButton2.setVisibility(View.GONE);
                 });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 两次返回键，退出程序
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                UiUtil.showToast(this,"再按一次退出程序");
                touchTime = currentTime;
            } else {
                System.exit(0);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
