package com.tobey.fragment_dynamicfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android_serialport_api.MyApplication;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tobey.bean_HTCP.SetData;
import com.tobey.bean_scenes.ModelData;
import com.tobey.bean_scenes.SceneData;
import com.tobey.easyLife.R;

public class ScenesFragment extends Fragment implements View.OnClickListener {
	
	private View scenesFragment;
	private ListView mListView; //首页的ListView
    private List<Map<String, String>> namesList = new ArrayList(); //用于装载数据的集合
    private String selectPosition = "-1";//用于记录用户选择的变量
    private String selectScene; //用户选择的场景
    private View addSceneLayout;
	private View returnLayout;
	private String path_getScene;
	private String path_setScene;
	private SceneData sceneDatatFromScene;
	private List<ModelData> modelDatatFromScene = new ArrayList<ModelData>();
	private MyAdapter myAdapter;
	private int status;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		scenesFragment = inflater.inflate(R.layout.fragment_scenes,
				container, false);
		MyApplication myApplication = ((MyApplication)getActivity().getApplication());
        // 获取串口及数据库管理对象
		path_getScene = myApplication.getPath_Scenes();
		path_setScene = myApplication.getPath_changeScenes();
		return scenesFragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initDatas();
	}
	
	 private void initView(){
	        mListView = (ListView)scenesFragment.findViewById(R.id.fragment_scenes_myList);
	        addSceneLayout = scenesFragment.findViewById(R.id.fragment_scenes_iv_add);
			returnLayout = scenesFragment.findViewById(R.id.fragment_scenes_iv_return);
			addSceneLayout.setOnClickListener((android.view.View.OnClickListener)this);
			returnLayout.setOnClickListener((android.view.View.OnClickListener)this);
	 }

    private void initDatas(){
    		getScene(path_getScene);
//    		sceneDatatFromScene = operator.getSceneDatatFromScene();
//    		modelDatatFromScene = operator.getModelDatatFromScene();
    		
	        //初始化ListView适配器的数据

	        myAdapter = new MyAdapter(getActivity(),namesList);
	        mListView.setAdapter(myAdapter);
	        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                //获取选中的参数
	                selectPosition = namesList.get(position).get("id");
	                String path = path_setScene + namesList.get(position).get("id");
	                changeScene(path);
	                myAdapter.notifyDataSetChanged();
	                selectScene = namesList.get(position).get("name");
	                Toast.makeText(getActivity(),"设置模式 ："+selectScene,Toast.LENGTH_SHORT).show();
	            }
	        });
	    }
	    public void getScene(String path){
	    	HttpUtils utils = new HttpUtils();
	    	utils.configCurrentHttpCacheExpiry(100);
	    	utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {
	
	    		public void onFailure(HttpException arg0, String arg1) {
	//    			status = 0;
				}
				
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					//加载数据  转化为JSON格式
					try {
						sceneDatatFromScene = (SceneData) JSONObject.parseObject(arg0.result, SceneData.class);
						//根据字段用get方法获取字段内容
						if(!sceneDatatFromScene.equals(null)) {
							modelDatatFromScene = sceneDatatFromScene.getData();
							try {
								Iterator<ModelData> it = modelDatatFromScene.iterator();
								ModelData data = new ModelData();
								while(it.hasNext()) {
									data = it.next();
									Map<String,String> map = new HashMap<String,String>();
									map.put("id", data.getMid());
					        		map.put("name", data.getMname());
					        		namesList.add(map);
								}
								selectPosition = sceneDatatFromScene.getMODELID();
								myAdapter.notifyDataSetChanged();
							} catch (Exception e) {
								// TODO: handle exception
							}
						} else {
							Log.e("ScenesFragment","Operator2Scenes getScene result is empty ");
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
	//				status = 1;
					
				}
				@Override
				public void onFailure(
						com.lidroid.xutils.exception.HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
	//				status = 0;
				}
				
			});
	//    	return status;
	    }
	    
	    public void changeScene(String path) {
	    	Log.e("scene",path);
			HttpUtils utils = new HttpUtils();
			utils.configCurrentHttpCacheExpiry(100);
	    	utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

				@Override
				public void onFailure(
						com.lidroid.xutils.exception.HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
//					status = 0;
					Log.e("scene","changed scene failed");
					
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					try {
						SetData setData = (SetData) JSONObject.parseObject(arg0.result, SetData.class);
						status = Integer.parseInt(setData.getStatus());
						Log.e("scene","changed scene success");
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
//				public void onFailure(HttpException arg0, String arg1) {
//	    			status = 0;
//				}
	    		
	    	});
	    	
	    	//return status;
		}

	    public class MyAdapter extends BaseAdapter{
	        Context context;
	        private List<Map<String, String>> scenesList;
	        LayoutInflater mInflater;
	        public MyAdapter(Context context,List<Map<String, String>> mList){
	            this.context = context;
	            this.scenesList = mList;
	            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        }
	        @Override
	        public int getCount() {
	        	if( scenesList.equals("null") ) {
	        		return 0;
	        	}else {
	        		return scenesList.size();
	        	}
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
	                convertView = mInflater.inflate(R.layout.item_scene,parent,false);
	                viewHolder = new ViewHolder();
	                viewHolder.name = (TextView)convertView.findViewById(R.id.fragment_scenes_name);
	                viewHolder.select = (RadioButton)convertView.findViewById(R.id.fragment_scenes_select);
	                convertView.setTag(viewHolder);
	            }else{
	                viewHolder = (ViewHolder)convertView.getTag();
	            }
	            viewHolder.name.setText(scenesList.get(position).get("name"));
	            
	            if(selectPosition.equals(scenesList.get(position).get("id"))){
	                viewHolder.select.setChecked(true);
	            }
	            else{
	                viewHolder.select.setChecked(false);
	            }
	            return convertView;
	        }
	    }

	    public class ViewHolder{
	        TextView name;
	        RadioButton select;
	    }

		@Override
		public void onClick(View view) {
			int id = view.getId();
			switch(id) {
			case R.id.fragment_scenes_iv_return:
				getFragmentManager().popBackStack();
				break;
			case R.id.fragment_scenes_iv_add:
				Toast.makeText(getActivity(),"添加新场景!",Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			
		}
}
