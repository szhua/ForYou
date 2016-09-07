package com.szhua.foryou.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szhua.foryou.R;
import com.szhua.foryou.adapter.ListAdapter;
import com.szhua.foryou.entity.ForYouEntity;
import com.szhua.foryou.utils.ProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import rx.Subscriber;

public class ForYouListActivity extends AppCompatActivity  implements View.OnClickListener ,SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.recycleview)
    RecyclerView recycleview;
    @InjectView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.back_bt)
    ImageView backBt;

    private int lastVisiblePostion;
    private ListAdapter listAdapter;
    private boolean hasMore ;
    private ProgressHUD  mProgressHUD;
    private ArrayList<ForYouEntity> foryoulist =new ArrayList<ForYouEntity>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_you_list);
        ButterKnife.inject(this);

        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/title.ttf"));
        backBt.setOnClickListener(this);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recycleview.setHasFixedSize(true);
        recycleview.setLayoutManager(gridLayoutManager);
        listAdapter = new ListAdapter(this);
        listAdapter.setForYouEntityArrayList(foryoulist);
        recycleview.setAdapter(listAdapter);
        swiperefreshlayout.setColorSchemeResources(R.color.color1,R.color.color3,R.color.color2,R.color.colorPrimary);
        swiperefreshlayout.setOnRefreshListener(this);
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && listAdapter != null &&
                        lastVisiblePostion + 1 == listAdapter.getItemCount()) {
                    if (hasMore) {
                       queryObjects();
                    } else {
                       listAdapter.notifyDataSetChanged();
                        showToast("没有更多内容了！");
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisiblePostion = gridLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        hasMore=false ;
        queryObjects();
        showProgress(true);
    }

    @Override
    public void onClick(View view) {
        if(view==backBt){
            finish();
        }
    }

    @Override
    public void onRefresh() {

        Log.i("MMM","okokok") ;
        hasMore =false ;
            new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           queryObjects();
                        }
                    });
                }
            }.start();
    }

    public void queryObjects() {
        final BmobQuery<ForYouEntity> bmobQuery = new BmobQuery<ForYouEntity>();
       // bmobQuery.setLimit(20);

        if(!hasMore){
            bmobQuery.setLimit(10);
        }else{
          bmobQuery.setSkip(10);
        }
        bmobQuery.order("-time");
        bmobQuery.findObjectsObservable(ForYouEntity.class)
                .subscribe(new Subscriber<List<ForYouEntity>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        // loge(e);
                    }
                    @Override
                    public void onNext(List<ForYouEntity> persons) {
                        //   refreshLayout.onRefreshComplete();
//                        forYouEntityArrayList.clear();
//                        forYouEntityArrayList.addAll(persons);
//                        sampleAdapter=new SampleAdapter(getSupportFragmentManager(),forYouEntityArrayList) ;
//                        viewpager.setAdapter(sampleAdapter);
                        if(hasMore){
                            foryoulist.addAll(persons);
                        }else{
                        foryoulist = (ArrayList<ForYouEntity>) persons;}
                        listAdapter.setForYouEntityArrayList(foryoulist);
                        swiperefreshlayout.setRefreshing(false);
                        if(persons!=null&&persons.size()<10){
                            hasMore =false ;
                        }else{
                            hasMore =true ;
                        }
                   // showToast("刷新成功");
                       showProgress(false);
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
}
