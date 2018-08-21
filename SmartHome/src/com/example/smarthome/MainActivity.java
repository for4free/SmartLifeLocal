package com.example.smarthome;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements OnClickListener{
	
	//Fragment定义
	private FragmentMessages currentMessage;
	private FragmentTrajectory currentTrajectory;
	private FragmentCompanies currentCompanies;
	private FragmentVirtual currentVirtual;
	private FragmentHome currentHome;
	private Fragment[] fragments;
	//Button的相关定义
	private Button[] mTabsButtons;
	private int index;
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		currentMessage = new FragmentMessages();
		currentTrajectory = new FragmentTrajectory();
		currentCompanies = new FragmentCompanies();
		currentVirtual = new FragmentVirtual();
		currentHome = new FragmentHome();	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
		fragments = new Fragment[] { currentMessage, currentTrajectory, currentCompanies, currentVirtual,currentHome };
		FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
		trx.replace(R.id.fragment_container, new FragmentCompanies());
		//trx.replace(R.id.fragment_container, new FragmentHome());
		trx.commit();
		index = 2;
		mTabsButtons[currentIndex].setSelected(false);
		currentIndex = index;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/*初始化组件*/
	private void initView(){
		mTabsButtons = new Button[5];
		mTabsButtons[0] = (Button)findViewById(R.id.btn_message);
		mTabsButtons[1] = (Button)findViewById(R.id.btn_trajectory);
		mTabsButtons[2] = (Button)findViewById(R.id.btn_companies);
		mTabsButtons[3] = (Button)findViewById(R.id.btn_virtual);
		mTabsButtons[4] = (Button)findViewById(R.id.btn_home);
		//把中间的组件设定为选中状态
		mTabsButtons[2].setSelected(true);
	}
	
	private void onTabClicked(View view){
		
		}
	/*点击事件，功能选择*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_message:
			index = 0;
			
			break;
		case R.id.btn_trajectory:
			index = 1;
			break;
		case R.id.btn_companies:
			index = 2;
			break;
		case R.id.btn_virtual:
			index = 3;
			break;
		case R.id.btn_home:
			index = 4;
			
			break;
		default:
			break;
	    }
		if(currentIndex!=index){
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.replace(R.id.fragment_container, fragments[index]);
			trx.commit();
		}
		mTabsButtons[currentIndex].setSelected(false);
		mTabsButtons[index].setSelected(true);
		currentIndex = index;
	}
}
