package com.cn.aixiyi.myview;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cn.aixiyi.R;


/*
 * Author: Administrator Email:gdpancheng@gmail.com
 * Created Date:2013-9-9
 * Copyright @ 2013 BU
 * Description: 类描
 *
 * History:
 */
public class CustomDialog extends Dialog {

	private  Context context;
	private  int     layout_id;
	private  ButtonBack   buttonBack;
	private  String   message;
	private  String   left;
	private  String   right;
	private  TextView text_message;
	private  Button   btn_submite;
	private  Button   btn_cancel;
	public CustomDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_dialog);
		setCanceledOnTouchOutside(true);
		setTitle("");
		text_message=(TextView) findViewById(R.id.text_message);
		text_message.setText(message);
		btn_submite=(Button) findViewById(R.id.button_submite);
		btn_cancel=(Button) findViewById(R.id.button_cancel);
		if(!TextUtils.isEmpty(left)){
			
			btn_cancel.setText(left);
			
		}
		
		if(!TextUtils.isEmpty(right)){
			
			btn_submite.setText(right);
		}
		btn_submite.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buttonBack.submite();
				dismiss();
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buttonBack.cancel();
				dismiss();
			}
		});
		
		
	}
	
	
	public CustomDialog(Context context,String message ,String left,String right, ButtonBack back){
		
		super(context,R.style.MyDialog);
		this.message=message;
		this.left=left;
		this.right=right;
		this.buttonBack=back;
	}
	
	
}
