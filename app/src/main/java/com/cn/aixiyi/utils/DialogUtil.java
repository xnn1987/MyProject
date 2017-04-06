package com.cn.aixiyi.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.aixiyi.R;


/**
 * Created by Administrator on 2016/5/25.
 */
public class DialogUtil {

    private static Activity mActivity;
    private static Dialog mDialog;
    private static ImageView mumImage;

    public DialogUtil() {

    }

    public DialogUtil(Activity activity) {
        this.mActivity = activity;
    }

    public static void startWaitingDialog() {
        startWaitingDialog(mActivity, "");
    }

    public static void startWaitingDialog(String notice) {
        startWaitingDialog(mActivity, notice);
    }

    /**
     * 网络请求错误提示的dialog
     *
     * @param mActivity
     * @param notice
     */
    public static void startWaitingDialog(Activity mActivity, String notice) {
        if (mDialog != null && mDialog.isShowing())
            return;
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View v = inflater.inflate(R.layout.loading_dialog, null);
        mumImage = (ImageView) v.findViewById(R.id.img);
        Animation mumAnimation = AnimationUtils.loadAnimation(
                mActivity, R.anim.load_animation);
        mumImage.startAnimation(mumAnimation);

        mDialog = new Dialog(mActivity, R.style.loading_dialog);
        mDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mDialog.setCancelable(false);
        if (!mDialog.isShowing())
            mDialog.show();

        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mDialog != null) {
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                            mDialog = null;
                        }
                    }

                    return true;
                }

                return false;
            }
        });
    }

    public static void stop_WaitingDialog() {
        if (mDialog != null && !mActivity.isFinishing()) {
            mDialog.dismiss();
            mDialog = null;
            if (mumImage != null) {
                mumImage.clearAnimation();
            }
        }
    }


    /*================================= 下面为小小金融的dialog的工具类 =======================================================*/

    /**
     * 自定义dialog  但是没有editext
     *
     * @param activity
     * @param view
     * @return
     */
    public static Dialog initDialog(Activity activity, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        dialog.setCanceledOnTouchOutside(false);
        window.setAttributes(params);
        window.setContentView(view);
        return dialog;
    }

    /**
     * 自定义dialog  但是有editext
     *
     * @param activity
     * @param view
     * @return
     */
    public static Dialog initDialogContainEditText(Activity activity, View view) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        // 设置宽度为屏幕的宽度
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() - DensityUtil.dq2px(activity, 14) * 2); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);
        return dialog;
    }

    /**
     * 提示
     *
     * @param activity
     * @param content
     * @param contentSize
     * @param ContentColor
     * @param bottomLeft
     * @param bottomLeftSizem
     * @param bottomLeftColor
     * @param bottomRight
     * @param bottomRightSizem
     * @param bottomRightColor
     * @return
     */
    public static Dialog commonDialogNotice(Activity activity, String content, int contentSize, int ContentColor,
                                            String bottomLeft, int bottomLeftSizem, int bottomLeftColor,
                                            String bottomRight, int bottomRightSizem, int bottomRightColor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final Dialog dialog = builder.create();
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_common_notice, null);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);
        window.setContentView(view);
        window.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.dialog_window_bg));
        TextView mTvContent = (TextView) view.findViewById(R.id.dialogCommon_tv_content);
        mTvContent.setText(content);
        mTvContent.setTextColor(ContentColor);
        mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, contentSize);
        TextView mTvLeft = (TextView) view.findViewById(R.id.dialogCommon_tv_left);
        mTvLeft.setText(bottomLeft);
        mTvLeft.setTextColor(bottomLeftColor);
        mTvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, bottomLeftSizem);
        TextView mTvRight = (TextView) view.findViewById(R.id.dialogCommon_tv_right1);
        mTvRight.setText(bottomRight);
        mTvRight.setTextColor(bottomRightColor);
        mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, bottomRightSizem);

        mTvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogListener != null) {
                    dialogListener.left();
                }
            }
        });
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.right();
            }
        });

        return dialog;
    }

    public interface DialogListener {
        void left();

        void right();
    }

    public static DialogListener dialogListener;

    public DialogListener getDialogListener() {
        return dialogListener;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
}
