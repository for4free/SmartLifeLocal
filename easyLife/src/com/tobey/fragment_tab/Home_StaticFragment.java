package com.tobey.fragment_tab;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpException;

import android.app.Dialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android_serialport_api.MyApplication;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tobey.bean_HTCP.GetData;
import com.tobey.bean_HTCP.ResultHTCP;
import com.tobey.bean_forecast.Forecast;
import com.tobey.bean_forecast.Hourly;
import com.tobey.bean_forecast.Pm25;
import com.tobey.bean_forecast.Result;
import com.tobey.bean_forecast.Skycon;
import com.tobey.bean_forecast.Temperature;
import com.tobey.bean_sensorDB.Sensor;
import com.tobey.dao_HTCP.Operator2HTCP;
import com.tobey.dao_serial.SensorDataGet;
import com.tobey.easyLife.R;

public class Home_StaticFragment extends Fragment implements  Runnable{

	 // TAG
	 // 传感器节点号：68
	private String MyId;//开发板/App标识
	private static final int FromHTCP = 33;	//表示收到HTCP发来的数据
	
	// 各类传感器的标识符
	protected static final int PYRO = 5;		//热释电
//	protected static final int HALL = 2;		//霍尔
	protected static final int THL = 2;			//温湿度光照
//	protected static final int SHOCK = 4;		//震动
//	protected static final int SMOKE = 5;		//烟
//	protected static final int TOUCH = 8;		//触摸
//	protected static final int MOTOR = -47;		//电机
//	protected static final int ULTRA = 9;		//距离
//	protected static final int PWM = -44;		//Pwm
//	protected static final int RELAY = -37;		//继电器
//	protected static final int CURRENT = 18;	//电流
//	protected static final int VOLTAGE = 19;	//电压（输入）
//	protected static final int VOLOUT = -34;	//电压（输出）
	
    // 用户界面控件
    private TextView tv_showTempSensor;
    private TextView tv_showHum;
    private TextView tv_showLight;
    private View homeStaticLayout;
    private View fragment_home_static_rv_forecast;
    private TextView tv_showSky;
    private TextView tv_showTime;
    private ImageView iv_skyImg;
    private TextView tv_showPm25;
    private TextView tv_showTempForecast;

    // 串口数据等变量
    private SensorDataGet sensorData;
    private String serialData[];
    
    /**
     * 数据库管理
     */
    private String statusAndData;	//状态和数据
    
    // 天气数据
	private String path_forecast;
	private String path_setHTCP ;
	private String path_getHTCP ;
	
	// 天气json中获取的数据
	private List<Skycon> skyconList = new ArrayList<Skycon>();
	private List<Pm25> pm25List = new ArrayList<Pm25>();
	private List<Temperature> temperatureList = new ArrayList<Temperature>();
	private String description;
	
	// HTCP中的数据
	private GetData getDataFromHTCP;
	private List<ResultHTCP> resultFromHTCP = new ArrayList<ResultHTCP>();
	private Operator2HTCP operator2htcp = new Operator2HTCP(getActivity(), "Home_StaticFragment");
	private int status;
	private String status_PYRO = "0";
	
	private ArrayList<HashMap<String, String>> mylist;//listview中显示的内容
    
