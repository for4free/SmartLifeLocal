package com.tobey.fragment_tab_equipment;

import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android_serialport_api.MyApplication;

import com.tobey.dao_HTCP.Operator2HTCP;
import com.tobey.dao_serial.SensorDataGet;
import com.tobey.easyLife.R;
import com.tobey.fragment_tab_equipment.equipment_THL_Dialog.equipment_THL_DialogListener;
import com.tobey.fragment_tab_equipment.equipment_TV_Dialog.euipment_TV_DialogListener;
import com.tobey.fragment_tab_equipment.equipment_airCleaner_Dialog.euipment_airCleaner_DialogListener;
import com.tobey.fragment_tab_equipment.equipment_airCondition_Dialog.euipment_airCondition_DialogListener;
import com.tobey.fragment_tab_equipment.equipment_humidifier_Dialog.euipment_humidifier_DialogListener;
import com.tobey.fragment_tab_equipment.equipment_illumination_Dialog.equipment_illumination_DialogListener;
import com.tobey.fragment_tab_equipment.equipment_pyro_Dialog.equipment_pyro_DialogListener;
import com.tobey.fragment_tab_equipment.equipment_socket_Dialog.euipment_socket_DialogListener;

public class WrapperExpandableListAdapter extends BaseExpandableListAdapter {

	public static Handler handler = new Handler();
	private final Context mContext;
    private final ExpandableListAdapter mWrappedAdapter;
    private final SparseBooleanArray mGroupExpandedMap = new SparseBooleanArray();

    private String path_setHTCP;
	private String path_DeleteAdevice;
	private Operator2HTCP operator2htcp ;
	private SensorDataGet sensorData;
    
