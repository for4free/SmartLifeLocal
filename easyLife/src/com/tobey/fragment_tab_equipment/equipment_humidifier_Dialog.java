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

public class equipment_humidifier_Dialog extends Dialog implements OnClickListener {

	private Context context;
	private Button bt_open, bt_close;
	private euipment_humidifier_DialogListener  listener;
	private TextView tv_humidifier_message, tv_humidifier_message_delete;
	private Button bt_confirm, bt_cancel;
	private View rl_firstView, rl_secondView;
	private ImageView iv_delete;
	private String status;
	private String name;
	private View view;
	
	public equipment_humidifier_Dialog(Context context) {
		super(context);
		this.context = context;
	}

	public interface euipment_humidifier_DialogListener {
		public void onClick(View view);
	}

	public equipment_humidifier_Dialog(String name,String newdata,Context context, int theme,euipment_humidifier_DialogListener listener) {
		super(context, theme);
		this.name = name;
		this.status = newdata;
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = this.getLayoutInflater().inflate(R.layout.dialog_equipment_humidifier, null);
//		this.setContentView(R.layout.dialog_equipment_humidifier);
		setContentView(view);
		initViews();
	}

	private void initViews() {
		bt_open = (Button) view.findViewById(R.id.dialog_equipment_humidifier_bt_open);
		bt_close = (Button) view.findViewById(R.id.dialog_equipment_humidifier_bt_close);
		tv_humidifier_message = (TextView) view.findViewById(R.id.dialog_equipment_humidifier_tv_message);
		tv_humidifier_message_delete = (TextView) view.findViewById(R.id.dialog_equipment_humidifier_tv_message_delete);
		iv_delete = (ImageView) view.findViewById(R.id.dialog_equipment_humidifier_iv_delete);;
		bt_cancel = (Button) findViewById(R.id.dialog_equipment_humidifier_bt_cancel);
		bt_confirm = (Button) findViewById(R.id.dialog_equipment_humidifier_bt_confirm);
		rl_firstView = findViewById(R.id.dialog_equipment_humidifier_rl_firstView);
		rl_secondView = findViewById(R.id.dialog_equipment_humidifier_rl_secondView);
			
		if(status.equals("0")){
			tv_humidifier_message.setText(name+" 已经关闭");
		}else{
			tv_humidifier_message.setText(name+" 已经开启");
		}
		
		bt_open.setOnClickListener(this);
		bt_close.setOnClickListener(this);
		iv_delete.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		bt_confirm.setOnClickListener(this);
	}

	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
//		TextVew message = 
		WrapperExpandableListAdapter.handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				String status = null;
//				super.handleMessage(msg);
				if(msg.what == 1) {
					status = msg.getData().getString("status");
					if(status != null) {
						if(status.equals("0")){
							tv_humidifier_message.setText(name+" 已经关闭");
						}else if(status.equals("1")){
							tv_humidifier_message.setText(name+" 已经开启");
						}else if(status.equals("delete")){
							rl_firstView.setVisibility(View.GONE);
							name = msg.getData().getString("name");
							tv_humidifier_message_delete.setText("确认删除设备：" + name + "?");
							rl_secondView.setVisibility(View.VISIBLE);
						}else if(status.equals("cancel")){
							dismiss();
						}else if(status.equals("confirm")){
							dismiss();
						}
					}
				}
				
//				dismiss();
			}
			
		};
		listener.onClick(view);
	}
}

