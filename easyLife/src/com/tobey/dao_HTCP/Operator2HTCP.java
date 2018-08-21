package com.tobey.dao_HTCP;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tobey.bean_HTCP.GetData;
import com.tobey.bean_HTCP.ResultHTCP;
import com.tobey.bean_HTCP.SetData;

public class Operator2HTCP {

	private Context context;
	private String operator;
	private int status = 9;
	private GetData getDataFromHTCP;
	private List<ResultHTCP> resultFromHTCP = new ArrayList<ResultHTCP>();
	
	public Operator2HTCP(Context context, String operator) {
		this.context = context;
		this.operator = operator;
	}
	
	/**
     * 加载数据数据
     * @param path
     */
    public int getHTCP(String path){
    	HttpUtils utils = new HttpUtils();
    	utils.configCurrentHttpCacheExpiry(100);
    	utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

    		public void onFailure(HttpException arg0, String arg1) {
    			status = 0;
			}
			
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				//加载数据  转化为JSON格式
				try {
					getDataFromHTCP = (GetData) JSONObject.parseObject(arg0.result, GetData.class);
					//根据字段用get方法获取字段内容
					if(!getDataFromHTCP.equals(null)) {
						resultFromHTCP = getDataFromHTCP.getResult();
					}
					if(resultFromHTCP.isEmpty()) {
						Log.e("Operator2HTCP","getHTCP result is empty ");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				status = 1;
				
			}

			@Override
			public void onFailure(
					com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				status = 0;
			}
		});
    	return status;
    }
    
    public List<ResultHTCP> getResultFromHTCP() {
    	return resultFromHTCP;
    }
    
    /**
	 * 向服务器发送数据
	 * @param path：地址+需要上传的数据
	 */
	public void dealHTCP(String path) {
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(100);
    	utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(
					com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				status = 0;
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					SetData setData = (SetData) JSONObject.parseObject(arg0.result, SetData.class);
					status = Integer.parseInt(setData.getStatus());
					switch(operator) {
					case "Home_StaticFragment":
						showHome_StaticFragmentMessage(status);
						break;
					case "AddEquipmentActivity":
						showAddEquipmentMessage(status);
						break;
					case "EuipmentExpandableListAdapter":
						showEuipmentExpandableListAdapterMessage(status);
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
//			public void onFailure(HttpException arg0, String arg1) {
//    			status = 0;
//			}
    		
    	});
    	
    	//return status;
	}
	
	public int getStatus() {
		return status;
	}
	
	private void showMessage(String sMsg)
    {
        Toast.makeText(context, sMsg, Toast.LENGTH_SHORT).show();
    }
	
	private void showAddEquipmentMessage(int status) {
		switch(status) {
		case 403:
			showMessage("添加设备成功");
			break;
		case 402:
			showMessage("设备已存在");
			break;
		case 401:
			showMessage("设备信息有误！请核对信息！");
			break;
		case 0:
			showMessage("请检查网络是否正常连接！");
			break;
		default:
			break;
		}
	}
	
	private void showHome_StaticFragmentMessage(int status) {
		switch(status) {
		case 0:
			showMessage("请检查网络是否正常连接！");
			break;
		default:
			break;
		}
	}
	private void showEuipmentExpandableListAdapterMessage(int status) {
		switch(status) {
		case 403:
			showMessage("操作成功！");
			break;
		case 402:
			showMessage("操作成功！");
			break;
		case 401:
			showMessage("传入空值！");
			break;
		case 0:
			showMessage("请检查网络是否正常连接！");
			break;
		default:
			break;
		}
	}
}
