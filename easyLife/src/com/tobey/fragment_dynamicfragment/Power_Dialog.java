package com.tobey.fragment_dynamicfragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tobey.easyLife.R;

public class Power_Dialog extends Dialog implements OnClickListener {

	private Context context;
	private Button bt_open, bt_close;
	private Power_DialogListener  listener;
	private TextView tv_dw_message;
	private String status;
	private String name;
	private View view;
	
	public Power_Dialog(Context context) {
		super(context);
		this.context = context;
	}

	public interface Power_DialogListener {
		public void onClick(View view);
	}

	public Power_Dialog(String name,String newdata,Context context, int theme,Power_DialogListener listener) {
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
		view = this.getLayoutInflater().inflate(R.layout.dialog_power, null);
		setContentView(view);
		initViews();
	}

	private void initViews() {
		bt_open = (Button) view.findViewById(R.id.dialog_power_bt_open);
		bt_close = (Button) view.findViewById(R.id.dialog_power_bt_close);
		tv_dw_message = (TextView) view.findViewById(R.id.dialog_power_tv_message);
		tv_dw_message.setText("电源：" + name);
		
		bt_open.setOnClickListener(this);
		bt_close.setOnClickListener(this);
	}

	
	@Override
	public void onClick(View view) {
		PowerFragment.handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				
				String status = null;
				if(msg.what == 1) {
					status = msg.getData().getString("status");
					if(status != null) {
						if(status.equals("open")){
							Toast.makeText(context, "电源已打开", Toast.LENGTH_SHORT).show();
						}else if(status.equals("close")){
							Toast.makeText(context, "电源已关闭", Toast.LENGTH_SHORT).show();
						}
					}
				}
				
				dismiss();
			}
			
		};
		listener.onClick(view);
	}
}

