package com.cn.aixiyi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.cn.aixiyi.common.HeaderUpLoaderManager;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtil {
    //圆角半径
    public static float ROUNDPX = 5.0f;

    public static float ROUNDPX_FACE = 2.0f;

    // 放大缩小图片
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }

    // 将Drawable转化为Bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    public static Drawable bitMapToDrawable(Bitmap bitmap) {
        // 这里可以让Bitmap再转化为Drawable
        Drawable roundDrawable = new BitmapDrawable(bitmap);
        // Drawable reflectDrawable = new BitmapDrawable(bitmap);
        // mImageView01.setBackgroundDrawable(roundDrawable);
        // mImageView02.setBackgroundDrawable(reflectDrawable);
        return roundDrawable;
    }

    // 获得圆角图片的方�?
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    // 获得圆角图片的方�?
    public static Drawable getRoundedCornerBitmap2(Drawable drawable, float pixels) {
        try {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap inpBitmap = bitmap;
            int width = 0;
            int height = 0;
            width = inpBitmap.getWidth();
            height = inpBitmap.getHeight();

            if (width <= height) {
                height = width;
            } else {
                width = height;
            }
            final int color = 0xff424242;
            Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, width, height);
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(inpBitmap, rect, rect, paint);

            return new BitmapDrawable(output);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }


    }

    // 获得带�?影的图片方法
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }

    // 压缩图片
    public static String Compress(String path, String newFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int be = test(path);
        options.inSampleSize = be;
        options.inTempStorage = new byte[16 * 1024];

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        // 保存入sdCard
        File file2 = new File(newFile);
        try {
            FileOutputStream out = new FileOutputStream(file2);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
            return newFile;
        } catch (Exception e) {

        }
        return null;

    }

    // 保存图片
    public static String Save(Bitmap bitmap, String newFile) {
        File file = new File(newFile);
        if (file.exists()) {
            file.delete();
        }
        // 保存入sdCard
        File file2 = new File(newFile);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file2);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
            }
            return newFile;
        } catch (Exception e) {
            Log.i("Exception", e + "");
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                Log.i("Exception", e + "");
            }
        }
        return null;

    }

    private static int test(String url) {
        int be = 1;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTempStorage = new byte[16 * 1024];
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(url, options);
            be = (int) (options.outHeight / (float) 260);
            if (be <= 0)
                be = 1;

            return be;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    /*
     * 从网络上获取图片，如果图片在本地存在的话就直接拿，如果不存在再去服务器上下载图片
     * 这里的path是图片的地址
     */
    public static void setImgView(Context context, ImageView imageView, String path) {
        File file = new File(path);
        // 如果图片存在本地缓存目录，则不去服务器下�?
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Drawable drawable = ImageUtil.bitMapToDrawable(bitmap);
            imageView.setImageDrawable(drawable);
        } else {
            // 从网络上获取图片
         //   HeaderUpLoaderManager.getInstance().downLoadHeader(context, file);
        }
    }

    public static Drawable resizeImage(Bitmap bitmap, int w, int h) {

        // load the origial Bitmap
        Bitmap BitmapOrg = bitmap;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

//        Log.v(tag,String.valueOf(width));
//        Log.v(tag,String.valueOf(height));
//        
//        Log.v(tag,String.valueOf(newWidth));
//        Log.v(tag,String.valueOf(newHeight));

        // calculate the scale
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
        float scaleWidth = ((float) newWidth);
        float scaleHeight = ((float) newHeight);

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
//        Bitmap resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.valueOf(name));
        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return new BitmapDrawable(resizedBitmap);
    }


    /**
     * bitmap  转成�? file 文件并且保存了下�?
     * @param context
     * @param bitmap
     * @return
     */
    public static String saveBitMapToFile(Context context, Bitmap bitmap) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(new Date(System.currentTimeMillis())) + ".jpg";
        if(null == context || null == bitmap) {
            return null;
        }
        if(TextUtils.isEmpty(fileName)) {
            return null;
        }
        FileOutputStream fOut = null;
        try {
            File file = null;
            String fileDstPath = "";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 保存到sd�?
                fileDstPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + "MyFile" + File.separator + fileName;

                File homeDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + "MyFile" + File.separator);
                if (!homeDir.exists()) {
                    homeDir.mkdirs();
                }
            } else {
                // 保存到file目录
                fileDstPath = context.getFilesDir().getAbsolutePath()
                        + File.separator + "MyFile" + File.separator + fileName;

                File homeDir = new File(context.getFilesDir().getAbsolutePath()
                        + File.separator + "MyFile" + File.separator);
                if (!homeDir.exists()) {
                    homeDir.mkdir();
                }
            }

            file = new File(fileDstPath);

            if (!file.exists() ) {
                // �?��起见，先删除老文件，不管它是否存在�?
                file.delete();

                fOut = new FileOutputStream(file);
                if (fileName.endsWith(".jpg")) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fOut);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                }
                fOut.flush();
                bitmap.recycle();
            }

            Log.i("FileSave", "saveDrawableToFile " + fileName
                    + " success, save path is " + fileDstPath);
            return fileDstPath;
        } catch (Exception e) {
            Log.e("FileSave", "saveDrawableToFile: " + fileName + " , error", e);
            return null;
        } finally {
            if(null != fOut) {
                try {
                    fOut.close();
                } catch (Exception e) {
                    Log.e("FileSave", "saveDrawableToFile, close error", e);
                }
            }
        }
    }
}