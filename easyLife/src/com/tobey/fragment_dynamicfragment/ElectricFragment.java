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
	 * 碎片布局
	 */
	private View electricLayout;
	private Button bt_electric;
	
	/**
	* @描述 在Fragment中要使用ListView，就要用ListFragment
	* */
	private String TAG = this.getClass().getName();  
	private SimpleAdapter adapter; 
	private ArrayList<HashMap<String, String>> mylist;//listview中显示的内容

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
		//生成动态数组，并且转载数据  
	    mylist = new ArrayList<HashMap<String, String>>();  
	    //初始化设备数量
	    String[] list = {"温湿度光照传感器","电机"};  
	    mylist = getData(list);
	    
	    //生成适配器，数组===》ListItem  
	    adapter = new SimpleAdapter(getActivity(),
	    		mylist,//数据来源   
	    		R.layout.equipment_listitem, //ListItem的XML实现  
	    		//动态数组与ListItem对应的子项          
                new String[] {"ItemTitle", "ItemText"},   
	    		new int[]{R.id.euipment_tv_ItemTitle,R.id.equipment_tv_ItemText});  
	    //添加并且显示  
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