    public WrapperExpandableListAdapter(ExpandableListAdapter adapter, Context mContext) {
        this.mWrappedAdapter = adapter;
        this.mContext = mContext;
        MyApplication myApplication = ((MyApplication)mContext.getApplicationContext());
		this.path_setHTCP = myApplication.getPath_setHTCP();
		this.path_DeleteAdevice = myApplication.getPath_DeleteDevice();
		this.sensorData = myApplication.getSensorData();
		this.operator2htcp = new Operator2HTCP(mContext, "EuipmentExpandableListAdapter");
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mWrappedAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mWrappedAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public int getGroupCount() {
        return mWrappedAdapter.getGroupCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mWrappedAdapter.getChildrenCount(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mWrappedAdapter.getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mWrappedAdapter.getChild(groupPosition, childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mWrappedAdapter.getGroupId(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mWrappedAdapter.getChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return mWrappedAdapter.hasStableIds();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView != null) {
            final Object tag = convertView.getTag(R.id.fgelv_tag_changed_visibility);
            if(tag instanceof Boolean) {
                final boolean changedVisibility = (Boolean) tag;
                if(changedVisibility) {
                    convertView.setVisibility(View.VISIBLE);
                }
            }
            convertView.setTag(R.id.fgelv_tag_changed_visibility, null);
        }
        mGroupExpandedMap.put(groupPosition, isExpanded);
        return mWrappedAdapter.getGroupView(groupPosition, isExpanded, convertView, parent);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = mWrappedAdapter.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
    	String groupName = String.valueOf(mWrappedAdapter.getGroup(groupPosition));
    	final Map<String,String> child = (Map<String, String>) mWrappedAdapter.getChild(groupPosition, childPosition);
		final String childName = child.get("name");
		
		final String group = String.valueOf(mWrappedAdapter.getGroup(groupPosition));
		final String TYPE = child.get("type");
		final String ID = child.get("id");
		final String STATUS = child.get("status");
		final int intID = Integer.parseInt(ID);
		final String FLAG = "1";
		view.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				switch(group) {
				case "安防":
					equipment_pyro_Dialog pyro_Dialog = 
						new equipment_pyro_Dialog(childName, STATUS, mContext,  R.style.Dialog, new equipment_pyro_DialogListener() {
							
							@Override
							public void onClick(View view) {
								String status = new String();
								int check = view.getId();
								switch(check) {
								case R.id.dialog_equipment_pyro_iv_delete:
									status = "delete";
									break;
								case R.id.dialog_equipment_pyro_bt_confirm:
									status = "confirm";
									confirmDelete(ID, TYPE);
									break;
								case R.id.dialog_equipment_pyro_bt_cancel:
									status = "cancel";
									break;
								default:
									break;
								}
								Message messagePyro = handler.obtainMessage();
								messagePyro.what = 1;
								messagePyro.getData().putString("status", status);
								if(status.equals("delete")) {
									messagePyro.getData().putString("name", childName);
								}
								handler.sendMessage(messagePyro);
							}
							
						});
					pyro_Dialog.show();
					disBackGround(pyro_Dialog);
					break;
				case "照明":
					equipment_illumination_Dialog illumination_Dialog = 
						new equipment_illumination_Dialog(childName, STATUS, TYPE, ID, mContext, R.style.Dialog, new equipment_illumination_DialogListener(){
							
							@Override
							public void onClick(View view) {
								String status = new String();
								int check = view.getId();
								switch(check) {
								case R.id.dialog_equipment_illumination_iv_delete:
									status = "delete";
									break;
								case R.id.dialog_equipment_illumination_bt_confirm:
									status = "confirm";
									confirmDelete(ID, TYPE);
									break;
								case R.id.dialog_equipment_illumination_bt_cancel:
									status = "cancel";
									break;
								default:
									break;
								}
								Message messageIllumination = handler.obtainMessage();
								messageIllumination.what = 1;
								messageIllumination.getData().putString("status", status);
								if(status.equals("delete")) {
									messageIllumination.getData().putString("name", childName);
								}
								handler.sendMessage(messageIllumination);
							}
							
						});
					
					
					
					
					
					illumination_Dialog.show();
					disBackGround(illumination_Dialog);
					break;
				case "插座":
					equipment_socket_Dialog socket_Dialog = 
							new equipment_socket_Dialog(childName, STATUS, mContext, R.style.Dialog, new euipment_socket_DialogListener() {
								
								@Override
								public void onClick(View view) {
									int check = view.getId();
									String[] statusPiece = STATUS.split("_");
									String path_socket = null;
									String status = new String();
									String NEWDATA = null;
									switch(check) {
									case R.id.dialog_equipment_socket_bt_open:
										status = "1";
										NEWDATA = "1" + "_" +  statusPiece[1];
										upDataInfo(TYPE, ID, NEWDATA, FLAG);
										break;
									case R.id.dialog_equipment_socket_bt_close:
										status = "0";
										NEWDATA = "0" + "_" +  statusPiece[1];
										upDataInfo(TYPE, ID, NEWDATA, FLAG);
										break;
									case R.id.dialog_equipment_socket_iv_delete:
										status = "delete";
										break;
									case R.id.dialog_equipment_socket_bt_confirm:
										status = "confirm";
										confirmDelete(ID, TYPE);
										break;
									case R.id.dialog_equipment_socket_bt_cancel:
										status = "cancel";
										break;
									default:
										break;
									}
									Message messageSocket = handler.obtainMessage();
									messageSocket.what = 1;
									messageSocket.getData().putString("status", status);
									if(status.equals("delete")) {
										messageSocket.getData().putString("name", childName);
									}
									handler.sendMessage(messageSocket);
								}
							});
					socket_Dialog.show();
					disBackGround(socket_Dialog);
					break;	
				case "窗帘":
					equipment_curtain_Dialog curtain_Dialog = 
							new equipment_curtain_Dialog(childName, STATUS, mContext, R.style.Dialog, new equipment_curtain_Dialog.equipment_curtain_DialogListener() {
								String status = new String();
								String NEWDATA = null;
								@Override
								public void onClick(View view) {
									int check = view.getId();
									switch(check) {
									case R.id.dialog_equipment_curtain_bt_open:
										NEWDATA = "1";
										upDataInfo(TYPE, ID, NEWDATA, FLAG);
										status = "1";
										break;
									case R.id.dialog_equipment_curtain_bt_stop:
										try {
											sensorData.sendMotorCmd(SensorDataGet.MotorCmd.MC_MOTORSTOP, intID);
										} catch (Exception e) {
											Log.e("串口操作失败","请检查串口是否连接正常！");
										}
										
										break;
									case R.id.dialog_equipment_curtain_bt_close:
										NEWDATA = "0";
										upDataInfo(TYPE, ID, NEWDATA, FLAG);
										status = "0";
										break;
									case R.id.dialog_equipment_curtain_iv_delete:
										status = "delete";
										break;
									case R.id.dialog_equipment_curtain_bt_confirm:
										status = "confirm";
										confirmDelete(ID, TYPE);
										break;
									case R.id.dialog_equipment_curtain_bt_cancel:
										status = "cancel";
										break;
									default:
										break;
									}
									Message messageCurtain = handler.obtainMessage();
									messageCurtain.what = 1;
									messageCurtain.getData().putString("status", status);
									if(status.equals("delete")) {
										messageCurtain.getData().putString("name", childName);
									}
									handler.sendMessage(messageCurtain);
								}
							});
					curtain_Dialog.show();
					disBackGround(curtain_Dialog);
					break;
				
				case "空调":
					equipment_airCondition_Dialog airCondition_Dialog = 
						new equipment_airCondition_Dialog(childName, STATUS, mContext, R.style.Dialog, new euipment_airCondition_DialogListener(){
							
							@Override
							public void onClick(View view) {
								int check = view.getId();
								String[] statusPiece = STATUS.split("_");
								String path_socket = null;
								String status = new String();
								String NEWDATA = null;
								switch(check) {
								case R.id.dialog_equipment_airCondition_bt_on:
									NEWDATA = "1" + "_" +  statusPiece[1] + "_" +  statusPiece[2];
									upDataInfo(TYPE, ID, NEWDATA, FLAG);
									status = "1";
									break;
								case R.id.dialog_equipment_airCondition_bt_off:
									NEWDATA = "0" + "_" +  statusPiece[1] + "_" +  statusPiece[2];
									upDataInfo(TYPE, ID, NEWDATA, FLAG);
									status = "0";
									break;
								case R.id.dialog_equipment_airCondition_iv_delete:
									status = "delete";
									break;
								case R.id.dialog_equipment_airCondition_bt_confirm:
									status = "confirm";
									confirmDelete(ID, TYPE);
									break;
								case R.id.dialog_equipment_airCondition_bt_cancel:
									status = "cancel";
									break;
								default:
									break;
								}
								Message messageAirCondition = handler.obtainMessage();
								messageAirCondition.what = 1;
								messageAirCondition.getData().putString("status", status);
								if(status.equals("delete")) {
									messageAirCondition.getData().putString("name", childName);
								}
								handler.sendMessage(messageAirCondition);
							}

						});
					airCondition_Dialog.show();
					disBackGround(airCondition_Dialog);
					break;
				case "电视":
					equipment_TV_Dialog TV_Dialog = 
					new equipment_TV_Dialog(childName, STATUS, mContext, R.style.Dialog, new euipment_TV_DialogListener() {
						
						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
							int check = view.getId();
							String[] statusPiece = STATUS.split("_");
							String path_socket = null;
							String status = new String();
							String NEWDATA = null;
							switch(check) {
							case R.id.dialog_equipment_TV_bt_on:
								NEWDATA = "1" + "_" +  statusPiece[1] + "_" +  statusPiece[2];
								upDataInfo(TYPE, ID, NEWDATA, FLAG);
								status = "1";
								break;
							case R.id.dialog_equipment_TV_bt_off:
								NEWDATA = "0" + "_" +  statusPiece[1] + "_" +  statusPiece[2];
								upDataInfo(TYPE, ID, NEWDATA, FLAG);
								status = "0";
								break;
							case R.id.dialog_equipment_TV_iv_delete:
								status = "delete";
								break;
							case R.id.dialog_equipment_TV_bt_confirm:
								status = "confirm";
								confirmDelete(ID, TYPE);
								break;
							case R.id.dialog_equipment_TV_bt_cancel:
								status = "cancel";
								break;
							default:
								break;
							}
							Message messageTV = handler.obtainMessage();
							messageTV.what = 1;
							messageTV.getData().putString("status", status);
							if(status.equals("delete")) {
								messageTV.getData().putString("name", childName);
							}
							handler.sendMessage(messageTV);
						}
					});
					TV_Dialog.show();
					disBackGround(TV_Dialog);
					
					break;
				case "温湿度":
					equipment_THL_Dialog THL_Dialog = 
					new equipment_THL_Dialog(childName, STATUS, mContext, R.style.Dialog, new equipment_THL_DialogListener() {
							
						public void onClick(View view) {
							String status = new String();
							String NEWDATA = null;
							int check = view.getId();
							switch(check) {
							case R.id.dialog_equipment_thl_iv_delete:
								status = "delete";
								break;
							case R.id.dialog_equipment_thl_bt_confirm:
								status = "confirm";
								confirmDelete(ID, TYPE);
								break;
							case R.id.dialog_equipment_thl_bt_cancel:
								status = "cancel";
								break;
							default:
								break;
							}
							Message messageTHL = handler.obtainMessage();
							messageTHL.what = 1;
							messageTHL.getData().putString("status", status);
							if(status.equals("delete")) {
								messageTHL.getData().putString("name", childName);
							}
							handler.sendMessage(messageTHL);
						}
					});
						
					THL_Dialog.show();
					disBackGround(THL_Dialog);
					break;
				case "增湿器":
					equipment_humidifier_Dialog humidifier_Dialog = 
							new equipment_humidifier_Dialog(childName, STATUS, mContext, R.style.Dialog, new euipment_humidifier_DialogListener(){
								String status = new String();
								String NEWDATA = null;
								@Override
								public void onClick(View view) {
									int check = view.getId();
									switch(check) {
									case R.id.dialog_equipment_humidifier_bt_open:
										NEWDATA = "1";
										upDataInfo(TYPE, ID, NEWDATA, FLAG);
										status = "1";
										break;
									case R.id.dialog_equipment_humidifier_bt_close:
										NEWDATA = "0";
										upDataInfo(TYPE, ID, NEWDATA, FLAG);
										status = "0";
										break;
									case R.id.dialog_equipment_humidifier_iv_delete:
										status = "delete";
										break;
									case R.id.dialog_equipment_humidifier_bt_confirm:
										status = "confirm";
										confirmDelete(ID, TYPE);
										break;
									case R.id.dialog_equipment_humidifier_bt_cancel:
										status = "cancel";
										break;
									default:
										break;
									}
									Message messagehumidifier = handler.obtainMessage();
									messagehumidifier.what = 1;
									messagehumidifier.getData().putString("status", status);
									if(status.equals("delete")) {
										messagehumidifier.getData().putString("name", childName);
									}
									handler.sendMessage(messagehumidifier);
								}
							});
					humidifier_Dialog.show();
					disBackGround(humidifier_Dialog);
					break;
				case "空气净化机":
					equipment_airCleaner_Dialog airCleaner_Dialog = 
					new equipment_airCleaner_Dialog(childName, STATUS, mContext, R.style.Dialog, new euipment_airCleaner_DialogListener(){
						String status = new String();
						String NEWDATA = null;
						@Override
						public void onClick(View view) {
							int check = view.getId();
							switch(check) {
							case R.id.dialog_equipment_airCleaner_bt_open:
								NEWDATA = "1";
								upDataInfo(TYPE, ID, NEWDATA, FLAG);
								status = "1";
								break;
							case R.id.dialog_equipment_airCleaner_bt_close:
								NEWDATA = "0";
								upDataInfo(TYPE, ID, NEWDATA, FLAG);
								status = "0";
								break;
							case R.id.dialog_equipment_airCleaner_iv_delete:
								status = "delete";
								break;
							case R.id.dialog_equipment_airCleaner_bt_confirm:
								status = "confirm";
								confirmDelete(ID, TYPE);
								break;
							case R.id.dialog_equipment_airCleaner_bt_cancel:
								status = "cancel";
								break;
							default:
								break;
							}
							Message messageairCleaner = handler.obtainMessage();
							messageairCleaner.what = 1;
							messageairCleaner.getData().putString("status", status);
							if(status.equals("delete")) {
								messageairCleaner.getData().putString("name", childName);
							}
							handler.sendMessage(messageairCleaner);
						}
					});
					airCleaner_Dialog.show();
					disBackGround(airCleaner_Dialog);
					break;
				default:
					break;
				}
			}

			private void disBackGround(Dialog dialog) {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			    lp.dimAmount = 0.55f;
			    dialog.getWindow().setAttributes(lp);
			    dialog.getWindow()
			            .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			}

		});
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return mWrappedAdapter.isChildSelectable(groupPosition, childPosition);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mWrappedAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEmpty() {
        return mWrappedAdapter.isEmpty();
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        mGroupExpandedMap.put(groupPosition, true);
        mWrappedAdapter.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        mGroupExpandedMap.put(groupPosition, false);
        mWrappedAdapter.onGroupCollapsed(groupPosition);
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return mWrappedAdapter.getCombinedGroupId(groupId);
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return mWrappedAdapter.getCombinedChildId(groupId, childId);
    }

    public boolean isGroupExpanded(int groupPosition) {
        final Boolean expanded = mGroupExpandedMap.get(groupPosition);
        return expanded != null ? expanded : false;
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
