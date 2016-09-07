package com.szhua.foryou.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smile.filechoose.api.ChooserType;
import com.smile.filechoose.api.ChosenFile;
import com.smile.filechoose.api.FileChooserListener;
import com.smile.filechoose.api.FileChooserManager;
import com.szhua.foryou.R;
import com.szhua.foryou.entity.ForYouEntity;
import com.szhua.foryou.entity.ImageTools;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.ProgressCallback;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;


public class UpLoadActivity extends AppCompatActivity implements FileChooserListener, View.OnClickListener {


    @InjectView(R.id.title)
    EditText title;
    @InjectView(R.id.contentString)
    EditText contentString;
    @InjectView(R.id.tv_one_one)
    Button tvOneOne;


    String name;
    String content;
    String dateString ;
    @InjectView(R.id.date)
    EditText date;
    private FileChooserManager fm;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load);
        ButterKnife.inject(this);
        tvOneOne.setOnClickListener(this);


    }


    public void pickFile() {
        fm = new FileChooserManager(this);
        fm.setFileChooserListener(this);
        try {
            fm.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ChosenFile choosedFile;

    @Override
    public void onFileChosen(final ChosenFile chosenFile) {
        choosedFile = chosenFile;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("MMM", choosedFile.getFilePath());
                // showFileDetails(chosenFile);
                Bitmap bitmap = ImageTools.readBitmapAutoSize(choosedFile.getFilePath(), 500,
                        600);
                bitmap = ImageTools.compressImage(bitmap);
                String picturePath = ImageTools.savaPhotoToLocal(bitmap);
                File mp3 = new File(picturePath);
                uploadMovoieFile(mp3);
            }
        });
    }

    @Override
    public void onError(String s) {

    }

    private void showFileDetails(ChosenFile file) {
        StringBuffer text = new StringBuffer();
        text.append("File name: " + file.getFileName() + "\n");
        text.append("File path: " + file.getFilePath() + "\n");
        text.append("File size: " + file.getFileSize());
        // tv_path.setText(text.toString());
    }

    /**
     * 上传指定路径下的电影文件
     *
     * @param file
     * @return void
     */
    private void uploadMovoieFile(File file) {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("上传中...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadObservable(new ProgressCallback() {//上传文件操作
            @Override
            public void onProgress(Integer value, long total) {
                //log("uploadMovoieFile-->onProgress:"+value);
                dialog.setProgress(value);
            }
        }).doOnNext(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                // url = bmobFile.getUrl();
                //log("上传成功："+url+","+bmobFile.getFilename());
            }
        }).concatMap(new Func1<Void, Observable<String>>() {//将bmobFile保存到movie表中
            @Override
            public Observable<String> call(Void aVoid) {
                return saveObservable(new ForYouEntity(name, bmobFile, content,dateString,System.currentTimeMillis()));
            }
        }).concatMap(new Func1<String, Observable<String>>() {//下载文件
            @Override
            public Observable<String> call(String s) {
                return bmobFile.downloadObservable(new ProgressCallback() {
                    @Override
                    public void onProgress(Integer value, long total) {
                        //log("download-->onProgress:"+value+","+total);
                    }
                });
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                //log("--onCompleted--");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("MMM","--onError--:"+e.getMessage());
                dialog.dismiss();
                choosedFile = null;
            }

            @Override
            public void onNext(String s) {
                dialog.dismiss();
                choosedFile = null;
                // log("download的文件地址："+s);
            }
        });
    }

    /**
     * save的Observable
     *
     * @param obj
     * @return
     */
    private Observable<String> saveObservable(BmobObject obj) {
        return obj.saveObservable();
    }

    @Override
    public void onClick(View view) {
        if (view == tvOneOne) {
            if (checkInput()) {
                insertDataWithOne();
            }
        }
    }

    public boolean checkInput() {
        name = title.getText().toString();
        content = contentString.getText().toString();
        dateString =date.getText().toString() ;

        if(TextUtils.isEmpty(dateString)){
            return  false ;
        }

        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (TextUtils.isEmpty(content)) {
            return false;
        }

//        if(choosedFile==null){
//            return  false;
//        }
//
        return true;


    }

    /**
     * 插入单条数据（单个BmobFile列）
     * 例如：插入单条电影
     *
     * @return void
     * @throws
     */
    private void insertDataWithOne() {
        if (choosedFile == null) {
            showToast("请先选择文件");
            pickFile();
            return;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChooserType.REQUEST_PICK_FILE && resultCode == RESULT_OK) {
            if (fm == null) {
                fm = new FileChooserManager(this);
                fm.setFileChooserListener(this);
            }
            Log.i("MMMM", "Probable file size: " + fm.queryProbableFileSize(data.getData(), this));
            fm.submit(requestCode, data);
        }
    }
}
