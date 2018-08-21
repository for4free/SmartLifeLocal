package com.tobey.fragment_tab_setting;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android_serialport_api.MyApplication;

import com.tobey.bean_family.Family;
import com.tobey.easyLife.R;


public class EtcFragment extends Fragment implements OnClickListener {

	//fragment
	private View etcLayout;

	private TextView tv_familyMember;
	private TextView tv_family_show;
	private Family resultFromFamily = new Family();
	
	public static Handler handler = new Handler();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		etcLayout = inflater.inflate(R.layout.fragment_etc,
				container, false);
		MyApplication myApplication = ((MyApplication)getActivity().getApplication());
		resultFromFamily = myApplication.getResultFromFamily();
		initView();
		return etcLayout;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		try {
			String name_str = data.getExtras().getString("name");
			tv_family_show.setText(name_str);
			if(name_str.equals("您还未加入家庭")) {
				Toast.makeText(this.getActivity(), "家庭不存在！", Toast.LENGTH_SHORT).show();
			}
			if(name_str.equals("请检查网络连接")) {
				Toast.makeText(this.getActivity(), "请检查网络连接！", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			tv_family_show.setText("请检查网络");
		}
		
	}
	
	private void initView() {
		tv_familyMember = (TextView) etcLayout.findViewById(R.id.fragment_etc_tv_famaly_member);
		tv_family_show = (TextView) etcLayout.findViewById(R.id.fragment_etc_tv_family_show);
		tv_familyMember.setOnClickListener(this);
		try{
			if(resultFromFamily.getStatus().equals("403")) {
				tv_family_show.setText(resultFromFamily.getFname());
			} else {
				tv_family_show.setText("您还未加入家庭！");
			}
		} catch(Exception e) {
			tv_family_show.setText("请检查网络连接！");
		}
//		if(!resultFromFamily.equals("")){
//			if(resultFromFamily.getStatus().equals("403")) {
//				tv_family_show.setText(resultFromFamily.getFname());
//			} else {
//				tv_family_show.setText("您还未加入家庭！");
//			}
//		} else {
//			tv_family_show.setText("请检查网络连接！");
//		}
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch(id) {
		case R.id.fragment_etc_tv_famaly_member:
//			if ( tv_family_show.getText().equals("您还未加入家庭！") ||  tv_family_show.getText().equals("请检查网络连接！")) {
//				Intent intent = new Intent(getActivity(),JoinFamilyActivity.class);
//			}
			Intent intent = new Intent(getActivity(),JoinFamilyActivity.class);
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}
	
	

	/** --显示消息
     * @param sMsg
     */
    private void showMessage(String sMsg)
    {
        Toast.makeText(getActivity(), sMsg, Toast.LENGTH_SHORT).show();
    }
    
/*    public void joinFamilyStatus(String path) {
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(100);
    	utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(
					com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					resultFromFamily = (Family) JSONObject.parseObject(arg0.result, Family.class);
					Log.e("JoinFamily","status: " + resultFromFamily.getStatus());
//					if(resultFromFamily.equals(null)) {
//						Toast.makeText(getActivity(), "请加入家庭!", Toast.LENGTH_SHORT).show();
//					}
					if(!resultFromFamily.equals(null)){
						if(resultFromFamily.getStatus().equals("403")) {
							tv_family_show.setText(resultFromFamily.getFname());
						} else {
							tv_family_show.setText("您还未加入家庭！");
						}
					} else {
						tv_family_show.setText("请检查网络连接！");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
    		
    	});
    }*/

}
