package com.tobey.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tobey.easyLife.R;

public class Adapter_forecast extends BaseAdapter{

	private ArrayList<HashMap<String, String>> myList;
	private LayoutInflater inflater;
	
	public Adapter_forecast(Context context, ArrayList<HashMap<String, String>> myList) {
		this.myList = myList;
		this.inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.item_forecast, null);
			holder = new Holder();
			holder.forecastImg = (ImageView) convertView.findViewById(R.id.item_forecase_iv_skyImg);
			holder.skyCon = (TextView) convertView.findViewById(R.id.item_forecase_tv_showSky);
			holder.temp = (TextView) convertView.findViewById(R.id.item_forecase_tv_showTempForecast);
			holder.pm25 = (TextView) convertView.findViewById(R.id.item_forecase_tv_showPm25);
			holder.time = (TextView) convertView.findViewById(R.id.item_forecase_tv_time);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
//		Log.e("AdapterForecast","imgID:" + myList.get(position).get("SkyImg"));
//		Log.e("AdapterForecast","Skycon:" + myList.get(position).get("Skycon"));
//		Log.e("AdapterForecast","Temperature:" + myList.get(position).get("Temperature"));
//		Log.e("AdapterForecast","Pm25:" + myList.get(position).get("Pm25"));
//		Log.e("AdapterForecast","Time:" + myList.get(position).get("Time"));
		switch(myList.get(position).get("Skycon")){
		case "«Á":
			holder.forecastImg.setImageResource(R.drawable.qingtian);
			break;
		case "«Á“π":
			holder.forecastImg.setImageResource(R.drawable.qingye);
			break;
		case "∂‡‘∆":
			holder.forecastImg.setImageResource(R.drawable.duoyunbaitian);
			break;
		case "∂‡‘∆“π":
			holder.forecastImg.setImageResource(R.drawable.duoyunye);
			break;
		case "“ı":
			holder.forecastImg.setImageResource(R.drawable.yin);
			break;
		case "”Í":
			holder.forecastImg.setImageResource(R.drawable.yu);
			break;
		case "∂≥”Í":
			holder.forecastImg.setImageResource(R.drawable.dongyu);
			break;
		case "—©":
			holder.forecastImg.setImageResource(R.drawable.xue);
			break;
		case "∑Á":
			holder.forecastImg.setImageResource(R.drawable.dafeng);
			break;
		case "ŒÌ":
			holder.forecastImg.setImageResource(R.drawable.wu);
			break;
		case "ˆ≤":
			holder.forecastImg.setImageResource(R.drawable.wu);
			break;
		}
		
		holder.skyCon.setText((String)myList.get(position).get("Skycon")); 
		holder.temp.setText((String)myList.get(position).get("Temperature")); 
		holder.pm25.setText((String)myList.get(position).get("Pm25")); 
		holder.time.setText((String)myList.get(position).get("Time")); 
		return convertView;
	}
	class Holder {
		ImageView forecastImg;
		TextView skyCon, temp, pm25, time;
	}
	
	
	
	
	

}