    // 线程处理,串口数据处理/Json更新
    private Handler handler = new Handler(){
    	public void handleMessage(Message msg) {
    		/**
    		 *  定义用于获取查询单个结果
    		 */
			Sensor sensorOne = new Sensor();
    		switch (msg.what) {
    		case THL:// 温湿度光照
    			statusAndData = serialData[3] + "_" + serialData[4] + "_" + serialData[5];
    			dealTHL();
    			break;
    		case PYRO:// pyro
    			status_PYRO = serialData[3];
    			Log.e("Home_StaticFragment","The case is PYRO: " + status_PYRO);
    			
			default:
				break;
    		}
    		
    	}

		

    };
    // 实例化时执行
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	if(homeStaticLayout != null) {
    		ViewGroup parent = (ViewGroup) homeStaticLayout.getParent();
    		if(parent != null)
    			parent.removeView(homeStaticLayout);
    	}
    	try {
    		homeStaticLayout = inflater.inflate(R.layout.fragment_home_static,
                container, false);
    	} catch (InflateException e) {
    		/* homeStaticLayout is already there, just return view as it is */
    	}
         MyApplication myApplication = ((MyApplication)getActivity().getApplication());
         // 获取串口及数据库管理对象
         sensorData = myApplication.getSensorData();
         path_setHTCP = myApplication.getPath_setHTCP();
         path_getHTCP = myApplication.getPath_getHTCP();
         path_forecast =  myApplication.getPath_forecast();
         String myId = myApplication.getMyId();
         double longitude = myApplication.getLongitude();
         double latitude = myApplication.getLatitude();
         String sendLocation = "http://123.206.78.18/api/SetInfo.php?tidORuid=" + myId + "&setLong=" + longitude + "&setLat=" + latitude;
         dealHTCP(sendLocation);
         initControl();//实例化控件
//         homeStaticLayout.getBackground().setAlpha(150);
         return homeStaticLayout;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	
    	setRetainInstance(true);
    }
    
	/**
     * 生命周期结束时需要进行的操作
     */
    @Override
    public void onDestroy() {
    	super.onDestroy(); 
    	// 关闭串口
    	CloseComPort(sensorData);
    }

    //---实例化控件及添加控件监听器、 添加线程
    private void initControl()
    {
        tv_showTempSensor = (TextView)homeStaticLayout.findViewById(R.id.fragment_home_static_tv_showTempSensor);
        tv_showHum = (TextView)homeStaticLayout.findViewById(R.id.fragment_home_static_tv_showHum);
        tv_showLight = (TextView)homeStaticLayout.findViewById(R.id.fragment_home_static_tv_showLight);
        iv_skyImg = (ImageView)homeStaticLayout.findViewById(R.id.fragment_home_static_iv_skyImg);
        tv_showTime = (TextView)homeStaticLayout.findViewById(R.id.fragment_home_static_tv_showTime);
        tv_showSky = (TextView)homeStaticLayout.findViewById(R.id.fragment_home_static_tv_showSky);
        tv_showPm25 = (TextView)homeStaticLayout.findViewById(R.id.fragment_home_static_tv_showPm25);
        tv_showTempForecast = (TextView)homeStaticLayout.findViewById(R.id.fragment_home_static_tv_showTempForecast);
        fragment_home_static_rv_forecast = homeStaticLayout.findViewById(R.id.fragment_home_static_rv_forecast);
        fragment_home_static_rv_forecast.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				forecastPredict();
			}
		});

      //获取传感器数据
        Runnable seonsorRunnable = new Runnable() {
    		
    		public void run() {
    			Message message = new Message();
    			String totalData = sensorData.getSerialData();
    			serialData = totalData.split("_");
    			int mark = 0;
    			// 根据接收到的消息获得传感器标识位
    			if(3 <= serialData.length) {
//    				mark = HexToInt(serialData[0]) + HexToInt(serialData[1]);
    				mark = HexToInt(serialData[0]);
    			}
    				
    			message.what = mark;
    			handler.sendMessage(message);
    			handler.postDelayed(this, 1000);
    			System.gc();
    		}
    	};
		handler.postDelayed(seonsorRunnable, 1000);
		handler.postDelayed(getHTCPRunnable, 1000);
		//===========json
		final Runnable forecastFirstRunnable = new Runnable() {
			
			public void run() {
				forecastUpdata forecastupdata = new forecastUpdata();
				forecastupdata.execute(path_forecast);
				handler.postDelayed(this, 500);
			}
		};
		handler.post(forecastFirstRunnable);
		
		Runnable forecastFirstRemoveRunnable = new Runnable() {
			
			public void run() {
				handler.removeCallbacks(forecastFirstRunnable);
			}
		};
		handler.postDelayed(forecastFirstRemoveRunnable,6000);
		
		Runnable forecastRunnable = new Runnable() {
			
			public void run() {
				forecastUpdata forecastupdata = new forecastUpdata();
				forecastupdata.execute(path_forecast);
				handler.postDelayed(this, 60000);
			}
		};
		handler.postDelayed(forecastRunnable, 60000);
		
		dispData();//控件初始化--显示数据
    }
    
	//获取HTCP数据
	Runnable getHTCPRunnable = new Runnable() {

		public void run() {
			AsySendMsg sendMsg = new AsySendMsg();
			sendMsg.execute();
			handler.postDelayed(this, 500);
		}
		
	};
    
    // Hex字符串转int
    static public int HexToInt(String inHex)
    {
    	return Integer.parseInt(inHex, 16);
    }
    // ----初始化界面
    private void dispData()
    {
        tv_showTempSensor.setText("22 ℃");
        tv_showHum.setText("234 %");
        tv_showLight.setText("98 LUM");
    }

    //==传感器处理函数===
    
    // 温湿度光照值进行动态更新
    private void dealTHL(){
        try {
        	tv_showTempSensor.setText(serialData[3] + " ℃");
            tv_showHum.setText(serialData[4] + " %");
            tv_showLight.setText(serialData[5] + " LUM");
            StringBuilder sb = new StringBuilder();
            sb.append(path_setHTCP);
            sb.append("&getType=").append(serialData[0]);
            sb.append("&getId=").append(serialData[2]);
            sb.append("&newData=");
            sb.append(statusAndData);
            dealHTCP(sb.toString());
        } catch (Exception e) {
        	Log.e("home_static","dealTHL has somthing wrong!");
        }
    }
    
    // ---关闭串口
    private void CloseComPort(SensorDataGet ComPort){
        if (ComPort!=null){
            ComPort.closeSensor();
        }
    }
    // --显示消息
    private void showMessage(String sMsg)
    {
        Toast.makeText(getActivity(), sMsg, Toast.LENGTH_SHORT).show();
    }

	// ======解析JSON
	public void updataUI() {
		mylist = new ArrayList<HashMap<String, String>>(); 
		Calendar c = Calendar.getInstance();  
		int year = c.get(Calendar.YEAR) ; 
		int month = c.get(Calendar.MONTH);  
		int day = c.get(Calendar.DAY_OF_MONTH);  
		int hourInt = c.get(Calendar.HOUR_OF_DAY);  
		int minuteInt = c.get(Calendar.MINUTE);  
		String hour = null, minute = null;
		if(hourInt < 9) {
			hour = "0" + hourInt;
		}else{
			hour = hourInt + "";
		}
		if(minuteInt < 10) {
			minute = "0" + minuteInt;
		}else{
			minute = "" + minuteInt;
		}
		String Time = hour + ":" + minute;
		try {
			String sky = skyconList.iterator().next().getValue();
			String dis = description;
			float pm25Float = Float.parseFloat(pm25List.iterator().next().getValue());
			int pm25 = Math.round(pm25Float);
			float tempForecastFloat = Float.parseFloat(temperatureList.iterator().next().getValue());
			int tempForecast = Math.round(tempForecastFloat);
			switch(sky) {
			case "晴":
				iv_skyImg.setImageResource(R.drawable.qingtian);
				break;
			case "晴夜":
				iv_skyImg.setImageResource(R.drawable.qingye);
				break;
			case "多云":
				iv_skyImg.setImageResource(R.drawable.duoyunbaitian);
				break;
			case "多云夜":
				iv_skyImg.setImageResource(R.drawable.duoyunye);
				break;
			case "阴":
				iv_skyImg.setImageResource(R.drawable.yin);
				break;
			case "雨":
				iv_skyImg.setImageResource(R.drawable.yu);
				break;
			case "冻雨":
				iv_skyImg.setImageResource(R.drawable.dongyu);
				break;
			case "雪":
				iv_skyImg.setImageResource(R.drawable.xue);
				break;
			case "风":
				iv_skyImg.setImageResource(R.drawable.dafeng);
				break;
			case "雾":
				iv_skyImg.setImageResource(R.drawable.wu);
				break;
			case "霾":
				iv_skyImg.setImageResource(R.drawable.wu);
				break;
			}
			tv_showTime.setText(Time);
			tv_showSky.setText(sky);
	        tv_showPm25.setText("PM2.5：" + pm25 + "μg/m3");
	        tv_showTempForecast.setText(tempForecast + "°");
		} catch (Exception e) {
			tv_showTime.setText(Time);
			tv_showSky.setText("");
	        tv_showPm25.setText("");
	        tv_showTempForecast.setText("");
		}
		
	}
    
	public void forecastPredict() {
		String dis = description;
		ArrayList<HashMap<String, String>> mylist_dialog = new ArrayList<HashMap<String, String>>();
		mylist_dialog = getJsonData(skyconList, temperatureList, pm25List);
		
		Dialog_home_static_forecast forecast_Dialog = new Dialog_home_static_forecast(getActivity(), dis, mylist_dialog, R.style.Dialog);
		forecast_Dialog.show();
		disBackGround(forecast_Dialog);
        
	}
	//虚化背景
	private void disBackGround(Dialog dialog) {
		// TODO Auto-generated method stub
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
	    lp.dimAmount = 0.55f;
	    dialog.getWindow().setAttributes(lp);
	    dialog.getWindow()
	            .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
	// 构造天气预报数据
    private ArrayList<HashMap<String, String>> getJsonData(
			List<Skycon> skyconList2, List<Temperature> temperatureList2,
			List<Pm25> pm25List2) {
    	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Iterator<Skycon> iterSkycon = skyconList2.iterator();
		Iterator<Temperature> iterTemp = temperatureList2.iterator();
		Iterator<Pm25> iterPm25 = pm25List2.iterator();
    	while(iterSkycon.hasNext())  
	    {  
	        HashMap<String, String> map = new HashMap<String, String>();  
	        map.put("Skycon", iterSkycon.next().getValue());  
	        float tempForecast = Float.parseFloat(iterTemp.next().getValue());
	        map.put("Temperature", Math.round(tempForecast) + "°"); 
	        float pm25 = Float.parseFloat(iterPm25.next().getValue());
	        map.put("Pm25", "Pm2.5:" + Math.round(pm25) + "μg/m3");   
	        map.put("Time", iterSkycon.next().getDatetime());
	        list.add(map);  
	    }  
		return list;
	}
    
    // 加载数据数据
    public void loadData(String path){
    	HttpUtils utils = new HttpUtils();
    	utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

    		public void onFailure(HttpException arg0, String arg1) {
//				Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				//加载数据  转化为JSON格式
				Forecast forecast = (Forecast) JSONObject.parseObject(arg0.result, Forecast.class);
				//根据字段用get方法获取字段内容
				if(!forecast.equals(null)) {
					Result result = forecast.getResult();
					Hourly hourly = result.getHourly();
					skyconList.clear();
					skyconList = hourly.getSkycon();
					skycon2Chinese();
					pm25List.clear();
					pm25List = hourly.getPm25();
					temperatureList.clear();
					temperatureList = hourly.getTemperature();
					description = hourly.getDescription();
				}
			}

			public void skycon2Chinese() {
				Iterator<Skycon> it = skyconList.iterator();
				while(it.hasNext()) {
					Skycon skycon = it.next();
					String value = skycon.getValue();
					switch(value){
					case "CLEAR_DAY":
						skycon.setValue("晴");
						break;
					case "CLEAR_NIGHT":
						skycon.setValue("晴夜");
						break;
					case "PARTLY_CLOUDY_DAY":
						skycon.setValue("多云");
						break;
					case "PARTLY_CLOUDY_NIGHT":
						skycon.setValue("多云夜");
						break;
					case "CLOUDY":
						skycon.setValue("阴");
						break;
					case "RAIN":
						skycon.setValue("雨");
						break;
					case "SLEET":
						skycon.setValue("冻雨");
						break;
					case "SNOW":
						skycon.setValue("雪");
						break;
					case "WIND":
						skycon.setValue("风");
						break;
					case "FOG":
						skycon.setValue("雾");
						break;
					case "HAZE":
						skycon.setValue("霾");
						break;
					default:
						break;
					}
				}
			}

			@Override
			public void onFailure(
					com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
//				Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_LONG).show();
			}
		});
    }

	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	// 向服务器发送数据
	private void dealHTCP(String path) {
		operator2htcp.dealHTCP(path);
		/*switch(status) {
		case 403:
			showMessage("数据更新成功！");
			break;
		case 402:
			showMessage("数据未更改！");
			break;
		case 401:
			showMessage("传入空值！");
			break;
		case 0:
			showMessage("请检查网络是否正常连接！");
			break;
		default:
			break;
		}*/
	}
	
    // 加载数据数据
    public void getHTCP(String path){
		status = operator2htcp.getHTCP(path);
		switch(status) {
		case 1:
			resultFromHTCP = operator2htcp.getResultFromHTCP();
			break;
		case 0:
			break;
		default:
			break;
		}
	}

    private int flagStart = 0;
    //向串口发送指令
    public void sendMSG() {
    	try {
    		Iterator<ResultHTCP> it = resultFromHTCP.iterator();
        	ResultHTCP sensorFromHTCP = new ResultHTCP();
        	while(it.hasNext()) {
        		sensorFromHTCP = it.next();
        		String flag = sensorFromHTCP.getFlag();
    	    	if("0".equals(flag) || !sensorData.getPortStatus())
    	    		continue;
        		final String TYPE = sensorFromHTCP.getGetType();
        		final String ID = sensorFromHTCP.getGetId();
    	    	final int id = Integer.parseInt(ID);
    	    	
    	    	String statusAndData = sensorFromHTCP.getDdata();
    	    	switch(TYPE) {
    	    	case "5":
    	    		flagStart++;
    	    		if (flagStart <= 10) {
    					continue;
    				}
    	    		final String path_setPYRO = path_setHTCP + "&getType=" + TYPE + "&getId=" + ID + "&newData=" + statusAndData.split("_")[0] + "_" + status_PYRO + "&flag=1";
    	    		Log.e("send","path " + path_setPYRO);
    	    		Log.e("send","status " + status_PYRO);
                	dealHTCP(path_setPYRO);
                	flagStart = 0;
        				
    	    		break;
    	    		
    	    	case "9":
    	    		final int status_Illumination = Integer.parseInt(statusAndData);
    	    		Runnable pwmRunnable = new Runnable() {
    					
    					public void run() {
    						// TODO Auto-generated method stub
    						try {
    							Random random = new Random();
    							int i = random.nextInt(10);
    							Thread.sleep(50*i);
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						try {
    							
    							String path_setPWM = path_setHTCP + "&getType=" + TYPE + "&getId=" + ID + "&newData=" + status_Illumination + "&flag=" + "0";
    							dealHTCP(path_setPWM);
    							sensorData.sendPwmCmd(status_Illumination,id);
    						} catch (Exception e) {
    							Log.e("Home_StaticFragment","串口操作失败! 请检查串口是否连接正常！");
    						}
    						
    					}
    				};
    				new Thread(pwmRunnable).start();
    	    		break;
    	    	case "6":
    	    		final int status_Motor = Integer.parseInt(statusAndData);
    	    		Runnable MotorRunnable = new Runnable() {
    					
    					public void run() {
    						// TODO Auto-generated method stub
    						try {
    							Random random = new Random();
    							int i = random.nextInt(10);
    							Thread.sleep(50*i);
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						try {
    							String path_setMotor = path_setHTCP + "&getType=" + TYPE + "&getId=" + ID + "&newData=" + status_Motor + "&flag=" + "0";
    							dealHTCP(path_setMotor);
    							if(0 == status_Motor) {
    								sensorData.sendMotorCmd(SensorDataGet.MotorCmd.MC_MOTORRESERVE,id);
    							} else if (1 == status_Motor) {
    								sensorData.sendMotorCmd(SensorDataGet.MotorCmd.MC_MOTORFORWARD, id);
    							} 
    							
    						} catch (Exception e) {
    							// TODO: handle exception
    							Log.e("Home_StaticFragment","串口操作失败! 请检查串口是否连接正常！");
    						}
    						try {
//    							Random random = new Random();
//    							int i = random.nextInt(10);
    							Thread.sleep(2000);
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						try {
    							sensorData.sendMotorCmd(SensorDataGet.MotorCmd.MC_MOTORSTOP, id);
    						} catch (Exception e) {
    							// TODO: handle exception
    							Log.e("Home_StaticFragment","串口操作失败! 请检查串口是否连接正常！");
    						}
    					}
    					
    				};
    				new Thread(MotorRunnable).start();
    	    		
    	    		break;
    	    		
    	    	case "a1"://插座
    	    		final int status_Socket = Integer.parseInt(statusAndData.split("_")[0]);
    	    		final String allStatusSocket = statusAndData;
    	    		Runnable SocketRunnable = new Runnable() {
    					
    					public void run() {
    						// TODO Auto-generated method stub
    						try {
    							Random random = new Random();
    							int i = random.nextInt(10);
    							Thread.sleep(50*i);
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						try {
    							String path_setSocket = path_setHTCP + "&getType=" + TYPE + "&getId=" + ID + "&newData=" + allStatusSocket + "&flag=" + "0";
    							dealHTCP(path_setSocket);
    							if(0 == status_Socket) {
    								sensorData.sendSocketCmd(status_Socket,id);
    							} else if (1 == status_Socket) {
    								sensorData.sendSocketCmd(2,id);
    							} 
    							
    						} catch (Exception e) {
    							Log.e("Home_StaticFragment","串口操作失败! 请检查串口是否连接正常！");
    						}
    						
    					}
    				};
    				new Thread(SocketRunnable).start();
    	    		break;
    	    		
    	    	case "a2"://空调
    	    		final int status_Aircondition = Integer.parseInt(statusAndData.split("_")[0]);
    	    		final String allStatusAir = statusAndData;
    	    		Runnable AirconditionRunnable = new Runnable() {
    					
    					public void run() {
    						// TODO Auto-generated method stub
    						try {
    							Random random = new Random();
    							int i = random.nextInt(10);
    							Thread.sleep(50*i);
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						try {
    							String path_setAircondition = path_setHTCP + "&getType=" + TYPE + "&getId=" + ID + "&newData=" + allStatusAir + "&flag=" + "0";
    							dealHTCP(path_setAircondition);
    							if(0 == status_Aircondition) {
    								sensorData.sendAirconditionCmd(status_Aircondition,id);
    							} else if (1 == status_Aircondition) {
    								sensorData.sendAirconditionCmd(2,id);
    							} 
    							
    						} catch (Exception e) {
    							Log.e("Home_StaticFragment","串口操作失败! 请检查串口是否连接正常！");
    						}
    						
    					}
    				};
    				new Thread(AirconditionRunnable).start();
    	    		break;
    	    		
    	    	case "a3":
    	    		final int status_TV = Integer.parseInt(statusAndData.split("_")[0]);
    	    		final String allStatusTV = statusAndData;
    	    		Runnable TVRunnable = new Runnable() {
    					
    					public void run() {
    						// TODO Auto-generated method stub
    						try {
    							Random random = new Random();
    							int i = random.nextInt(10);
    							Thread.sleep(50*i);
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						try {
    							String path_setTV = path_setHTCP + "&getType=" + TYPE + "&getId=" + ID + "&newData=" + allStatusTV + "&flag=" + "0";
    							dealHTCP(path_setTV);
    							if(0 == status_TV) {
    								sensorData.sendTVCmd(status_TV,id);
    							} else if (1 == status_TV) {
    								sensorData.sendTVCmd(2,id);
    							} 
    							
    						} catch (Exception e) {
    							Log.e("Home_StaticFragment","串口操作失败! 请检查串口是否连接正常！");
    						}
    						
    					}
    				};
    				new Thread(TVRunnable).start();
    	    		break;
    	    		
    	    	case "a4":
    	    		final int status_Humidifier = Integer.parseInt(statusAndData.split("_")[0]);
    	    		final String allStatusHumid = statusAndData;
    	    		Runnable HumidifierRunnable = new Runnable() {
    					
    					public void run() {
    						// TODO Auto-generated method stub
    						try {
    							Random random = new Random();
    							int i = random.nextInt(10);
    							Thread.sleep(50*i);
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						try {
    							String path_setHum = path_setHTCP + "&getType=" + TYPE + "&getId=" + ID + "&newData=" + allStatusHumid + "&flag=" + "0";
    							dealHTCP(path_setHum);
    							if(0 == status_Humidifier) {
    								sensorData.sendHumidifierCmd(status_Humidifier,id);
    							} else if (1 == status_Humidifier) {
    								sensorData.sendHumidifierCmd(2,id);
    							} 
    							
    						} catch (Exception e) {
    							Log.e("Home_StaticFragment","串口操作失败! 请检查串口是否连接正常！");
    						}
    						
    					}
    				};
    				new Thread(HumidifierRunnable).start();
    	    		break;
    	    		
    	    	case "a5":
    	    		final int status_Aircleaner = Integer.parseInt(statusAndData.split("_")[0]);
    	    		final String allStatusAircleaner = statusAndData;
    	    		Runnable AircleanerRunnable = new Runnable() {
    					
    					public void run() {
    						// TODO Auto-generated method stub
    						try {
    							Random random = new Random();
    							int i = random.nextInt(10);
    							Thread.sleep(50*i);
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						try {
    							String path_setAircleaner = path_setHTCP + "&getType=" + TYPE + "&getId=" + ID + "&newData=" + allStatusAircleaner + "&flag=" + "0";
    							dealHTCP(path_setAircleaner);
    							if(0 == status_Aircleaner) {
    								sensorData.sendAircleanerCmd(status_Aircleaner,id);
    							} else if (1 == status_Aircleaner) {
    								sensorData.sendAircleanerCmd(2,id);
    							} 
    							
    						} catch (Exception e) {
    							Log.e("Home_StaticFragment","串口操作失败! 请检查串口是否连接正常！");
    						}
    						
    					}
    				};
    				new Thread(AircleanerRunnable).start();
    	    		break;
    	    		
    			default:
    				break;
    	    	}
        	}
    	} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	
    }
    class forecastUpdata extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... arg0) {
			loadData(arg0[0]);
			return null;
		}
    	@Override
    	protected void onPostExecute(String result) {
    		// TODO Auto-generated method stub
    		super.onPostExecute(result);
    		updataUI();
    		
    	}
    }
    
    class AsySendMsg extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... arg0) {
			getHTCP(path_getHTCP);
			return null;
		}
    	@Override
    	protected void onPostExecute(String result) {
    		// TODO Auto-generated method stub
    		super.onPostExecute(result);
    		sendMSG();
    		
    	}
    }
    
}