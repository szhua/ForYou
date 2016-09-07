package com.szhua.foryou.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.szhua.foryou.R;
import com.szhua.foryou.entity.ImageTools;
import com.szhua.foryou.utils.AnimUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CheckLargePicActivity extends AppCompatActivity {

    @InjectView(R.id.image)
    ImageView image;
    @InjectView(R.id.parent)
    RelativeLayout parent;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_large_pic);
        ButterKnife.inject(this);

        url = getIntent().getStringExtra("url");

        setPhoto(url);

        setLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CheckLargePicActivity.this);
                builder.setTitle("选择").setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getImageStream(url));
                            ImageTools.savaPhotoToLocal(bitmap);
                            Toast.makeText(CheckLargePicActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                return false;
            }
        });
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                AnimUtil.intentPushDown(CheckLargePicActivity.this);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                AnimUtil.intentPushDown(CheckLargePicActivity.this);
            }
        });

    }

    public void setPhoto(String path) {
        Picasso.with(this).load(path).placeholder(R.drawable.default_hp_image).into(image);
    }

    public void setLongClickListener(View.OnLongClickListener listener) {
        image.setOnLongClickListener(listener);
    }


    /**
     * Get image from newwork
     *
     * @param path The path of image
     * @return InputStream
     * @throws Exception
     */
    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 两次返回键，退出程序
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            finish();
            AnimUtil.intentPushDown(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
