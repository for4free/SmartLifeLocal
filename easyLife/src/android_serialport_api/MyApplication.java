package android_serialport_api;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tobey.bean_family.Family;
import com.tobey.bean_huanxin.CallReceiver;
import com.tobey.dao_sensorDB.DBManager;
import com.tobey.dao_serial.SensorDataGet;

public class MyApplication extends Application{
	
	public static Context applicationContext;
    private CallReceiver callReceiver;
	/**
     * 串口数据管理接口
     */
    static SensorDataGet sensorData;// 串口
    /**
     * 数据库管理接口
     */
    private DBManager mgr;  
   //设备ID，JSON路径
    private String MyId;
    private String path_setHTCP = "http://123.206.78.18/api/TSetData.php?Tid=";
	private String path_getHTCP = "http://123.206.78.18/api/TGetData.php?tid=";
    private String path_AddDevice = "http://123.206.78.18/api/AddDevice.php?uidORtid=";
    private String path_DeleteDevice = "http://123.206.78.18/api/DeleteDevice.php?uidORtid=";
    private String path_getScenes = "http:" +
    		"//2naive.cn/api/GetModel.php?tid=";
    private String path_changeScenes = "http://2naive.cn/api/ChangeModel.php?tid=";
    private String path_getJoinFamily = "http://2naive.cn/api/JoinFamily.php?tidORuid=";
    //获取经纬度
    /**
     * 天气数据
     */
	private String path_forecast;
    private double latitude;  
    private double longitude; 
    private LocationManager locationManager ; 
    
    private Family resultFromFamily = null;
      
    
    @Override
    public void onCreate() {
    	super.onCreate();
    	sensorData = new SensorDataGet();
    	try {
            sensorData = new SensorDataGet();
            if( sensorData.getPortStatus() ){
            	showMessage("打开串口成功");
            } else {
            	showMessage("打开串口失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
//    	MyId = "abc1234"; 
    	String m_szAndroidID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
    	MyId = "id" + m_szAndroidID;
    	joinFamilyStatus("http://2naive.cn/api/TJudge.php?tid=" + MyId);
    	path_forecast = "https://api.caiyunapp.com/v2/kQq6LBc-ueXw2M0d/";
    	path_getHTCP = "http://123.206.78.18/api/TGetData.php?tid=";
    	path_setHTCP = "http://123.206.78.18/api/TSetData.php?Tid=";
    	path_AddDevice = "http://123.206.78.18/api/AddDevice.php?uidORtid=";
    	path_getScenes = "http://2naive.cn/api/GetModel.php?tid=";
    	path_changeScenes = "http://2naive.cn/api/ChangeModel.php?tid=";
    	setPath_DeleteDevice("http://123.206.78.18/api/DeleteDevice.php?uidORtid=");
        latitude=36.001347;
        longitude =120.124097;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        applicationContext = this;
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);
        IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingCallBroadcastAction());
        if(callReceiver == null){
            callReceiver = new CallReceiver();
        }
        //注册通话广播接收者
        this.registerReceiver(callReceiver, callFilter);
    }
    public String getPath_changeScenes() {
		return path_changeScenes + getMyId() + "&Mid=";
	}
    public String getPath_Scenes() {
		return path_getScenes + getMyId();
	}
	public void setPath_getScenes(String path_getScenes) {
		this.path_getScenes = path_getScenes;
	}
	public String getPath_JoinFamily() {
		return path_getJoinFamily + getMyId() + "&fmlNu=";
	}
	
	@Override
    public void onTerminate() {
    	super.onTerminate();
    }
    
    /**
     * --显示消息
     * @param sMsg
     */
    private void showMessage(String sMsg)
    {
        Toast.makeText(this, sMsg, Toast.LENGTH_SHORT).show();
    }

  //设置位置坐标
    public void setLocation(double latitude, double longitude ) {
    	this.latitude = latitude;
    	this.longitude = longitude;
    }
    
    
    //获取位置坐标
    public String getLocation() {
    	LocationListener locationListener = new LocationListener() {  
            
            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
            @Override  
            public void onStatusChanged(String provider, int status, Bundle extras) {  
                  
            }  
              
            // Provider被enable时触发此函数，比如GPS被打开  
            @Override  
            public void onProviderEnabled(String provider) {  
                  
            }  
              
            // Provider被disable时触发此函数，比如GPS被关闭   
            @Override  
            public void onProviderDisabled(String provider) {  
                  
            }  
              
            //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发   
            @Override  
            public void onLocationChanged(Location location) {  
                if (location != null) {     
                    Log.e("Map", "Location changed : Lat: "    
                    + location.getLatitude() + " Lng: "    
                    + location.getLongitude());     
                }  
            }  
        }; 
        /*Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if(provider == null) {
        	Log.e("MyApplication","No location provider found!");
        }
        Location location = locationManager.getLastKnownLocation(provider);*/
//        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        try {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);     
	        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);     
	        if(location != null){     
	            latitude = location.getLatitude(); //经度     
	            longitude = location.getLongitude(); //纬度  
	        }   
		} catch (Exception e) {
			// TODO: handle exception
		}
          
	    return longitude + "," + latitude;
    }
    
    //获取纬度
    public double getLongitude() {
    	return longitude;
    }
    
    //获取经度
    public double getLatitude() {
    	return latitude;
    }
    
    //获取彩云Json
    public String getPath_forecast() {
    	String location = getLocation();
    	return path_forecast + location + "/forecast.json";
    }
    
    //获取串口操作类实例
    public SensorDataGet getSensorData(){
    	return sensorData;
    }
    
    //获取HTCP
    public String getPath_setHTCP() {
    	return path_setHTCP + getMyId();
    	
    }
    public String getPath_getHTCP() {
    	return path_getHTCP + getMyId();
    	
    }
    
    //获取数据库管理类实例
    public DBManager getDBManager() {
    	return mgr;
    }
    
    public String getMyId() {
    	return MyId;
    }

	public String getPath_AddDevice() {
		return path_AddDevice+getMyId();
	}
	public String getPath_DeleteDevice() {
		return path_DeleteDevice+getMyId();
	}
	public void setPath_DeleteDevice(String path_DeleteDevice) {
		this.path_DeleteDevice = path_DeleteDevice;
	}
	public void joinFamilyStatus(String path) {
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(100);
    	utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(
					com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					resultFromFamily = (Family) JSONObject.parseObject(arg0.result, Family.class);
					if(resultFromFamily.equals("402")) {
						Toast.makeText(applicationContext, "请加入家庭!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
    		
    	});
    	
	}
	
	public Family getResultFromFamily() {
		return resultFromFamily;
	}

}
