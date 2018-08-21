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
	 * Fragment����
	 */
	private View homeLayout;
	/**
     * ���ڶ�Fragment���й���
     */
    private FragmentManager fragmentManager;
    
    /**
     * �����桢�豸�����õ�Fragment����
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
       //��һ������ʱ��ʼ������
//         setTabSelection();
        return homeLayout;
    }
    /*private void setTabSelection() {
        // ����һ��Fragment����
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        hideFragments(transaction);
        if (homeStaticFragment == null) {
            // ���HomeFragmentΪ�գ��򴴽�һ������ӵ�������
            homeStaticFragment = new Home_StaticFragment();
            transaction.add(R.id.fragment_home_static, homeStaticFragment);
        } else {
            // ���HomeFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
            transaction.show(homeStaticFragment);
        }
        
        if (homeDynamicFragment == null) {
            // ���HomeFragmentΪ�գ��򴴽�һ������ӵ�������
            homeDynamicFragment = new Home_DynamicFragment();
            transaction.add(R.id.fragment_home_dynamic, homeDynamicFragment);
        } else {
            // ���HomeFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
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