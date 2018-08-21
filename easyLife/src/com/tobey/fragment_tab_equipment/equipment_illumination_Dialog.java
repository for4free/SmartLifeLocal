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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android_serialport_api.MyApplication;

import com.tobey.dao_HTCP.Operator2HTCP;
import com.tobey.easyLife.R;

public class equipment_illumination_Dialog extends Dialog implements OnClickListener {

	private Context context;
	private Operator2HTCP operator2htcp = new Operator2HTCP(context, "equipment_illumination_Dialog");
	private TextView tv_illumination_message, tv_illumination_message_delete;
	private SeekBar sb_illumination_level;
	private Button bt_confirm, bt_cancel;
	private View rl_firstView, rl_secondView;
	private ImageView iv_delete;
	private String status;
	private String name;
	private equipment_illumination_DialogListener  listener;
	private String path_setHTCP;
	private String TYPE;
	private String ID;
	public equipment_illumination_Dialog(Context context) {
		super(context);
		this.context = context;
	}

	public interface equipment_illumination_DialogListener {
		public void onClick(View view);
	}

	public equipment_illumination_Dialog(String name,String newdata,String type, String id, Context context, int theme, equipment_illumination_DialogListener  equipment_illumination_DialogListener) {
		super(context, theme);
		this.name = name;
		this.status = newdata;
		this.context = context;
		this.listener = equipment_illumination_DialogListener;
		this.TYPE = type;
		this.ID = id;
		MyApplication myApplication = ((MyApplication)context.getApplicationContext());
        /**
         * 获取串口及数据库管理对象
         */
        path_setHTCP = myApplication.getPath_setHTCP();
        this.path_setHTCP = path_setHTCP;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_equipment_illumination);
		initViews();
	}

	private void initViews() {
		tv_illumination_message = (TextView) findViewById(R.id.dialog_equipment_illumination_tv_message);
		iv_delete = (ImageView) findViewById(R.id.dialog_equipment_illumination_iv_delete);
		tv_illumination_message_delete = (TextView) findViewById(R.id.dialog_equipment_illumination_tv_message_delete);
		bt_cancel = (Button) findViewById(R.id.dialog_equipment_illumination_bt_cancel);
		bt_confirm = (Button) findViewById(R.id.dialog_equipment_illumination_bt_confirm);
		rl_firstView = findViewById(R.id.dialog_equipment_illumination_rl_firstView);
		rl_secondView = findViewById(R.id.dialog_equipment_illumination_rl_secondView);
		sb_illumination_level = (SeekBar) findViewById(R.id.dialog_equipment_illumination_sb_level);
		sb_illumination_level.setProgress(Integer.parseInt(status)-1);
		sb_illumination_level.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar sb) {
				final int level = sb.getProgress() + 1;
				/**
				 * 更新HTCP数据
				 */
				upDataInfo(TYPE, ID, String.valueOf(level), "1");
				
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		tv_illumination_message.setText(name);
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
						tv_illumination_message_delete.setText("确认删除设备：" + name + "?");
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
	private void dealHTCP(String path) {
		operator2htcp.dealHTCP(path);
	}
	//修改设备信息
	private void upDataInfo(String type, String id,
			String newData, String flag) {
		// TODO Auto-generated method stub
		String path = path_setHTCP + 
				"&getType=" + type + 
				"&getId=" + id + 
				"&newData=" + newData + 
				"&flag=" + flag;
		dealHTCP(path);
	}

}
