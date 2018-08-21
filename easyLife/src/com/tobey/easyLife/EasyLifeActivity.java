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
 * serialport api��jniȡ��http://code.google.com/p/android-serialport-api/
 */

public class EasyLifeActivity extends Activity implements View.OnClickListener {

	/**
	 * TAG
	 */
    /**
     * �����桢�豸�����õ�Fragment����
     */
    private HomeFragment homeFragment;
    private EntertainmentFragment entertainmentFragment;
    private EtcFragment etcFragment;
    private Home_StaticFragment homeStaticFragment;;
    private Home_DynamicFragment homeDynamicFragment;

    /**
     * ���沼��
     */
    private View homeLayout;
    private View equipmentLayout;
    private View settingLayout;

    /**
     * Tab������ͼ��Ŀؼ�
     */
    private ImageView homeImage;
    private ImageView equipmentImage;
    private ImageView settingImage;

    /**
     * Tab�����ϱ���Ŀؼ�
     */
    private TextView homeText;
    private TextView equipmentText;
    private TextView settingText;

    /**
     * ���ڶ�Fragment���й���
     */
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        setContentView(R.layout.activity_main);
        //��ʼ������Ԫ��
        initViews();
        fragmentManager = getFragmentManager();
//        if(savedInstanceState != null) {
//    		homeFragment = (HomeFragment) fragmentManager.findFragmentByTag("homeFragment");
//    		entertainmentFragment = (EntertainmentFragment) fragmentManager.findFragmentByTag("equipmentFragment");
//    		settingFragment = (SettingFragment) fragmentManager.findFragmentByTag("settingFragment");
//    		homeStaticFragment = (Home_StaticFragment) fragmentManager.findFragmentByTag("homeStaticFragment");
//    		homeDynamicFragment = (Home_DynamicFragment) fragmentManager.findFragmentByTag("homeDynamicFragment");
//        }
        //��һ������ʱѡ�е�0��tab
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // �������е�Fragment����ֹ���Fragmentͬʱ��ʾ�ڽ����ϵ����
        hideFragments(transaction);
        setTabSelection(0);
        super.onCreate(savedInstanceState);

    }

    /**
     * �������ȡ��ÿ����Ҫ�õ��Ŀؼ���ʵ���������������úñ�Ҫ�ĵ���¼���
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
                // ���������Ϣtabʱ��ѡ�е�1��tab
                setTabSelection(0);
                break;
            case R.id.activity_main_entertainment_layout:
                // ���������ϵ��tabʱ��ѡ�е�2��tab
                setTabSelection(1);
                break;
            case R.id.activity_main_setting_layout:
                // �����������tabʱ��ѡ�е�4��tab
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
     * ���ݴ����index����������ѡ�е�tabҳ��
     *
     * @param index
     * ÿ��tabҳ��Ӧ���±ꡣ0��ʾ��ҳ��1��ʾ�豸��2��ʾ���á�
     */
    private void setTabSelection(int index) {
        // ������ϴε�ѡ��״̬
        clearSelection();
        // ����һ��Fragment����
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // �������е�Fragment����ֹ���Fragmentͬʱ��ʾ�ڽ����ϵ����
        hideFragments(transaction);
        switch (index) {
            case 0:
            	homeImage.setImageResource(R.drawable.home_selected);
                homeText.setTextColor(Color.WHITE);
                if (homeFragment == null) {
                    // ���HomeFragmentΪ�գ��򴴽�һ������ӵ�������
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment);
                } else {
                    // ���HomeFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
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
                // ��������豸tabʱ���ı�ؼ���ͼƬ��������ɫ
                equipmentImage.setImageResource(R.drawable.ic_entertainment);
                equipmentText.setTextColor(Color.WHITE);
                if (entertainmentFragment == null) {
                    // ���EntertainmentFragmentΪ�գ��򴴽�һ������ӵ�������
                    entertainmentFragment = new EntertainmentFragment();
                    transaction.add(R.id.content, entertainmentFragment);
                } else {
                    // ���EntertainmentFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
                    transaction.show(entertainmentFragment);
                }
                break;
            case 2:
            	// �����������tabʱ���ı�ؼ���ͼƬ��������ɫ
                settingImage.setImageResource(R.drawable.ic_etc);
                settingText.setTextColor(Color.WHITE);
                if (etcFragment == null) {
                    // ���SettingFragmentΪ�գ��򴴽�һ������ӵ�������
                    etcFragment = new EtcFragment();
                    transaction.add(R.id.content, etcFragment);
                } else {
                    // ���SettingFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
                    transaction.show(etcFragment);
                }
                break;
            default:
            	break;
        }
        transaction.commit();
    }

    /**
     * ��������е�ѡ��״̬
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
     * �����е�Fragment����Ϊ����״̬��
     *
     * @param transaction
     * ���ڶ�Fragmentִ�в���������
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
            Toast.makeText(getApplicationContext(),"�˺Ż����벻��Ϊ�գ�",Toast.LENGTH_LONG).show();
            return ;
        }
        //�����Ƚ����Լ��������ĵ�¼����
        //�Լ���������¼�ɹ�����ִ�л��ŷ������ĵ�¼����
        EMChatManager.getInstance().login(u, p, new EMCallBack() {//�ص�
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        Toast.makeText(getBaseContext(), "��½����������ɹ�", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e("TAG", "��½����������� " + "progress:" + progress + " status:" + status);
            }

            @Override
            public void onError(int code, String message) {
                Log.e("TAG", "��½���������ʧ�ܣ�"+code+"=="+message+"�û���"+u+"���룺"+p);
                //Toast.makeText(MainActivity.this, "��½���������ʧ��"+code+"=="+message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
