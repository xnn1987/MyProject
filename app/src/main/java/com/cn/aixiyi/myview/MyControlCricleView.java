package com.cn.aixiyi.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cn.aixiyi.R;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.DensityUtil;

/**
 * Created by Administrator on 2016/11/13.
 */
public class MyControlCricleView extends View {
    Context context;
    Paint mPaint;
    int progress = 30;
    int start_degree = -90;

    public MyControlCricleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        InitResources(context, attrs);

    }

    int background_int;
    int progress_int;
    float layout_width_float;
    int textColor_int;
    float textSize_float;
    int max_int;
    Drawable thumb_double;
    Bitmap bitmap;
    int thumbSize_int;
    String progresstext ="开始洗衣";
    private void InitResources(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.windows);

        background_int = mTypedArray.getColor(R.styleable.windows_backGround,getResources().getColor(R.color.aixiyi_saveline_gray));
        progress_int = mTypedArray.getColor(R.styleable.windows_progressDrawable, getResources().getColor(R.color.aixiyi_control_zhi));
        layout_width_float = mTypedArray.getDimension(R.styleable.windows_layout_width, 7);
        textColor_int = mTypedArray.getColor(R.styleable.windows_textcolor, getResources().getColor(R.color.aixiyi_center_txt_blue));
        textSize_float = mTypedArray.getDimension(R.styleable.windows_textsize, 30);
        max_int = mTypedArray.getInt(R.styleable.windows_max, 100);
        progress = mTypedArray.getInt(R.styleable.windows_progress, 0);
        thumb_double = mTypedArray.getDrawable(R.styleable.windows_thumb);
        thumbSize_int = mTypedArray.getInt(R.styleable.windows_thumbSize, 30);
        mTypedArray.recycle();

        if (thumb_double == null) {
            Bitmap bitmap = Bitmap.createBitmap(thumbSize_int, thumbSize_int, Bitmap.Config.ARGB_8888);
            // 图片画片
            Canvas cas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(getResources().getColor(R.color.aixiyi_saveline_gray));
            int center = thumbSize_int / 2;
            int radius = center - 4;
            cas.drawCircle(center, center, radius, paint);
            thumb_double = new BitmapDrawable(getResources(), bitmap);
        }
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.wash_control_style);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgressView(canvas);
    }

    private void drawProgressView(Canvas canvas) {
        InitOval(canvas);
        drawBackground(canvas);
        drawProgress(canvas);
        drawProgressText(canvas);
    }

    RectF oval;

    private void InitOval(Canvas canvas) {
        int center = getWidth() / 2;
        int radius = (int) (center  - thumbSize_int / 2);
        // 画布中央减去半径就是左上角
        int left_top = center - radius;
        // 画布中央加上半径就是右下角
        int right_bottom = center + radius;

        if (oval == null) {
            oval = new RectF(left_top, left_top, right_bottom, right_bottom);
        }
    }

    /**
     * 绘制进度文字
     *
     * @param canvas
     */
    private void drawProgressText(Canvas canvas) {

        mPaint.setTextSize(textSize_float);
        mPaint.setColor(textColor_int);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.FILL);
        float text_left = (getWidth() - mPaint.measureText(progresstext)) / 2;

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        // 绘制文字是底部对齐
        float text_top = (float) ((getHeight() / 2 + Math.ceil(fontMetrics.descent - fontMetrics.ascent) / 2));
        DebugLog.e("Response", "text_top=="+text_top);
        canvas.drawText(progresstext, text_left, text_top+ mPaint.measureText(progresstext)/2, mPaint);
        canvas.drawBitmap(bitmap,text_left, text_top-126- mPaint.measureText(progresstext)/3, mPaint);

    }

    private void drawProgress(Canvas canvas) {
        // 设置进度
        mPaint.setColor(progress_int);
        mPaint.setStrokeWidth(layout_width_float);
        float seek = 0;
        if (max_int > 0) {
            seek = (float) progress / max_int * 360;
        }
        canvas.drawArc(oval, start_degree, seek, false, mPaint);
        canvas.save();
        int center = getWidth() / 2;
        int radius = (int) (center - thumbSize_int / 2);

        double cycle_round = (seek + start_degree) * Math.PI / 180;
        float x = (float) (Math.cos(cycle_round) * (radius) + center - thumbSize_int / 2);
        float y = (float) (Math.sin(cycle_round) * (radius) + center - thumbSize_int / 2);
        thumb_double.setBounds(0, 0, thumbSize_int, thumbSize_int);
        canvas.translate(x, y);
        thumb_double.draw(canvas);
        canvas.restore();
    }

    private void drawBackground(Canvas canvas) {

        mPaint.setStrokeWidth(layout_width_float);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        // 设置背景
        mPaint.setColor(background_int);
        canvas.drawArc(oval, start_degree, 360, false, mPaint);

    }

    /**
     * 查看Seekbar源码
     *
     * @param progress
     */
    public synchronized void setProgress(int progress,int sleep,String progessStr) {
        progresstext=progessStr;
        if (progress > max_int) {
            progress = max_int;
        }
        if(progress==0){
            postInvalidate();
        }else{
            Thread myThread=new MyThread(progress,sleep);
            myThread.start();
        }
    }

    public int getProgress() {
        return this.progress;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (thumb_double != null) {
            thumb_double.setCallback(null);
            thumb_double = null;
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress= (int) msg.obj;
            postInvalidate();

        }
    };
    class MyThread extends   Thread{
        private int progress=0;
        private int sleep=0;
        public MyThread(int progress ,int sleep) {
            this.progress=progress;
            this.sleep=sleep;
        }

        @Override
        public void run() {
            int i=0;
            while (i<progress){
                try {
                    Thread.sleep(sleep);
                    i++;
                    Message msg=new Message();
                    msg.obj=i;
                    handler.handleMessage(msg);
                }catch (Exception e){
                    Log.i("MyThread",e.fillInStackTrace().toString());
                }
            }
        }
    }

}