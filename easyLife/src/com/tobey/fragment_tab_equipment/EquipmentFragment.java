package com.tobey.fragment_tab_equipment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android_serialport_api.MyApplication;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tobey.bean_HTCP.GetData;
import com.tobey.bean_HTCP.ResultHTCP;
import com.tobey.easyLife.R;
import com.tobey.fragment_tab_setting.AddEquipmentActivity;

public class EquipmentFragment extends Fragment implements View.OnClickListener  {

	 //碎片布局
	private View equipmentLayout;
	private FloatingGroupExpandableListView expandableListView;
	private View addEquipmentLayout;
	private View returnLayout;
	/**
	 * HTCP中的数据
	 */
	private GetData getDataFromHTCP;
	private List<ResultHTCP> resultFromHTCP = new ArrayList<ResultHTCP>();
//	String path = "http://123.206.78.18/api/TGetData.php?tid=1234";
	String path;
	String[][] mChilds = new String[4][10];
	Handler handler = new Handler();
	private List<String> groupList;
	private  Map<String,List<Map<String,String>>> childMap;
	private List<Map<String,String>> listPyro = new ArrayList<Map<String,String>>();//热释电
	private List<Map<String,String>> listSocket = new ArrayList<Map<String,String>>();//照明
	private List<Map<String,String>> listIllumination = new ArrayList<Map<String,String>>();//插座
	private List<Map<String,String>> listCurtain = new ArrayList<Map<String,String>>();//窗帘
	private List<Map<String,String>> listAirConditioning = new ArrayList<Map<String,String>>();//空调
	private List<Map<String,String>> listTV = new ArrayList<Map<String,String>>();//电视
	private List<Map<String,String>> listTHL = new ArrayList<Map<String,String>>();//温湿度
	private List<Map<String,String>> listHumidifier = new ArrayList<Map<String,String>>();//增湿器
	private List<Map<String,String>> listAirCleaner = new ArrayList<Map<String,String>>();//空气净化器
	private EuipmentExpandableListAdapter adapter;
	private WrapperExpandableListAdapter wrapperAdapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		equipmentLayout = inflater.inflate(R.layout.fragment_equipment,
				container, false);
		MyApplication myApplication = (MyApplication)getActivity().getApplication();
		path = myApplication.getPath_getHTCP();
		return equipmentLayout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		expandableListView = (FloatingGroupExpandableListView) equipmentLayout.findViewById(R.id.fragment_equipment_expandablelist);
		InitData();
		
//		final LayoutInflater inflater = getActivity().getLayoutInflater();
//		final View header = inflater.inflate(R.layout.fragment_equipment_list_header, expandableListView, false);
//		expandableListView.addHeaderView(header);
		
		adapter = new EuipmentExpandableListAdapter(getActivity(),groupList,childMap);
		
		wrapperAdapter = new WrapperExpandableListAdapter(adapter, getActivity());
		expandableListView.setAdapter(wrapperAdapter);
		
