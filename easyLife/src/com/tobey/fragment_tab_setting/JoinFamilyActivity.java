package com.tobey.fragment_tab_setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android_serialport_api.MyApplication;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tobey.bean_family.Family;
import com.tobey.easyLife.R;

public class JoinFamilyActivity extends Activity implements OnClickListener {

	private String path_getJoinFamily;
	private EditText et_id;
	private Button bt_join;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_family);
		MyApplication myApplication = ((MyApplication)getApplication());
        // 获取串口及数据库管理对象
		path_getJoinFamily = myApplication.getPath_JoinFamily();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		et_id = (EditText) findViewById(R.id.activity_join_family_et_id);
		bt_join = (Button) findViewById(R.id.activity_join_family_bt_joinfamily);
		bt_join.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch(id) {
		case R.id.activity_join_family_bt_joinfamily:
			String getId = et_id.getText().toString();
			String path = path_getJoinFamily + getId;
			Log.e("joinFamily", "before:" + path);
			if ( getId.equals("") ){
				Toast.makeText(this, "id号不能为空！", Toast.LENGTH_SHORT).show();
			} else {
//				String path = path_getJoinFamily + getId;
				Log.e("joinFamily", path);
				joinFamily(path);
			}
			
			break;
		default:
			break;
		}
	}
	
	public void joinFamily(String path) {
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(100);
    	utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(
					com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("name", "请检查网络连接");
				JoinFamilyActivity.this.setResult(0x01, intent);
				finish();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					Family resultFromFamily = (Family) JSONObject.parseObject(arg0.result, Family.class);
					Log.e("JoinFamily","status: " + resultFromFamily.getStatus());
					if ( resultFromFamily.getStatus().equals("403")) {
						// 返回数据
    					Intent intent = new Intent();
    					intent.putExtra("name", resultFromFamily.getFname());
    					JoinFamilyActivity.this.setResult(0x01, intent);
    					finish();
					} else {
						Intent intent = new Intent();
						intent.putExtra("name", "您还未加入家庭");
						JoinFamilyActivity.this.setResult(0x01, intent);
						finish();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
    		
    	});
    	
	}
	
}
