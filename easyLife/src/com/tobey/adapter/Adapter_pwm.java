package com.tobey.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android_serialport_api.MyApplication;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tobey.bean_HTCP.SetData;
import com.tobey.dao_serial.SensorDataGet;
import com.tobey.easyLife.R;

public class Adapter_pwm extends BaseAdapter {  

	private String TYPE = "9";
	/**
	 * 设备ID
	 */
	private String MyId;
	/**
	 * 更新数据路径
	 */
	private String path_setHTCP = "http://123.206.78.18/api/TSetData.php?Tid=";
    private LayoutInflater mInflater;  
    /**
     * 串口管理
     */
    private SensorDataGet sensorData;
    private ArrayList<HashMap<String, String>> mylist;
    
    public Adapter_pwm(ArrayList<HashMap<String, String>> list, Context context) { 
        this.mInflater = LayoutInflater.from(context);  
        MyApplication myApplication = ((MyApplication)context.getApplicationContext());
        /**
         * 获取串口及数据库管理对象
         */
        MyId = myApplication.getMyId();
        this.path_setHTCP = path_setHTCP  + String.valueOf(MyId);
        this.mylist = list; 
        this.sensorData = myApplication.getSensorData();
    }  

    public int getCount() {  
        // TODO Auto-generated method stub  
        return mylist.size();  
    }  
 
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return 0;  
    }  
    //****************************************final方法  
    //注意原本getView方法中的int position变量是非final的，现在改为final  
    @SuppressLint("InflateParams") public View getView(final int position, View convertView, ViewGroup parent) {  
         ViewHolder holder = null;  
         Log.e("Adapter","getView");
        if (convertView == null) {  
              
            holder=new ViewHolder();    
              
            //可以理解为从vlist获取view  之后把view返回给ListView  
              
            convertView = mInflater.inflate(R.layout.item_illumination, null);  
            holder.id = (TextView)convertView.findViewById(R.id.item_illumination_tv_id);  
            holder.sb_level = (SeekBar)convertView.findViewById(R.id.item_illumination_sb_level);  
            holder.name = (TextView)convertView.findViewById(R.id.item_illumination_tv_name);  
            convertView.setTag(holder);               
        }else {               
            holder = (ViewHolder)convertView.getTag();  
        }         
        holder.id.setText("");  
        holder.name.setText((String)mylist.get(position).get("name")); 
        holder.sb_level.setTag(position);
        holder.sb_level.setProgress(Integer.parseInt(mylist.get(position).get("level"))-1);
        holder.sb_level.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar sb) {
				final int level = sb.getProgress() + 1;
				/**
				 * 更新HTCP数据
				 */
				path_setHTCP = path_setHTCP + "&getType=" + TYPE + "&getId=" + 
						mylist.get(position).get("id") + "&newData=" + level + "&flag=" + "1";
				dealHTCP(path_setHTCP);
				
			}
			
			public void onStartTrackingTouch(SeekBar sb) {
				// TODO Auto-generated method stub
				
			}
			
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
			}
		});
          
        return convertView;  
    }  
  //提取出来方便点  
    public final class ViewHolder {  
        public TextView name;  
        public TextView id;  
        public SeekBar sb_level;
    }  
    /**
	 * 向服务器发送数据
	 * @param path：地址+需要上传的数据
	 */
	private void dealHTCP(String path) {
		Log.e("dealHTCP","Adapter_pwm");
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(500);
    	utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(
					com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				SetData setData = (SetData) JSONObject.parseObject(arg0.result, SetData.class);
				String status = setData.getStatus();
				if("403".equals(status))
				{
					Log.e("Adapter_pwm","更新成功！");
				}
				else if("402".equals(status))
				{
					Log.e("Adapter_pwm","数据未更改！");
				}
				else if("401".equals(status))
				{
					Log.e("Adapter_pwm","传入空值！");
				}
			}
    		
    	});
	}
}  
