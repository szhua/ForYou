package com.szhua.foryou.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.szhua.foryou.R;
import com.szhua.foryou.activity.CheckLargePicActivity;
import com.szhua.foryou.activity.HomeActivity;
import com.szhua.foryou.base.ObservableScrollView;
import com.szhua.foryou.base.ScrollViewListener;
import com.szhua.foryou.entity.ForYouEntity;
import com.szhua.foryou.entity.ImageTools;
import com.szhua.foryou.utils.AnimUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by szhua on 2016/8/16.
 */
public class ContentFragment extends Fragment {

    @InjectView(R.id.photo)
    ImageView photo;
    @InjectView(R.id.date)
    TextView date;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.content)
    TextView content;
    @InjectView(R.id.scrollView)
    ObservableScrollView scrollView;
    private ForYouEntity forYouEntity;



    public static  ContentFragment getInstance(ForYouEntity forYouEntity){
        ContentFragment contentFragment =new ContentFragment() ;
        contentFragment.forYouEntity =forYouEntity ;
        return  contentFragment ;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment_layout, container, false);
        ButterKnife.inject(this, view);



        return view;
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Picasso.with(getContext()).load(forYouEntity.getFile().getUrl()).placeholder(R.drawable.default_hp_image).resize(600,400).centerCrop().into(photo);
        content.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/leilei.ttf"));
        content.setText(forYouEntity.getContentString());
        title.setText(forYouEntity.getName());
        date.setText(forYouEntity.getDate());
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(), CheckLargePicActivity.class);
                intent.putExtra("url",forYouEntity.getFile().getUrl()) ;
                startActivity(intent);
                AnimUtil.intentPushUp(getActivity());
//                ((HomeActivity) getActivity()).setPhoto(forYouEntity.getFile().getUrl());
//                ((HomeActivity) getActivity()).setLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        return false;
//                    }
//                });
            }
        });

        scrollView.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if(oldy>0){
                    ( (HomeActivity)getActivity()).showFla();
                }else if(oldy<0){
                    ( (HomeActivity)getActivity()).hideFla();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
