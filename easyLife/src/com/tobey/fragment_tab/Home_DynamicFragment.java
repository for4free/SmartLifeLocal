package com.tobey.fragment_tab;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android_serialport_api.MyApplication;

import com.easemob.chat.EMChatManager;
import com.tobey.bean_huanxin.VideoCallActivity;
import com.tobey.dao_serial.SensorDataGet;
import com.tobey.easyLife.R;
import com.tobey.fragment_dynamicfragment.CommunicationFragment;
import com.tobey.fragment_dynamicfragment.DoorAndWindowFragment;
import com.tobey.fragment_dynamicfragment.PowerFragment;
import com.tobey.fragment_dynamicfragment.ScenesFragment;
import com.tobey.fragment_tab_equipment.EquipmentFragment;

public class Home_DynamicFragment extends Fragment implements View.OnClickListener{

	private String TAG = this.getClass().getName();
	SensorDataGet sensorData;
	/**
	 * 按键布局电器、门铃、照明、监控、通讯、场景
	 */
	private View rl_power;
	private View rl_doorBell;
	private View rl_communication;
	private View rl_illumination;
	private View rl_doorandwindow;
	private View rl_scenes;
	
	
	/**
	 * Fragment布局
	 */
	View homeDynamicLayout;
	
	/**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		homeDynamicLayout = inflater.inflate(R.layout.fragment_home_dynamic,
				container, false);
		MyApplication getSensorData = ((MyApplication)getActivity().getApplication());
        sensorData = getSensorData.getSensorData();
		initView(); 
		fragmentManager = getFragmentManager();
		return homeDynamicLayout;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	
    	setRetainInstance(true);
    }
	private void initView() {
		// TODO Auto-generated method stub
		rl_power = homeDynamicLayout.findViewById(R.id.fragment_home_dynamic_rl_power);
		rl_doorBell = homeDynamicLayout.findViewById(R.id.fragment_home_dynamic_rl_doolBell);
		rl_illumination = homeDynamicLayout.findViewById(R.id.fragment_home_dynamic_rl_equipment);
		rl_doorandwindow = homeDynamicLayout.findViewById(R.id.fragment_home_dynamic_rl_doorAndWindow);
		rl_communication = homeDynamicLayout.findViewById(R.id.fragment_home_dynamic_rl_communication);
		rl_scenes = homeDynamicLayout.findViewById(R.id.fragment_home_dynamic_rl_scenes);
	}
	
	@Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        rl_power.setOnClickListener((android.view.View.OnClickListener) this);
        rl_doorBell.setOnClickListener((android.view.View.OnClickListener)this);  
        rl_illumination.setOnClickListener((android.view.View.OnClickListener)this); 
        rl_doorandwindow.setOnClickListener((android.view.View.OnClickListener)this);
        rl_communication.setOnClickListener((android.view.View.OnClickListener)this); 
        rl_scenes.setOnClickListener((android.view.View.OnClickListener)this);
    }
	//触摸事件
    public void onClick(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int id = v.getId();
        switch(id) {
    	case R.id.fragment_home_dynamic_rl_power:
    		PowerFragment powerLayout = new PowerFragment();
			transaction.replace(R.id.fragment_home_dynamic, powerLayout);
    		// 将当前fragment加入到返回栈中
			transaction.addToBackStack(null);
			break;
    	case R.id.fragment_home_dynamic_rl_doolBell:
    		video();
    		break;
    	case R.id.fragment_home_dynamic_rl_communication:
    		CommunicationFragment comFragment = new CommunicationFragment();
			transaction.replace(R.id.fragment_home_dynamic, comFragment);
    		// 将当前fragment加入到返回栈中
			transaction.addToBackStack(null);
    		break;
    	case R.id.fragment_home_dynamic_rl_equipment:
    	    EquipmentFragment equipmentFragment = new EquipmentFragment();
//    		IlluminationFragment illuminationLayout = new IlluminationFragment();
			transaction.replace(R.id.fragment_home_dynamic, equipmentFragment);
    		// 将当前fragment加入到返回栈中
			transaction.addToBackStack(null);
    		break;
    	case R.id.fragment_home_dynamic_rl_doorAndWindow:
    		DoorAndWindowFragment dwFragment = new DoorAndWindowFragment();
			transaction.replace(R.id.fragment_home_dynamic, dwFragment);
    		// 将当前fragment加入到返回栈中
			transaction.addToBackStack(null);
    		break;
    	case R.id.fragment_home_dynamic_rl_scenes:
    		ScenesFragment scenesFragment = new ScenesFragment();
			transaction.replace(R.id.fragment_home_dynamic, scenesFragment);
    		// 将当前fragment加入到返回栈中
			transaction.addToBackStack(null);
    		break;
    	}    
        transaction.commit();
    }
    
    private void video() {
        if (!EMChatManager.getInstance().isConnected()) {
            Toast.makeText(getActivity(), "未连接到服务器", Toast.LENGTH_SHORT).show();
        }
        else {
            String toUser="smart".toString();
            if (TextUtils.isEmpty(toUser)){
                Toast.makeText(getActivity(), "请填写接受方账号", Toast.LENGTH_SHORT).show();
                return ;
            }
            Intent intent = new Intent(getActivity(), VideoCallActivity.class);
            intent.putExtra("username", toUser);
            intent.putExtra("isComingCall", false);
            startActivity(intent);
        }
    }
	
}