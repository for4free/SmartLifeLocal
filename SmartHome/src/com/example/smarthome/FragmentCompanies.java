package com.example.smarthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentCompanies extends Fragment {
	Communicator communicator;
	Button btn_furniture,btn_goout,btn_equipment,btn_pay,btn_bike,btn_car;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView;
		rootView = inflater.inflate(R.layout.fragment_companies, container, false);
		btn_furniture = (Button)rootView.findViewById(R.id.btn_furniture);
		btn_goout = (Button)rootView.findViewById(R.id.btn_goout);
		btn_equipment = (Button)rootView.findViewById(R.id.btn_equipment);
		btn_pay = (Button)rootView.findViewById(R.id.btn_pay);
		btn_bike = (Button)rootView.findViewById(R.id.btn_bike);
		btn_car = (Button)rootView.findViewById(R.id.btn_car);
		
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
