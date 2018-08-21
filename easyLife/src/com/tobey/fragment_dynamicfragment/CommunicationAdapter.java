package com.tobey.fragment_dynamicfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tobey.easyLife.R;

public class CommunicationAdapter extends BaseExpandableListAdapter {

	private final Context mContext;
	private final LayoutInflater mLayoutInflater;

	private List<String> mGroups = new ArrayList();

	private Map<String,List<Map<String,String>>> mChilds = new HashMap<String,List<Map<String,String>>>();
	
	private List<Map<String,String>> listHurry = new ArrayList<Map<String,String>>();//紧急呼叫
	private List<Map<String,String>> listFreq = new ArrayList<Map<String,String>>();//常用联系人
	
	public CommunicationAdapter(Context context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroups.add("紧急呼叫");
		mGroups.add("常用联系人");
		Map<String,String> map1 = new HashMap<String,String>();
		map1.put("name", "警察");
		map1.put("phone", "110");
		Map<String,String> map2 = new HashMap<String,String>();
		map2.put("name", "急救");
		map2.put("phone", "120");
		Map<String,String> map3 = new HashMap<String,String>();
		map3.put("name", "火警");
		map3.put("phone", "119");
		
		listHurry.add(map1);
		listHurry.add(map2);
		listHurry.add(map3);
		
		Map<String,String> map4 = new HashMap<String,String>();
		map4.put("name", "Tobey");
		map4.put("phone", "15253246036");
		Map<String,String> map5 = new HashMap<String,String>();
		map5.put("name", "DD");
		map5.put("phone", "15892129867");
		Map<String,String> map6 = new HashMap<String,String>();
		map6.put("name", "GG");
		map6.put("phone", "13328953722");
		
		listFreq.add(map4);
		listFreq.add(map5);
		listFreq.add(map6);
		
		mChilds.put("紧急呼叫",listHurry);
		mChilds.put("常用联系人",listFreq);
		
		
	}
	
	@Override
	public int getGroupCount() {
		return mGroups.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		String groupName = mGroups.get(groupPosition);
		return groupName;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			groupHolder = new GroupHolder();
			convertView = mLayoutInflater.inflate(R.layout.group_communication, null);
			groupHolder.groupName = (TextView) convertView
					.findViewById(R.id.group_communication_group);
			groupHolder.expandedImage = (ImageView) convertView
					.findViewById(R.id.group_communication_image);
			groupHolder.childrenCount = (TextView) convertView
					.findViewById(R.id.group_communication_memberNumber);
			groupHolder.groupName.setTextSize(18);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

		groupHolder.groupName.setText(getGroup(groupPosition).toString());
		groupHolder.childrenCount.setText(String.valueOf(getChildrenCount(groupPosition)));
		if (isExpanded)// ture is Expanded or false is not isExpanded
			groupHolder.expandedImage.setImageResource(R.drawable.expanded);
		else
			groupHolder.expandedImage.setImageResource(R.drawable.collapse);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		String groupName = mGroups.get(groupPosition);
		try {
			int childCount = mChilds.get(groupName).size();
			return childCount;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		String groupName = mGroups.get(groupPosition);
		Map<String, String> child = mChilds.get(groupName).get(childPosition);
		return child;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ItemHolder itemHolder = null;
		if (convertView == null) {
			itemHolder = new ItemHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_communication, null);
			itemHolder.name = (TextView) convertView
					.findViewById(R.id.item_communication_tv_name);
			itemHolder.msgImage = (ImageView) convertView
					.findViewById(R.id.item_communication_iv_msg);
			itemHolder.phoneNumber = (TextView) convertView
					.findViewById(R.id.item_communication_tv_phone);
//			itemHolder.groupName.setTextSize(18);
			convertView.setTag(itemHolder);
		} else {
			itemHolder = (ItemHolder) convertView.getTag();
		}
		String groupName = (String) getGroup(groupPosition);
		final String childName = mChilds.get(groupName).get(childPosition).get("name");
		final String childPhone = mChilds.get(groupName).get(childPosition).get("phone");
		itemHolder.name.setText(childName);
		itemHolder.msgImage.setImageResource(R.drawable.ic_sendmsg);
		itemHolder.phoneNumber.setText(childPhone);
		itemHolder.msgImage.setOnClickListener(new OnClickListener() {
			
			private Uri uri;

			@Override
			public void onClick(View arg0) {
				uri = Uri.parse("smsto:"+childPhone);
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);
				
				mContext.startActivity(it);
				
			}
		}); 
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	class GroupHolder {
		TextView groupName,childrenCount;
		ImageView expandedImage;
		
	}
	
	class ItemHolder {
		TextView name,phoneNumber;
		ImageView  msgImage;
		
	}

}
