package com.szhua.foryou.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.szhua.foryou.R;
import com.szhua.foryou.entity.ForYouEntity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import rx.Subscriber;

public class QueryActivity extends AppCompatActivity {

    @InjectView(R.id.iv)
    ImageView iv;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.content)
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        ButterKnife.inject(this);
        queryObjects();


    }

    public void queryObjects() {
        final BmobQuery<ForYouEntity> bmobQuery = new BmobQuery<ForYouEntity>();
//		bmobQuery.addWhereEqualTo("age", 25);
        bmobQuery.setLimit(20);
      //  bmobQuery.order("-createdAt");

//		//先判断是否有缓存
//		boolean isCache = bmobQuery.hasCachedResult(Movie.class);
//		if(isCache){
//			bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
//		}else{
//			bmobQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);	// 如果没有缓存的话，则先从网络中取
//		}
//		observable形式
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
                        Log.i("szhua", "moives" + persons.get(0).getFile().getUrl());
                        //("查询成功：共"+persons.size()+"条数据。");
                        showToast("查询成功：共" + persons.size() + "条数据。");
                        Picasso.with(QueryActivity.this).load(persons.get(0).getFile().getUrl()).into(iv);
                        name.setText(persons.get(0).getName());
                        content.setText(persons.get(0).getContentString());
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
}
