package com.tobey.easyLife;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.tobey.fragment_tab.EntertainmentFragment;
import com.tobey.fragment_tab.HomeFragment;
import com.tobey.fragment_tab.Home_DynamicFragment;
import com.tobey.fragment_tab.Home_StaticFragment;
import com.tobey.fragment_tab_setting.EtcFragment;

/**
 * serialport api和jni取自http://code.google.com/p/android-serialport-api/
 */

public class EasyLifeActivity extends Activity implements View.OnClickListener {

	/**
	 * TAG
	 */
    /**
     * 主界面、设备、设置的Fragment界面
     */
    private HomeFragment homeFragment;
    private EntertainmentFragment entertainmentFragment;
    private EtcFragment etcFragment;
    private Home_StaticFragment homeStaticFragment;;
    private Home_DynamicFragment homeDynamicFragment;

    /**
     * 界面布局
     */
    private View homeLayout;
    private View equipmentLayout;
    private View settingLayout;

    /**
     * Tab布局上图标的控件
     */
    private ImageView homeImage;
    private ImageView equipmentImage;
    private ImageView settingImage;

    /**
     * Tab布局上标题的控件
     */
    private TextView homeText;
    private TextView equipmentText;
    private TextView settingText;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        setContentView(R.layout.activity_main);
        //初始化布局元素
        initViews();
        fragmentManager = getFragmentManager();
//        if(savedInstanceState != null) {
//    		homeFragment = (HomeFragment) fragmentManager.findFragmentByTag("homeFragment");
//    		entertainmentFragment = (EntertainmentFragment) fragmentManager.findFragmentByTag("equipmentFragment");
//    		settingFragment = (SettingFragment) fragmentManager.findFragmentByTag("settingFragment");
//    		homeStaticFragment = (Home_StaticFragment) fragmentManager.findFragmentByTag("homeStaticFragment");
//    		homeDynamicFragment = (Home_DynamicFragment) fragmentManager.findFragmentByTag("homeDynamicFragment");
//        }
        //第一次启动时选中第0个tab
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有的Fragment，防止多个Fragment同时显示在界面上的情况
        hideFragments(transaction);
        setTabSelection(0);
        super.onCreate(savedInstanceState);

    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
    	login();
        homeLayout = findViewById(R.id.activity_main_home_layout);
        equipmentLayout = findViewById(R.id.activity_main_entertainment_layout);
        settingLayout = findViewById(R.id.activity_main_setting_layout);
        homeImage = (ImageView) findViewById(R.id.activity_main_home_image);
        equipmentImage = (ImageView) findViewById(R.id.activity_main_entertainment_image);
        settingImage = (ImageView) findViewById(R.id.activity_main_setting_image);
        homeText = (TextView) findViewById(R.id.activity_main_home_text);
        equipmentText = (TextView) findViewById(R.id.activity_main_entertainment_text);
        settingText = (TextView) findViewById(R.id.activity_main_setting_text);
        homeLayout.setOnClickListener(this);
        equipmentLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_home_layout:
                // 当点击了消息tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.activity_main_entertainment_layout:
                // 当点击了联系人tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.activity_main_setting_layout:
                // 当点击了设置tab时，选中第4个tab
                setTabSelection(2);
                break;
            default:
                break;
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
//    	if(homeFragment != null) {
//    		getFragmentManager().putFragment(outState, "homeFragment", homeFragment);
//    	}
//    	if(equipmentFragment != null) {
//    		getFragmentManager().putFragment(outState, "equipmentFragment", equipmentFragment);
//    	}
//    	if(settingFragment != null) {
//    		getFragmentManager().putFragment(outState, "settingFragment", settingFragment);
//    	}
//    	if(homeStaticFragment != null) {
//    		getFragmentManager().putFragment(outState, "homeStaticFragment", homeStaticFragment);
//    	}
//    	if(homeDynamicFragment != null) {
//    		getFragmentManager().putFragment(outState, "homeDynamicFragment", homeDynamicFragment);
//    	}
    	//super.onSaveInstanceState(outState);
    }
    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     * 每个tab页对应的下标。0表示主页，1表示设备，2表示设置。
     */
    private void setTabSelection(int index) {
        // 清除掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有的Fragment，防止多个Fragment同时显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
            	homeImage.setImageResource(R.drawable.home_selected);
                homeText.setTextColor(Color.WHITE);
                if (homeFragment == null) {
                    // 如果HomeFragment为空，则创建一个并添加到界面上
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment);
                } else {
                    // 如果HomeFragment不为空，则直接将它显示出来
                    transaction.show(homeFragment);
                }
                if( homeStaticFragment == null ) {
                	homeStaticFragment = new Home_StaticFragment();
                    transaction.add(R.id.fragment_home_static, homeStaticFragment);
                } else {
                	transaction.show(homeStaticFragment);
                }
                if (homeDynamicFragment == null) {
                	homeDynamicFragment = new Home_DynamicFragment();
                    transaction.add(R.id.fragment_home_dynamic, homeDynamicFragment);
                } else {
            		homeDynamicFragment.getFragmentManager().popBackStack();
            		transaction.show(homeDynamicFragment);
                	
                }
                break;
            case 1:
                // 当点击了设备tab时，改变控件的图片和文字颜色
                equipmentImage.setImageResource(R.drawable.ic_entertainment);
                equipmentText.setTextColor(Color.WHITE);
                if (entertainmentFragment == null) {
                    // 如果EntertainmentFragment为空，则创建一个并添加到界面上
                    entertainmentFragment = new EntertainmentFragment();
                    transaction.add(R.id.content, entertainmentFragment);
                } else {
                    // 如果EntertainmentFragment不为空，则直接将它显示出来
                    transaction.show(entertainmentFragment);
                }
                break;
            case 2:
            	// 当点击了设置tab时，改变控件的图片和文字颜色
                settingImage.setImageResource(R.drawable.ic_etc);
                settingText.setTextColor(Color.WHITE);
                if (etcFragment == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    etcFragment = new EtcFragment();
                    transaction.add(R.id.content, etcFragment);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(etcFragment);
                }
                break;
            default:
            	break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态
     */
    private void clearSelection() {
        homeImage.setImageResource(R.drawable.home_unselected);
        homeText.setTextColor(Color.parseColor("#82858b"));
        equipmentImage.setImageResource(R.drawable.ic_entertainment);
        equipmentText.setTextColor(Color.parseColor("#82858b"));
        settingImage.setImageResource(R.drawable.ic_etc);
        settingText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     * 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
    	if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (homeStaticFragment != null) {
            transaction.hide(homeStaticFragment);
        }
        if (homeDynamicFragment != null) {
            transaction.hide(homeDynamicFragment);
        }
        if (entertainmentFragment != null) {
            transaction.hide(entertainmentFragment);
        }
        if (etcFragment != null) {
            transaction.hide(etcFragment);
        }
    }
    private void login() {
        final String u="abc1234".toString();
        final String p="abc1234home".toString();
        if (TextUtils.isEmpty(u)||TextUtils.isEmpty(p)){
            Toast.makeText(getApplicationContext(),"账号或密码不能为空！",Toast.LENGTH_LONG).show();
            return ;
        }
        //这里先进行自己服务器的登录操作
        //自己服务器登录成功后再执行环信服务器的登录操作
        EMChatManager.getInstance().login(u, p, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        Toast.makeText(getBaseContext(), "登陆聊天服务器成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e("TAG", "登陆聊天服务器中 " + "progress:" + progress + " status:" + status);
            }

            @Override
            public void onError(int code, String message) {
                Log.e("TAG", "登陆聊天服务器失败！"+code+"=="+message+"用户名"+u+"密码："+p);
                //Toast.makeText(MainActivity.this, "登陆聊天服务器失败"+code+"=="+message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
