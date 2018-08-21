package com.tobey.fragment_tab_equipment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tobey.easyLife.R;


public class equipment_curtain_Dialog extends Dialog implements OnClickListener {

	private Context context;
	private Button bt_open, bt_stop, bt_close;
	private equipment_curtain_DialogListener  listener;
	private TextView tv_curtain_message, tv_curtain_message_delete;
	private Button bt_confirm, bt_cancel;
	private View rl_firstView, rl_secondView;
	private ImageView iv_delete;
	private String status;
	private String name;
	public equipment_curtain_Dialog(Context context) {
		super(context);
		this.context = context;
	}

	public interface equipment_curtain_DialogListener {
		public void onClick(View view);
	}

	public equipment_curtain_Dialog(String name,String newdata,Context context, int theme,equipment_curtain_DialogListener listener) {
		super(context, theme);
		this.name = name;
		this.status = newdata;
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_equipment_curtain);
		initViews();
	}

	private void initViews() {
		bt_open = (Button) findViewById(R.id.dialog_equipment_curtain_bt_open);
		bt_stop = (Button) findViewById(R.id.dialog_equipment_curtain_bt_stop);
		bt_close = (Button) findViewById(R.id.dialog_equipment_curtain_bt_close);
		tv_curtain_message = (TextView) findViewById(R.id.dialog_equipment_curtain_tv_message);
		tv_curtain_message_delete = (TextView) findViewById(R.id.dialog_equipment_curtain_tv_message_delete);
		iv_delete = (ImageView) findViewById(R.id.dialog_equipment_curtain_iv_delete);
		bt_cancel = (Button) findViewById(R.id.dialog_equipment_curtain_bt_cancel);
		bt_confirm = (Button) findViewById(R.id.dialog_equipment_curtain_bt_confirm);
		rl_firstView = findViewById(R.id.dialog_equipment_curtain_rl_firstView);
		rl_secondView = findViewById(R.id.dialog_equipment_curtain_rl_secondView);
		if(status.equals("0")){
			tv_curtain_message.setText(name+" 已经关闭");
		}else{
			tv_curtain_message.setText(name+" 已经打开");
		}
		bt_open.setOnClickListener(this);
		bt_stop.setOnClickListener(this);
		bt_close.setOnClickListener(this);
		iv_delete.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		bt_confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
		WrapperExpandableListAdapter.handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				String status = msg.getData().getString("status");
				if(msg.what == 1){
					if(status.equals("0")){
						tv_curtain_message.setText(name+" 已经关闭");
					}else if(status.equals("1")){
						tv_curtain_message.setText(name+" 已经打开");
					}else if(status.equals("delete")){
						rl_firstView.setVisibility(View.GONE);
						name = msg.getData().getString("name");
						tv_curtain_message_delete.setText("确认删除设备：" + name + "?");
						rl_secondView.setVisibility(View.VISIBLE);
					}else if(status.equals("cancel")){
						dismiss();
					}else if(status.equals("confirm")){
						dismiss();
					}
				}
			}
			
		};
		listener.onClick(view);
	}
}
