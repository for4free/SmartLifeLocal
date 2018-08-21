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

	 //��Ƭ����
	private View equipmentLayout;
	private FloatingGroupExpandableListView expandableListView;
	private View addEquipmentLayout;
	private View returnLayout;
	/**
	 * HTCP�е�����
	 */
	private GetData getDataFromHTCP;
	private List<ResultHTCP> resultFromHTCP = new ArrayList<ResultHTCP>();
//	String path = "http://123.206.78.18/api/TGetData.php?tid=1234";
	String path;
	String[][] mChilds = new String[4][10];
	Handler handler = new Handler();
	private List<String> groupList;
	private  Map<String,List<Map<String,String>>> childMap;
	private List<Map<String,String>> listPyro = new ArrayList<Map<String,String>>();//���͵�
	private List<Map<String,String>> listSocket = new ArrayList<Map<String,String>>();//����
	private List<Map<String,String>> listIllumination = new ArrayList<Map<String,String>>();//����
	private List<Map<String,String>> listCurtain = new ArrayList<Map<String,String>>();//����
	private List<Map<String,String>> listAirConditioning = new ArrayList<Map<String,String>>();//�յ�
	private List<Map<String,String>> listTV = new ArrayList<Map<String,String>>();//����
	private List<Map<String,String>> listTHL = new ArrayList<Map<String,String>>();//��ʪ��
	private List<Map<String,String>> listHumidifier = new ArrayList<Map<String,String>>();//��ʪ��
	private List<Map<String,String>> listAirCleaner = new ArrayList<Map<String,String>>();//����������
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
		
		//ÿ1s����һ�ο���λ��
	    //����ִ��
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
		// ����Դ  
		childMap = new HashMap<String, List<Map<String,String>>>();
		groupList = new ArrayList<String>();
		
		groupList = new ArrayList<String>();
		
		groupList.add("����");
		groupList.add("����");
		groupList.add("����");
		groupList.add("�յ�");
		groupList.add("����");
		groupList.add("��ʪ��");
		groupList.add("��ʪ��");
		groupList.add("����������");
		groupList.add("����");

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
     * ������������
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
				//��������  ת��ΪJSON��ʽ
				try {
					getDataFromHTCP = (GetData) JSONObject.parseObject(arg0.result, GetData.class);
					//�����ֶ���get������ȡ�ֶ�����
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
    	    	case "2"://"��ʪ�ȡ�
    	    		listTHL.add(map);
    	    		break;
    	    	case "5"://"���͵�",//--��5
    				listPyro.add(map);
    	    		break;
    	    	case "6"://"����",//���--��6
    	    		listCurtain.add(map);
    	    		break;
    	    	case "9"://"pwm"
    	    		listIllumination.add(map);
    	    		break;
    	    	case "a1"://"����",//�̵���--��a
    	    		listSocket.add(map);
    	    		break;
    	    	case "a2"://"�յ�",//����--��A2
    	    		listAirConditioning.add(map);
    	    		break;
    	    	case "a3"://"����",//����--��A3
    	    		listTV.add(map);
    	    		break;
    	    	case "a4"://"��ʪ��",//����--��A4
    	    		listHumidifier.add(map);
    	    		break;
    	    	case "a5"://"����������",//����--��A5
    	    		listAirCleaner.add(map);
    	    		break;
    			default:
    				break;
    	    	}
        	}
        	
        	childMap.put("����",listPyro);
        	childMap.put("����",listIllumination);
        	childMap.put("����",listSocket);
        	childMap.put("����",listCurtain);
        	childMap.put("�յ�",listAirConditioning);
        	childMap.put("����",listTV);
        	childMap.put("��ʪ��",listTHL);
        	childMap.put("��ʪ��",listHumidifier);
        	childMap.put("����������",listAirCleaner);
        
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