		//每1s更新一次控制位置
	    //定期执行
	    handler.post(getDataFirstRunnable);
	    handler.postDelayed(removegetDataFirstRunnable, 1000);
	    handler.postDelayed(getDataRunnable, 500);
	}

	/***
	 * InitData
	 */
	void InitData() {
		addEquipmentLayout = equipmentLayout.findViewById(R.id.fragment_equipment_iv_add);
		returnLayout = equipmentLayout.findViewById(R.id.fragment_equipment_iv_return);
		addEquipmentLayout.setOnClickListener((android.view.View.OnClickListener)this);
		returnLayout.setOnClickListener((android.view.View.OnClickListener)this);
		// 数据源  
		childMap = new HashMap<String, List<Map<String,String>>>();
		groupList = new ArrayList<String>();
		
		groupList = new ArrayList<String>();
		
		groupList.add("照明");
		groupList.add("插座");
		groupList.add("窗帘");
		groupList.add("空调");
		groupList.add("电视");
		groupList.add("温湿度");
		groupList.add("增湿器");
		groupList.add("空气净化机");
		groupList.add("安防");

	}

	private Runnable getDataFirstRunnable = new Runnable() {
		
		public void run() {
			getHTCP(path);
			handler.postDelayed(this, 500);
		} 
	};
	
	private Runnable removegetDataFirstRunnable = new Runnable() {
		
		public void run() {
			handler.removeCallbacks(getDataFirstRunnable);
		}
	};
	
	private Runnable getDataRunnable = new Runnable() {
		
		public void run() {
			getHTCP(path);
			handler.postDelayed(this, 1000);
		} 
	};
	/**
     * 加载数据数据
     * @param path
     */
    public void getHTCP(String path){
    	HttpUtils utils = new HttpUtils();
    	utils.configCurrentHttpCacheExpiry(100);
    	utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

    		public void onFailure(HttpException arg0, String arg1) {
			}
			
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				//加载数据  转化为JSON格式
				try {
					getDataFromHTCP = (GetData) JSONObject.parseObject(arg0.result, GetData.class);
					//根据字段用get方法获取字段内容
					if(!getDataFromHTCP.equals(null)) {
						resultFromHTCP = getDataFromHTCP.getResult();
						createStringGroup();
					}
					if(resultFromHTCP.isEmpty()) {
						Log.e("getResult","getHTCP result is empty ");
						
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				adapter.notifyDataSetChanged();
			}

		});
    }
    
    public void createStringGroup() {
    	try {
    		Iterator<ResultHTCP> it = resultFromHTCP.iterator();
        	ResultHTCP sensorFromHTCP = new ResultHTCP();
        	childMap.clear();
        	listPyro.clear();
        	listIllumination.clear();
        	listSocket.clear();
        	listCurtain.clear();
        	listAirConditioning.clear();
        	listTHL.clear();
        	listTV.clear();
        	listHumidifier.clear();
        	listAirCleaner.clear();
        	while(it.hasNext()) {
        		sensorFromHTCP = it.next();
        		Map<String,String> map = new HashMap<String,String>();
        		map.put("type", sensorFromHTCP.getGetType());
        		map.put("name", sensorFromHTCP.getDname());
        		map.put("id", sensorFromHTCP.getGetId());
        		map.put("status", sensorFromHTCP.getDdata());
        		map.put("flag", sensorFromHTCP.getFlag());
        		String type = sensorFromHTCP.getGetType();
    	    	switch(type) {
    	    	case "2"://"温湿度”
    	    		listTHL.add(map);
    	    		break;
    	    	case "5"://"热释电",//--》5
    				listPyro.add(map);
    	    		break;
    	    	case "6"://"窗帘",//电机--》6
    	    		listCurtain.add(map);
    	    		break;
    	    	case "9"://"pwm"
    	    		listIllumination.add(map);
    	    		break;
    	    	case "a1"://"插座",//继电器--》a
    	    		listSocket.add(map);
    	    		break;
    	    	case "a2"://"空调",//待定--》A2
    	    		listAirConditioning.add(map);
    	    		break;
    	    	case "a3"://"电视",//待定--》A3
    	    		listTV.add(map);
    	    		break;
    	    	case "a4"://"加湿器",//待定--》A4
    	    		listHumidifier.add(map);
    	    		break;
    	    	case "a5"://"空气净化器",//待定--》A5
    	    		listAirCleaner.add(map);
    	    		break;
    			default:
    				break;
    	    	}
        	}
        	
        	childMap.put("安防",listPyro);
        	childMap.put("照明",listIllumination);
        	childMap.put("插座",listSocket);
        	childMap.put("窗帘",listCurtain);
        	childMap.put("空调",listAirConditioning);
        	childMap.put("电视",listTV);
        	childMap.put("温湿度",listTHL);
        	childMap.put("增湿器",listHumidifier);
        	childMap.put("空气净化机",listAirCleaner);
        
		} catch (Exception e) {
			// TODO: handle exception
		}
    }

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch(id) {
		case R.id.fragment_equipment_iv_return:
			getFragmentManager().popBackStack();
			break;
		case R.id.fragment_equipment_iv_add:
			Intent intent = new Intent(getActivity(), AddEquipmentActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}



}
