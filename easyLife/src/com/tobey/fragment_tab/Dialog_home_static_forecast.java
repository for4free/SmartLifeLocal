package com.tobey.fragment_tab;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tobey.adapter.Adapter_forecast;
import com.tobey.easyLife.R;

public class Dialog_home_static_forecast extends Dialog {

	private Context context;
	private ListView lv_list;
	private Button bt_confirm;
	private TextView tv_message;
	private String title;
	private ArrayList<HashMap<String, String>> myList;
	private Adapter_forecast adapter;
	
	public Dialog_home_static_forecast(Context context) {
		super(context);
	}
	
	public Dialog_home_static_forecast(Context context, String description, ArrayList<HashMap<String, String>> myList, int theme) {
		super(context,theme);
		this.context = context;
		this.myList = myList;
		this.title = description;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_forecast);
		initViews();
		
	}

	private void initViews() {
		lv_list = (ListView) findViewById(R.id.dialog_forecast_lv);
		tv_message = (TextView) findViewById(R.id.dialog_forecast_tv_message);
		tv_message.setText(title);
		
		adapter = new Adapter_forecast(context, myList);
		lv_list.setAdapter(adapter);
	}

}







