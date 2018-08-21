package com.tobey.fragment_tab;

import com.tobey.easyLife.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class EntertainmentFragment extends Fragment implements View.OnClickListener{

	private View entertainmentLayout;
	private View movieLayout;
	private View ktvLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		entertainmentLayout = inflater.inflate(R.layout.fragment_entertainment,
                container, false);
		return entertainmentLayout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}

	private void initViews() {

		movieLayout = entertainmentLayout.findViewById(R.id.fragment_entertainment_rl_movie);
		ktvLayout = entertainmentLayout.findViewById(R.id.fragment_entertainment_rl_ktv);
		movieLayout.setOnClickListener((OnClickListener) this);
		ktvLayout.setOnClickListener((OnClickListener) this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.fragment_entertainment_rl_movie:
			Toast.makeText(getActivity(), "开启电影旅程", Toast.LENGTH_SHORT).show();
			break;
		case R.id.fragment_entertainment_rl_ktv:
			Toast.makeText(getActivity(), "进入KTV狂欢模式", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		
	}
}
