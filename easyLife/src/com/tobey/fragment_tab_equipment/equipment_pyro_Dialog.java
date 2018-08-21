package com.tobey.fragment_tab_equipment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tobey.easyLife.R;

public class equipment_pyro_Dialog extends Dialog implements OnClickListener {

	private Context context;
	private TextView tv_pyro_message, tv_pyro_message_delete;
	private Button bt_confirm, bt_cancel;
	private View rl_firstView, rl_secondView;
	private ImageView iv_delete;
	private String status;
	private String name;
	private equipment_pyro_DialogListener  listener;
	public equipment_pyro_Dialog(Context context) {
		super(context);
		this.context = context;
	}

	public interface equipment_pyro_DialogListener {
		public void onClick(View view);
	}

	public equipment_pyro_Dialog(String name,String newdata,Context context, int theme, equipment_pyro_DialogListener  listener) {
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
		this.setContentView(R.layout.dialog_equipment_pyro);
		initViews();
	}

	private void initViews() {
		tv_pyro_message = (TextView) findViewById(R.id.dialog_equipment_pyro_tv_message);
		iv_delete = (ImageView) findViewById(R.id.dialog_equipment_pyro_iv_delete);
		tv_pyro_message_delete = (TextView) findViewById(R.id.dialog_equipment_pyro_tv_message_delete);
		bt_cancel = (Button) findViewById(R.id.dialog_equipment_pyro_bt_cancel);
		bt_confirm = (Button) findViewById(R.id.dialog_equipment_pyro_bt_confirm);
		rl_firstView = findViewById(R.id.dialog_equipment_pyro_rl_firstView);
		rl_secondView = findViewById(R.id.dialog_equipment_pyro_rl_secondView);
		if(status.split("_")[1].equals("0")){
			tv_pyro_message.setText(name+" 无人");
		}else{
			tv_pyro_message.setText(name+" 有人");
		}
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
				String name;
				if(msg.what == 1){
					if(status.equals("delete")){
						rl_firstView.setVisibility(View.GONE);
						name = msg.getData().getString("name");
						tv_pyro_message_delete.setText("确认删除设备：" + name + "?");
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
