package com.tobey.fragment_tab_equipment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tobey.easyLife.R;

public class equipment_THL_Dialog extends Dialog implements OnClickListener {

	private Context context;
	private TextView tv_THL_message;
	private TextView tv_THL_temp;
	private TextView tv_THL_hum;
	private TextView tv_THL_light, tv_THL_message_delete;
	private Button bt_confirm, bt_cancel;
	private View rl_firstView, rl_secondView;
	private ImageView iv_delete;
	private equipment_THL_DialogListener  listener;
	private String status;
	private String name;
	public equipment_THL_Dialog(Context context) {
		super(context);
		this.context = context;
	}

	public interface equipment_THL_DialogListener {
		public void onClick(View view);
	}

	public equipment_THL_Dialog(String name,String newdata,Context context, int theme, equipment_THL_DialogListener  listener) {
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
		this.setContentView(R.layout.dialog_equipment_thl);
		initViews();
	}

	private void initViews() {
		tv_THL_message = (TextView) findViewById(R.id.dialog_equipment_thl_tv_message);
		tv_THL_message_delete = (TextView) findViewById(R.id.dialog_equipment_thl_tv_message_delete);
		tv_THL_temp = (TextView) findViewById(R.id.dialog_equipment_thl_tv_temp);
		tv_THL_hum = (TextView) findViewById(R.id.dialog_equipment_thl_tv_hum);
		tv_THL_light = (TextView) findViewById(R.id.dialog_equipment_thl_tv_light);
		iv_delete = (ImageView) findViewById(R.id.dialog_equipment_thl_iv_delete);
		bt_cancel = (Button) findViewById(R.id.dialog_equipment_thl_bt_cancel);
		bt_confirm = (Button) findViewById(R.id.dialog_equipment_thl_bt_confirm);
		rl_firstView = findViewById(R.id.dialog_equipment_thl_rl_firstView);
		rl_secondView = findViewById(R.id.dialog_equipment_thl_rl_secondView);
		tv_THL_message.setText(name);
		String[] statusPiece = status.split("_");
		if(0 != statusPiece.length) {
			tv_THL_temp.setText(statusPiece[0] + "℃");
			tv_THL_hum.setText(statusPiece[1] + "%");
			tv_THL_light.setText(statusPiece[2] + "LUM");
		}
		iv_delete.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		bt_confirm.setOnClickListener(this);
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
						tv_THL_message_delete.setText("确认删除设备：" + name + "?");
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
