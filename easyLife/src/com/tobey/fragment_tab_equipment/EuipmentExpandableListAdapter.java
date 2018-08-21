package com.tobey.fragment_tab_equipment;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android_serialport_api.MyApplication;

import com.tobey.dao_HTCP.Operator2HTCP;
import com.tobey.dao_serial.SensorDataGet;
import com.tobey.easyLife.R;

/***
 * 数据源
 * 
 * @author Administrator
 * 
 */
public class EuipmentExpandableListAdapter extends BaseExpandableListAdapter {
	public static Handler handler = new Handler();
	private Context context;
	private LayoutInflater inflater;
	private String path_setHTCP;
	private String path_DeleteAdevice;
	private Operator2HTCP operator2htcp ;
	private int status;
	private SensorDataGet sensorData;
	private String STATUS;

	public List<String> groupList;
	public  Map<String,List<Map<String,String>>> childMap;
	
	public EuipmentExpandableListAdapter(Context context,List<String> parentList, Map<String, List<Map<String, String>>> map) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.groupList = parentList;
		this.childMap = map;
		MyApplication myApplication = ((MyApplication)context.getApplicationContext());
		this.path_setHTCP = myApplication.getPath_setHTCP();
		this.path_DeleteAdevice = myApplication.getPath_DeleteDevice();
		this.sensorData = myApplication.getSensorData();
		this.operator2htcp = new Operator2HTCP(context, "EuipmentExpandableListAdapter");
	}

	// 返回父列表个数
	@Override
	public int getGroupCount() {
		return groupList.size();
	}
	
	// 返回子列表个数
	@Override
	public int getChildrenCount(int groupPosition) {
		String groupName = groupList.get(groupPosition);
		try {
			int childCount = childMap.get(groupName).size();
			return childCount;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {

		String groupName = groupList.get(groupPosition);
		return groupName;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		String groupName = groupList.get(groupPosition);
		Map<String, String> child = childMap.get(groupName).get(childPosition);
		return child;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {

		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			groupHolder = new GroupHolder();
			convertView = inflater.inflate(R.layout.fragment_equipment_group, null);
			groupHolder.groupName = (TextView) convertView
					.findViewById(R.id.fragment_equipment_group);
			groupHolder.expandedImage = (ImageView) convertView
					.findViewById(R.id.fragment_equipment_image);
			groupHolder.childrenCount = (TextView) convertView
					.findViewById(R.id.fragment_equipment_memberNumber);
			groupHolder.groupName.setTextSize(18);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

		groupHolder.groupName.setText(getGroup(groupPosition).toString());
		groupHolder.childrenCount.setText(String.valueOf(getChildrenCount(groupPosition)));
//		if (isExpanded)// ture is Expanded or false is not isExpanded
//			groupHolder.expandedImage.setImageResource(R.drawable.expanded);
//		else
//			groupHolder.expandedImage.setImageResource(R.drawable.collapse);
		final int resId = isExpanded ? R.drawable.expanded : R.drawable.collapse;
		groupHolder.expandedImage.setImageResource(resId);
		return convertView;
		
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_equipment_item, null);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.fragment_equipment_item);
		textView.setTextSize(16);
		String groupName = groupList.get(groupPosition);
		final String childName = childMap.get(groupName).get(childPosition).get("name");
		textView.setText(childName);
        
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class GroupHolder {
		TextView groupName,childrenCount;
		ImageView expandedImage;
		
	}
	
	/**
	 * 向服务器发送数据
	 * @param path：地址+需要上传的数据
	 */
	private void dealHTCP(String path) {
		operator2htcp.dealHTCP(path);
	}
	
	//修改设备信息
	private void upDataInfo(String type, String id,
			String newData, String flag) {
		// TODO Auto-generated method stub
		String path = path_setHTCP + 
				"&getType=" + type + 
				"&getId=" + id + 
				"&newData=" + newData + 
				"&flag=" + flag;
				dealHTCP(path);
	}
	
	//删除设备
	private void confirmDelete(String id, String type) {
		String path = path_DeleteAdevice + "&getId=" + id + "&getType=" + type;
		dealHTCP(path);
	}
}














