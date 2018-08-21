package com.tobey.fragment_dynamicfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android_serialport_api.MyApplication;

import com.tobey.adapter.Adapter_pwm;
import com.tobey.bean_HTCP.ResultHTCP;
import com.tobey.dao_HTCP.Operator2HTCP;
import com.tobey.easyLife.R;


public class IlluminationFragment extends Fragment {
	
	// ListView
	private ListView illuminationLv;
	
	Handler handler = new Handler();
	// ��Ƭ����
	private View illuminationLayout;
	private String TYPE = "9";
	
	// ��Fragment��Ҫʹ��ListView����Ҫ��ListFragment
	private Adapter_pwm adapter; 
	// HTCP�е�����
	private List<ResultHTCP> resultFromHTCP = new ArrayList<ResultHTCP>();
	private ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();//listview����ʾ������
	private Operator2HTCP operator2htcp = new Operator2HTCP(getActivity(),"IlluminationFragment");
	private int status;
     // ���ݿ����
    private String path_getHTCP ;
    // �̹߳���
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		illuminationLayout = inflater.inflate(R.layout.fragment_illumination,
				container, false);
		MyApplication myApplication = ((MyApplication)getActivity().getApplication());
        /**
         * ��ȡ���ڼ����ݿ�������
         */
        path_getHTCP = myApplication.getPath_getHTCP();
		return illuminationLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();  
		//���ɶ�̬���飬����ת������  
	    //����������������===��ListItem  
		adapter = new Adapter_pwm(mylist, getActivity());
	    illuminationLv.setAdapter(adapter); 
	    //ÿ1s����һ�ο���λ��
	    //����ִ��
	    handler.post(pwmRunnable);
	    handler.postDelayed(removePwmRunnable, 1000);
	    handler.post(updataRunnable);
	}
	
	private Runnable pwmRunnable = new Runnable() {
		
		public void run() {
			getHTCP(path_getHTCP);
			getData();
			adapter.notifyDataSetChanged();
			handler.postDelayed(this, 500);
		} 
	};
	
	private Runnable removePwmRunnable = new Runnable() {
		
		public void run() {
			handler.removeCallbacks(pwmRunnable);
		}
	};
	
	private Runnable updataRunnable = new Runnable() {
		
		public void run() {
			getHTCP(path_getHTCP);
			getData();
			adapter.notifyDataSetChanged();
			handler.postDelayed(this, 300000);
		} 
	};
	
	private void getData() {
		try {
			Iterator<ResultHTCP> it = resultFromHTCP.iterator();
	    	ResultHTCP sensorFromHTCP = new ResultHTCP();
	    	mylist.clear();
	    	while(it.hasNext()) {
	    		sensorFromHTCP = it.next();
	    		
	    		HashMap<String, String> map = new HashMap<String, String>(); 
	    		if(TYPE.equals(sensorFromHTCP.getGetType())) {
	    			map.put("id", sensorFromHTCP.getGetId());
		    		map.put("level", sensorFromHTCP.getDdata());
		    		map.put("name", sensorFromHTCP.getDname());
		    		mylist.add(map);
	    		}
	    		
	    	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	public void initView() {
		illuminationLv = (ListView) illuminationLayout.findViewById(R.id.fragment_illumination_lv);
	}

    // ������������
    public void getHTCP(String path){
    	status = operator2htcp.getHTCP(path);
		switch(status) {
		case 1:
			resultFromHTCP = operator2htcp.getResultFromHTCP();
			break;
		case 0:
			break;
		default:
			break;
		}
    }
}