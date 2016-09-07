package com.szhua.foryou.utils;

/**
 * Created by szhua 2016/3/11
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author mazhenjian
 * @version 1.0.0 2015-7-29
 * @Description
 * @reviewer
 */
public class ImageTools {
    public static int reckonThumbnail(int oldWidth, int oldHeight,
                                      int newWidth, int newHeight) {
        if ((oldHeight > newHeight && oldWidth > newWidth)
                || (oldHeight <= newHeight && oldWidth > newWidth)) {
            int be = (int) (oldWidth / (float) newWidth);
            if (be <= 1)
                be = 1;
            return be;
        } else if (oldHeight > newHeight && oldWidth <= newWidth) {
            int be = (int) (oldHeight / (float) newHeight);
            if (be <= 1)
                be = 1;
            return be;
        }
        return 1;
    }

    public static Bitmap PicZoom(Bitmap bmp, int width, int height) {
        int bmpWidth = bmp.getWidth();
        int bmpHeght = bmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);

        return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
    }

    public static String savaPhotoToLocal(Bitmap btp) {
        // 如果文件夹不存在则创建文件夹，并将bitmap图像文件保存
        File rootdir = Environment.getExternalStorageDirectory();
        String imagerDir = rootdir.getPath() + "/szhua";
        File dirpath = new File(imagerDir);
        if (!dirpath.exists()) {
            dirpath.mkdir();
        }
        String filename = "leilei"+System.currentTimeMillis() + ".jpg";
        File tempFile = new File(dirpath, filename);
        String filePath = tempFile.getAbsolutePath();
        try {
            // 将bitmap转为jpg文件保存
            FileOutputStream fileOut = new FileOutputStream(tempFile);
            btp.compress(CompressFormat.JPEG, 100, fileOut);
        } catch (FileNotFoundException e) {
//            MobclickAgent.reportError(BaseApplication.mContext, e);
        }
        return filePath;
    }

    public static Bitmap readBitmapAutoSize(String filePath, int outWidth,
                                            int outHeight) {
        // outWidth和outHeight是目标图片的最大宽度和高度，用作限制
        FileInputStream fs = null;
        BufferedInputStream bs = null;
        try {
            fs = new FileInputStream(filePath);
            bs = new BufferedInputStream(fs);
            BitmapFactory.Options options = setBitmapOption(filePath, outWidth,
                    outHeight);
            return BitmapFactory.decodeStream(bs, null, options);
        } catch (Exception e) {
//            MobclickAgent.reportError(BaseApplication.mContext, e);
        } finally {
            try {
                bs.close();
                fs.close();
            } catch (Exception e) {
//                MobclickAgent.reportError(BaseApplication.mContext, e);
            }
        }
        return null;
    }

    private static BitmapFactory.Options setBitmapOption(String file,
                                                         int width, int height) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        // 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
        BitmapFactory.decodeFile(file, opt);

        int outWidth = opt.outWidth; // 获得图片的实际高和宽
        int outHeight = opt.outHeight;
        opt.inDither = false;
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        // 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
        opt.inSampleSize = 2;
        // 设置缩放比,1表示原比例，2表示原来的四分之一....
        // 计算缩放比
        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
            int sampleSize = (outWidth / width + outHeight / height) / 2;
            opt.inSampleSize = sampleSize;
        }

        opt.inJustDecodeBounds = false;// 最后把标志复原
        return opt;
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * bitmap to byte[]
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static int BACKGROUND_COLOR = Color.argb(255, 128, 128, 128);
    private static int LINE_COLOR = Color.argb(255, 154, 154, 154);

    public static Bitmap drawBackground(int cellSize, int height, int widht, Bitmap bg, Context context) {
        Bitmap bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //加载背景图片
        // Bitmap bmps = BitmapFactory.decodeResource(getResources(), R.drawable.playerbackground);
        canvas.drawBitmap(bg, 0, 0, null);
        //加载要保存的画面
        // canvas.drawBitmap(bmp, 10, 100, null);
        //保存全部图层
        Paint background = new Paint();
       // background.setColor(BACKGROUND_COLOR);
       // canvas.drawRect(0, 0, widht, height, background);
        background.setAntiAlias(true);
        background.setColor(LINE_COLOR);

        for (int i = 0; i < widht / cellSize; i++) {
            canvas.drawLine(cellSize * i, 0, cellSize * i, height, background);
        }
        for (int i = 0; i < height / cellSize; i++) {
            canvas.drawLine(0, cellSize * i, widht, cellSize * i, background);
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        //存储路径
        File file = new File("/sdcard/song/");
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + "/xuanzhuan.jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            System.out.println("saveBmp is here");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  bitmap ;
    }

}

