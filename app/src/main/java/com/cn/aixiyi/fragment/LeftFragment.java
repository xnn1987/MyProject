package com.cn.aixiyi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.activity.AdsRevenueActivity;
import com.cn.aixiyi.activity.AiMainActivity;
import com.cn.aixiyi.activity.LoadingActivity;
import com.cn.aixiyi.activity.MessageCenterActivity;
import com.cn.aixiyi.activity.MyOrderActivity;
import com.cn.aixiyi.activity.MyQianBaoActivity;
import com.cn.aixiyi.activity.PersonInfoActivity;
import com.cn.aixiyi.activity.SettingActivity;
import com.cn.aixiyi.activity.SuggestRKStyleActivity;
import com.cn.aixiyi.activity.UserLoginActivity;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.bean.ImageBean;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myview.MyHeaderCricleView;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.PictureUtil;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.ypy.eventbus.EventBus;

import org.ddq.common.util.JsonUtil;

import java.util.List;
import java.util.Map;

/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 侧边栏菜
 */
public class LeftFragment extends BaseFragment {
	private AiMainActivity fca;
	private View mHeaderView;
	private View mQbView;
	private View mMyDingDanView;
	private View mAdsView;
	private View mMessageView;
	private View mSuggestView;
	private View mSettingView;
	private View mTelView;
	private MyHeaderCricleView headerImg;
	private TextView userNameTxt;
	private  TextView userPhoneTxt;
	private View mQbLine;
	private View mMyDingDanLine;
	private View mAdsLine;
	private View mMessageLine;
	private View mSuggestLine;
	private View mSettingLine;
	private View mTelLine;
	private  TextView msgTxt;
	private String urls;
	private String telNum="400 820  4120";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		fca= (AiMainActivity) getActivity();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		getUserInfo();
		getMyMessageData();
		setListeners();
		return view;
	}
	public void findViews(View view) {
		mHeaderView=view.findViewById(R.id.left_user_name_lay);
		mQbView=view.findViewById(R.id.my_qianbao_lay);
		mMyDingDanView=view.findViewById(R.id.left_dingdan_lay);
		mAdsView=view.findViewById(R.id.left_ads_lay);
		mMessageView=view.findViewById(R.id.left_message_lay);
		mSuggestView=view.findViewById(R.id.left_suggest_lay);
		mSettingView=view.findViewById(R.id.left_setting_lay);
		mTelView=view.findViewById(R.id.left_tel_lay);
		msgTxt= (TextView) view.findViewById(R.id.left_messagenum_txt);
		headerImg= (MyHeaderCricleView) view.findViewById(R.id.profile_image);
		userNameTxt= (TextView) view.findViewById(R.id.left_user_name_txt);
		userPhoneTxt=(TextView) view.findViewById(R.id.left_user_phone_txt);
		mQbLine=view.findViewById(R.id.qiabao_line);
		mMyDingDanLine=view.findViewById(R.id.dingdan_line);
		mAdsLine=view.findViewById(R.id.ads_line);
		mMessageLine=view.findViewById(R.id.message_line);
		mSuggestLine=view.findViewById(R.id.suggest_line);
		mSettingLine=view.findViewById(R.id.setting_line);
		mTelLine=view.findViewById(R.id.tel_line);

	}

	private void setListeners() {
		mHeaderView.setOnClickListener(this);
		mQbView.setOnClickListener(this);
		mMyDingDanView.setOnClickListener(this);
		mAdsView.setOnClickListener(this);
		mMessageView.setOnClickListener(this);
		mSuggestView.setOnClickListener(this);
		mSettingView.setOnClickListener(this);
		mTelView.setOnClickListener(this);
	}
	private void getUserInfo() {
		MyOkHttpUtils.getRequest(Urls.USER_INFO).execute(new MapCallback(fca,false) {
			@Override
			public void onResponse(Map rMap) {
				super.onResponse(rMap);
				if (rMap != null) {
					int rcode = (int) rMap.get("rcode");
					if (rcode == 0) {
						Map<String, Object> data = (Map<String, Object>) rMap.get("data");
						DebugLog.e("Response", "getUserInfo="+data.toString());
						MyApplication.userInfo = data;
						MyApplication.uid = TextUtil.getTextToString(data.get("id"));
						if (!TextUtils.isEmpty((String) data.get("userAccount"))){
							String userAccount=  TextUtil.getTextToString(data.get("userAccount"));
							userNameTxt.setText(userAccount);
						}
						if (!TextUtils.isEmpty((String) data.get("phone"))){
							String phone= TextUtil.getTextToString(data.get("phone"));
							userPhoneTxt.setText(phone);
						}
						if (!TextUtils.isEmpty((String) data.get("head"))){
							String headerUrl= TextUtil.getTextToString(data.get("head"));
							DisplayImageOptions options=PictureUtil.setImageOptions(fca,R.drawable.pic_load,4);
							MyApplication.imageLoader.displayImage(headerUrl,headerImg,options);
						}
						saveUserInfo(data.toString());
					}
				}
			}
		});
	}
	/**
	 * 存储用户信息
	 */
	private void saveUserInfo(String data) {
		SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(fca, Contanct.SP_USER_NAME);
		sharedPrefUtil.putString(Contanct.SP_USERINFO, data);
		sharedPrefUtil.commit();
		DebugLog.e("Response", "222222222222");
	}


	/**
	 * 返回的 图片 url
	 * @param bean
	 */
	public void onEventMainThread(ImageBean bean){
		if (Contanct.UPLOAD_STYLE.equals(Contanct.UPLOAD_HEAD)){
			urls = bean.getImgStr();
			DebugLog.e("Response", "onEventMainThread==1111111111111"+urls );
			DisplayImageOptions options=PictureUtil.setImageOptions(fca,R.drawable.pic_load,4);
			MyApplication.imageLoader.displayImage(urls,headerImg,options);
			DebugLog.e("Response", "onEventMainThread==22222222222222222"+urls );
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.left_user_name_lay: // 个人信息
          if (MathCommon.isLogin(fca)){
			intent.setClass(fca, PersonInfoActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.my_qianbao_lay://钱包
			if (MathCommon.isLogin(fca)){
				setBackLine(mQbLine,mMyDingDanLine,mAdsLine,mMessageLine,mSuggestLine,mSettingLine,mTelLine);
				intent.setClass(fca, MyQianBaoActivity.class);
				startActivity(intent);
			}

			break;
		case R.id.left_dingdan_lay: // 订单
			if (MathCommon.isLogin(fca)){
				setBackLine(mMyDingDanLine,mQbLine,mAdsLine,mMessageLine,mSuggestLine,mSettingLine,mTelLine);
				intent.setClass(fca, MyOrderActivity.class);
				intent.putExtra("order", 1);
				startActivity(intent);

			}


			break;
		case R.id.left_ads_lay: // 广告
			if (MathCommon.isLogin(fca)){
				setBackLine(mAdsLine,mMyDingDanLine,mQbLine,mMessageLine,mSuggestLine,mSettingLine,mTelLine);
				intent.setClass(fca, AdsRevenueActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.left_message_lay: // 信息
			if (MathCommon.isLogin(fca)){
				setBackLine(mMessageLine,mMyDingDanLine,mAdsLine,mQbLine,mSuggestLine,mSettingLine,mTelLine);
				intent.setClass(fca, MessageCenterActivity.class);
				startActivity(intent);
			}
			break;
			case R.id.left_suggest_lay: // 建议
				if (MathCommon.isLogin(fca)){
					setBackLine(mSuggestLine,mMyDingDanLine,mAdsLine,mMessageLine,mQbLine,mSettingLine,mTelLine);
					intent.setClass(fca, SuggestRKStyleActivity.class);
					startActivity(intent);

				}
				break;
			case R.id.left_setting_lay: // 设置;
				setBackLine(mSettingLine,mMyDingDanLine,mAdsLine,mMessageLine,mSuggestLine,mQbLine,mTelLine);
				intent.setClass(fca, SettingActivity.class);
				startActivity(intent);
				break;
			case R.id.left_tel_lay:
				setBackLine(mTelLine,mMyDingDanLine,mAdsLine,mMessageLine,mSuggestLine,mSettingLine,mQbLine);
				 intent = new Intent(Intent.ACTION_CALL);
				 intent.setData(Uri.parse("tel:"+telNum));
				 startActivity(intent);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						EventBus.getDefault().post(Contanct.MAINACTIVTTY);
					}
				},500);
				break;
			default:
				break;
		}

	}
   //设置选择线
	private void setBackLine(View mQbLine, View mMyDingDanLine, View mAdsLine, View mMessageLine, View mSuggestLine, View mSettingLine, View mTelLine) {
		mQbLine.setBackgroundColor(getResources().getColor(R.color.white));
		mMyDingDanLine.setBackgroundColor(0);
		mAdsLine.setBackgroundColor(0);
		mMessageLine.setBackgroundColor(0);
		mSuggestLine.setBackgroundColor(0);
		mSettingLine.setBackgroundColor(0);
		mTelLine.setBackgroundColor(0);
	}
	private void getMyMessageData() {
		String  messageUrls=Urls.MYMASSGE_URLS+"?p="+1;
		MyOkHttpUtils.getRequest(messageUrls).execute(new MapCallback(fca) {
			@Override
			public void onResponse(Map rMap) {
				super.onResponse(rMap);
				if (rMap != null) {
					int rcode = (int) rMap.get("rcode");
					if (rcode == 0) {
						List<Map<String, Object>> bannersList = (List<Map<String, Object>>) rMap.get("data");
						msgTxt.setText(bannersList.size()+"");
					}
				}
			}
		});
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
