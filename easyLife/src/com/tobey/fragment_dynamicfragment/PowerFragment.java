package com.tobey.fragment_dynamicfragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tobey.easyLife.R;
import com.tobey.fragment_dynamicfragment.Power_Dialog.Power_DialogListener;

public class PowerFragment extends Fragment implements View.OnClickListener {
	
	public static Handler handler;
	private View powerFragment;
	private ListView mListView; //首页的ListView
    private List<Scene> namesList; //用于装载数据的集合
    private int selectPosition = -1;//用于记录用户选择的变量
    private Scene selectPower; //用户选择的电源
    private View addPowerLayout;
	private View returnLayout;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		powerFragment = inflater.inflate(R.layout.fragment_power,
				container, false);
		return powerFragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initDatas();
	}

	private void initView(){
		 
    	mListView = (ListView)powerFragment.findViewById(R.id.fragment_power_myList);
        addPowerLayout = powerFragment.findViewById(R.id.fragment_power_iv_add);
		returnLayout = powerFragment.findViewById(R.id.fragment_power_iv_return);
		addPowerLayout.setOnClickListener((android.view.View.OnClickListener)this);
		returnLayout.setOnClickListener((android.view.View.OnClickListener)this);
	 }

	private void initDatas(){
        //初始化ListView适配器的数据
        namesList = new ArrayList<>();
        Scene scene0 = new Scene("总开关");
        Scene scene1 = new Scene("照明");
        Scene scene2 = new Scene("卧室客厅插座");
        Scene scene3 = new Scene("厨房插座");
        Scene scene4 = new Scene("卫生间插座");
        namesList.add(scene0);
        namesList.add(scene1);
        namesList.add(scene2);
        namesList.add(scene3);
        namesList.add(scene4);
        final MyAdapter myAdapter = new MyAdapter(getActivity(),namesList);
        mListView.setAdapter(myAdapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //获取选中的参数
//                selectPosition = position;
//                myAdapter.notifyDataSetChanged();
//                selectPower = namesList.get(position);
//                Toast.makeText(getActivity(),"设置模式 ："+selectPower.getScenename(),Toast.LENGTH_SHORT).show();
//            }
//        });
    }

	public class MyAdapter extends BaseAdapter{
	    Context context;
	    List<Scene> scenesList;
	    LayoutInflater mInflater;
	    public MyAdapter(Context context,List<Scene> mList){
	        this.context = context;
	        this.scenesList = mList;
	        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	    @Override
	    public int getCount() {
	        return scenesList.size();
	    }
	
	    @Override
	    public Object getItem(int position) {
	        return position;
	    }
	
	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	
	        ViewHolder viewHolder = null;
	        if(convertView == null){
	            convertView = mInflater.inflate(R.layout.item_power,parent,false);
	            viewHolder = new ViewHolder();
	            viewHolder.name = (TextView)convertView.findViewById(R.id.fragment_power_tv_name);
	            convertView.setTag(viewHolder);
	        }else{
	            viewHolder = (ViewHolder)convertView.getTag();
	        }
	        final String childName = scenesList.get(position).getScenename();
	        viewHolder.name.setText(scenesList.get(position).getScenename());
	        convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Power_Dialog dw_dialog = new Power_Dialog(childName, "1", getActivity(),  
							R.style.Dialog, new Power_DialogListener(){
						
						@Override
						public void onClick(View view) {
							int check = view.getId();
							String status = new String();
							switch(check) {
							case R.id.dialog_power_bt_open:
								status = "open";
								break;
							case R.id.dialog_power_bt_close:
								status = "close";
								break;
							default:
								break;
							}
							Message messageDw = handler.obtainMessage();
							messageDw.what = 1;
							messageDw.getData().putString("status", status);
							handler.sendMessage(messageDw);
						}
						
					});
					dw_dialog.show();
					disBackGround(dw_dialog);
				}
	        });
	        return convertView;
	    }
	    
	    private void disBackGround(Dialog dialog) {
			// TODO Auto-generated method stub
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		    lp.dimAmount = 0.55f;
		    dialog.getWindow().setAttributes(lp);
		    dialog.getWindow()
		            .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		}
	}
	
	
	public class ViewHolder{
	    TextView name;
	}
	
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch(id) {
		case R.id.fragment_power_iv_return:
			getFragmentManager().popBackStack();
			break;
		default:
			break;
		}
		
	}
}
