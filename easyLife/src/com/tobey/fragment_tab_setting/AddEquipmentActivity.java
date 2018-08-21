package com.tobey.fragment_tab_setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android_serialport_api.MyApplication;

import com.tobey.dao_HTCP.Operator2HTCP;
import com.tobey.easyLife.R;

public class AddEquipmentActivity extends Activity implements OnClickListener {

	private View rl_return;
	private TextView tv_livingRoom, tv_kitchen, tv_master, tv_study, tv_toilet, tv_balcony;
	private Button bt_add;
	private EditText et_euipmentId, et_equipmentName;
	private String path_AddDevice;
	private Operator2HTCP operator2htcp;
	private int status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_equipment);
		MyApplication myApplication = (MyApplication)this.getApplication();
		path_AddDevice = myApplication.getPath_AddDevice();
		operator2htcp = new Operator2HTCP(this, "AddEquipmentActivity");
		initViews();
	}

	private void initViews() {
		tv_livingRoom = (TextView) findViewById(R.id.activity_add_equipment_tv_livingRoom);
		tv_kitchen = (TextView) findViewById(R.id.activity_add_equipment_tv_kitchen);
		tv_master = (TextView) findViewById(R.id.activity_add_equipment_tv_master);
		tv_study = (TextView) findViewById(R.id.activity_add_equipment_tv_study);
		tv_toilet = (TextView) findViewById(R.id.activity_add_equipment_tv_toilet);
		tv_balcony = (TextView) findViewById(R.id.activity_add_equipment_tv_balcony);
		bt_add = (Button) findViewById(R.id.activity_add_equipment_bt_add);
		et_equipmentName = (EditText) findViewById(R.id.activity_add_equipment_et_name);
		et_euipmentId = (EditText) findViewById(R.id.activity_add_equipment_et_id);
		rl_return = findViewById(R.id.activity_add_equipment_rl_return);
		
		tv_livingRoom.setOnClickListener(this);
		tv_kitchen.setOnClickListener(this);
		tv_master.setOnClickListener(this);
		tv_study.setOnClickListener(this);
		tv_toilet.setOnClickListener(this);
		bt_add.setOnClickListener(this);
		rl_return.setOnClickListener(this);
		tv_balcony.setOnClickListener(this);
		
	}


	@Override
	public void onClick(View view) {
		int select = view.getId();
		switch(select) {
		case R.id.activity_add_equipment_tv_livingRoom:
			et_equipmentName.setText("客厅-");
			break;
		case R.id.activity_add_equipment_tv_kitchen:
			et_equipmentName.setText("厨房-");
			break;
		case R.id.activity_add_equipment_tv_master:
			et_equipmentName.setText("主卧-");
			break;
		case R.id.activity_add_equipment_tv_study:
			et_equipmentName.setText("书房-");
			break;
		case R.id.activity_add_equipment_tv_toilet:
			et_equipmentName.setText("洗手间-");
			break;
		case R.id.activity_add_equipment_tv_balcony:
			et_equipmentName.setText("阳台-");
			break;
		case R.id.activity_add_equipment_bt_add:
			String[] TYPEandID = et_euipmentId.getText().toString().split("-");
			String ID = null;
			String TYPE = null;
			String NAME = null;
			if(TYPEandID.length == 2) {
				ID = TYPEandID[1];
				TYPE = TYPEandID[0];
				NAME = et_equipmentName.getText().toString();
				addAdvice(ID, TYPE, NAME);
			} else {
				showMessage("设备ID填写有误！请重新填写");
				et_euipmentId.setHint("请输入设备ID");
			}
			
			break;
		case R.id.activity_add_equipment_rl_return:
			this.finish();
		default:
			break;
		}
	}
	
	/**
	 * 向服务器发送数据
	 * @param path：地址+需要上传的数据
	 */
	private void addAdvice(String id, String type, String name) {
		String path = path_AddDevice + "&getId=" + id + "&getType=" + type
				+ "&getName=" + name;
		operator2htcp.dealHTCP(path);
	}
	
	private void showMessage(String sMsg)
    {
        Toast.makeText(this, sMsg, Toast.LENGTH_SHORT).show();
    }

	private void clearData() {
		et_equipmentName.setText(null);
		et_euipmentId.setText(null);
	}
}
