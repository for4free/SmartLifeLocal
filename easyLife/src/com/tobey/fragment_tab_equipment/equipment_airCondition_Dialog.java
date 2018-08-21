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

public class equipment_airCondition_Dialog extends Dialog implements OnClickListener {

	private Context context;
	private Button bt_power, bt_close,suspend;
	private euipment_airCondition_DialogListener  listener;
	private TextView tv_airCondition_message, tv_airCondition_message_delete;
	private ImageView iv_delete;
	private Button bt_confirm, bt_cancel;
	private View rl_firstView, rl_secondView;
	private String newdata;
	private String name;
	public equipment_airCondition_Dialog(Context context) {
		super(context);
		this.context = context;
	}

	public interface euipment_airCondition_DialogListener {
		public void onClick(View view);
	}

	public equipment_airCondition_Dialog(String name,String newdata,Context context, int theme,euipment_airCondition_DialogListener listener) {
		super(context, theme);
		this.name = name;
		this.newdata = newdata;
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_equipment_aircondition);
		initViews();
	}

	private void initViews() {
		//btn_close = (Button) findViewById(R.id.dialog_list01_but_close);
		iv_delete = (ImageView) findViewById(R.id.dialog_equipment_airCondition_iv_delete);
		bt_cancel = (Button) findViewById(R.id.dialog_equipment_airCondition_bt_cancel);
		bt_confirm = (Button) findViewById(R.id.dialog_equipment_airCondition_bt_confirm);
		rl_firstView = findViewById(R.id.dialog_equipment_airCondition_rl_firstView);
		rl_secondView = findViewById(R.id.dialog_equipment_airCondition_rl_secondView);
		tv_airCondition_message_delete = (TextView) findViewById(R.id.dialog_equipment_airCondition_tv_message_delete);
		bt_power = (Button)findViewById(R.id.dialog_equipment_airCondition_bt_on);
		bt_close = (Button)findViewById(R.id.dialog_equipment_airCondition_bt_off);
		
		iv_delete.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		bt_confirm.setOnClickListener(this);
		bt_power.setOnClickListener(this);
		bt_close.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		WrapperExpandableListAdapter.handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				String status = msg.getData().getString("status");
				String name;
				if(msg.what == 1){
					if(status.equals("delete")){
						rl_firstView.setVisibility(View.GONE);
						name = msg.getData().getString("name");
						tv_airCondition_message_delete.setText("确认删除设备：" + name + "?");
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
