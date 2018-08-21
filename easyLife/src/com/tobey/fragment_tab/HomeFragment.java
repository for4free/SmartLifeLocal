package com.tobey.fragment_tab;



import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tobey.easyLife.R;

public class HomeFragment extends Fragment{

	/**
	 * TAG
	 */
	private String TAG = this.getClass().getName();
	
	/**
	 * Fragment布局
	 */
	private View homeLayout;
	/**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    
    /**
     * 主界面、设备、设置的Fragment界面
     */
    private Home_StaticFragment homeStaticFragment;;
    private Home_DynamicFragment homeDynamicFragment;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.e("HomeFragment","onCreateView");
    	if(homeLayout != null) {
    		ViewGroup parent = (ViewGroup) homeLayout.getParent();
    		if(parent != null)
    			parent.removeView(homeLayout);
    	}
    	try {
    		homeLayout = inflater.inflate(R.layout.fragment_home,
                container, false);
    	} catch (InflateException e) {
    		/* homeStaticLayout is already there, just return view as it is */
    	}
         
//         fragmentManager = getFragmentManager();
       //第一次启动时初始化界面
//         setTabSelection();
        return homeLayout;
    }
    /*private void setTabSelection() {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        hideFragments(transaction);
        if (homeStaticFragment == null) {
            // 如果HomeFragment为空，则创建一个并添加到界面上
            homeStaticFragment = new Home_StaticFragment();
            transaction.add(R.id.fragment_home_static, homeStaticFragment);
        } else {
            // 如果HomeFragment不为空，则直接将它显示出来
            transaction.show(homeStaticFragment);
        }
        
        if (homeDynamicFragment == null) {
            // 如果HomeFragment为空，则创建一个并添加到界面上
            homeDynamicFragment = new Home_DynamicFragment();
            transaction.add(R.id.fragment_home_dynamic, homeDynamicFragment);
        } else {
            // 如果HomeFragment不为空，则直接将它显示出来
            transaction.show(homeDynamicFragment);
        }
        transaction.commit();
    }*/
    
//    private void hideFragments(FragmentTransaction transaction) {
//        
//    }
    /*@Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	fragmentManager = getFragmentManager();
    	if(savedInstanceState != null) {
    		homeStaticFragment = (Home_StaticFragment) fragmentManager.findFragmentByTag("homeStaticFragment");
    		homeDynamicFragment = (Home_DynamicFragment) fragmentManager.findFragmentByTag("homeDynamicFragment");
    	}
    	super.onCreate(savedInstanceState);
    }*/
   

}