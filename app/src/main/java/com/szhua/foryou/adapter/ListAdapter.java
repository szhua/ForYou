package com.szhua.foryou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.szhua.foryou.R;
import com.szhua.foryou.activity.DetailActivity;
import com.szhua.foryou.entity.ForYouEntity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by szhua on 2016/8/25.
 */
public class ListAdapter extends RecyclerView.Adapter {
    private Context context;
    public ListAdapter(Context context) {
        this.context = context;
    }
    ArrayList<ForYouEntity> forYouEntityArrayList =new ArrayList<ForYouEntity>() ;
    public void setForYouEntityArrayList(ArrayList<ForYouEntity> forYouEntityArrayList) {
        this.forYouEntityArrayList = forYouEntityArrayList;
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false);
        ListHolder listHolder = new ListHolder(view);
        return listHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {

        ListHolder holder = (ListHolder) holder1;
        final ForYouEntity forYouEntity =forYouEntityArrayList.get(position) ;

          holder.cardView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent =new Intent(context, DetailActivity.class) ;
                  intent.putExtra("foryou",forYouEntity);
                  context.startActivity(intent);
              }
          });
        if(!TextUtils.isEmpty(forYouEntity.getFile().getUrl()))
        Picasso.with(context).load(forYouEntity.getFile().getUrl()).resize(300,200).centerCrop().placeholder(R.drawable.default_hp_image).into(holder.iv);
        else holder.iv.setImageResource(R.drawable.default_hp_image);
        holder.content.setText(forYouEntity.getContentString());
        holder.date.setText(forYouEntity.getDate());
    }
    @Override
    public int getItemCount() {
        return forYouEntityArrayList==null?0:forYouEntityArrayList.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv)
        ImageView iv;
        @InjectView(R.id.date)
        TextView date;
        @InjectView(R.id.content)
        TextView content;
        @InjectView(R.id.card)
        CardView cardView;
        ListHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
