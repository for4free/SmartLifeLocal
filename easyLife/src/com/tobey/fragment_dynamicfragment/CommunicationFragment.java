package com.tobey.fragment_dynamicfragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tobey.easyLife.R;

public class CommunicationFragment extends Fragment implements View.OnClickListener {
	
	private View communicationLayout;
	private CommunicationExpandableListView expandableList;
	
	private CommunicationAdapter adapter;
	private CommunicationExpandableListAdapter expandableAdapter;
	
	private View returnLayout;
	private View addLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		communicationLayout = inflater.inflate(R.layout.fragment_communication,
				container, false);
		return communicationLayout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		expandableList = (CommunicationExpandableListView) communicationLayout.findViewById(R.id.fragment_communication_expandablelist);
		InitData();
		adapter = new CommunicationAdapter(getActivity());
		
		expandableAdapter = new CommunicationExpandableListAdapter(adapter,getActivity());
		expandableList.setAdapter(expandableAdapter);
	}

	private void InitData() {
		returnLayout = communicationLayout.findViewById(R.id.fragment_communication_rl_return);
		returnLayout.setOnClickListener((android.view.View.OnClickListener)this);
		addLayout = communicationLayout.findViewById(R.id.fragment_communication_rl_add);
		addLayout.setOnClickListener((android.view.View.OnClickListener)this);
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch(id) {
		case R.id.fragment_communication_rl_return:
			getFragmentManager().popBackStack();
			break;
		case R.id.fragment_communication_rl_add:
			Toast.makeText(getActivity(), "添加联系人", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		
	}
}
