package com.tobey.fragment_dynamicfragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.tobey.easyLife.R;


public class ElectricFragment extends Fragment {
	
	/**
	 * ListView
	 */
	private ListView electriLv;
	
	
	/**
	 * ��Ƭ����
	 */
	private View electricLayout;
	private Button bt_electric;
	
	/**
	* @���� ��Fragment��Ҫʹ��ListView����Ҫ��ListFragment
	* */
	private String TAG = this.getClass().getName();  
	private SimpleAdapter adapter; 
	private ArrayList<HashMap<String, String>> mylist;//listview����ʾ������

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		electricLayout = inflater.inflate(R.layout.fragment_electric,
				container, false);
		return electricLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();  
		//���ɶ�̬���飬����ת������  
	    mylist = new ArrayList<HashMap<String, String>>();  
	    //��ʼ���豸����
	    String[] list = {"��ʪ�ȹ��մ�����","���"};  
	    mylist = getData(list);
	    
	    //����������������===��ListItem  
	    adapter = new SimpleAdapter(getActivity(),
	    		mylist,//������Դ   
	    		R.layout.equipment_listitem, //ListItem��XMLʵ��  
	    		//��̬������ListItem��Ӧ������          
                new String[] {"ItemTitle", "ItemText"},   
	    		new int[]{R.id.euipment_tv_ItemTitle,R.id.equipment_tv_ItemText});  
	    //��Ӳ�����ʾ  
	    electriLv.setAdapter(adapter); 
	}
	
	public void initView() {
		electriLv = (ListView) electricLayout.findViewById(R.id.fragment_electric_lv);
	}

	private ArrayList<HashMap<String, String>> getData(String[] str) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for(int i=0;i<str.length;i++)  
	    {  
	        HashMap<String, String> map = new HashMap<String, String>();  
	        map.put("ItemTitle", str[i]);  
	        map.put("ItemText", "0");  
	        list.add(map);  
	    }  
		return list;
	}

	
}