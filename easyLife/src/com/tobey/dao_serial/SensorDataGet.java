package com.tobey.dao_serial;

import java.util.Random;

import android.util.Log;

import com.tobey.bean_serial.SerialPortAction;

public class SensorDataGet {
	
	
	private SerialPortAction serialPortAction;
	
	private Boolean portStatus=false;
	
	//private byte[] buffer=new byte[128];
	
	public SensorDataGet(){
		
		serialPortAction=new SerialPortAction();
		
		int re=serialPortAction.OpenPort();
		if(re==-1){
//			System.out.println("打开串口失败");
			return;
		}else if(re==-2){
//			System.out.println("获取输入输出流失败");
			return;
		}
		portStatus=true;
		
	}
	
	public void closeSensor(){
		
		if(portStatus){
			serialPortAction.ClosePort();
			
		}
	}
	/**
	 * 获取串口消息
	 * 返回数据格式：
	 * 有数据时返回：传感器类型ID_命令_传感器节点ID_需要获得的数据
	 * 		传感器类型ID_命令：标识传感器
	 * 		传感器节点ID：标识具体传感器
	 * 使用split("_")对返回值切割后：0：传感器类型ID，1：命令，2：传感器节点ID，3~：数据域 + 校验
	 * 没有传感器接入时返回：""
	 * @return
	 */
	public String getSerialData(){
		byte[] buffer=new byte[128];
		StringBuilder sb = new StringBuilder();
		if(portStatus){
			serialPortAction.ReadData(buffer, buffer.length);
			if(buffer[0]==0x40){
				/**
				 * 获取热释电传感器信息，标识位：51 -> 6
				 * 返回值1: 有人， 0: 无人
				 */
				
				/**
				 *  标识位(传感器ID_命令）_结点ID
				 *  typeID_cmd_NodeID
				 */
				sb.append(buffer[3]).append("_").append(buffer[4]).append("_");
				sb.append(buffer[2]).append("_");
				
				if((buffer[3]==(byte)0x5)&&(buffer[4]==(byte)0x1)){
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * 获取获取霍尔节点的信息,标识位：11 -> 2
				 * 返回值分别为0:无磁场, 1:有磁场
				 */
				else if((buffer[3]==(byte)0x1)&&(buffer[4]==(byte)0x1)){
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * 获取温湿度、光照信息,标识位：21 -> 3
				 * 返回值：type1_type2_id_tem_hum_light
				 */
				else if((buffer[3]==(byte)0x2)&&(buffer[4]==(byte)0x1)){
					
					int tem=buffer[5]*256+buffer[6];
					int hum=buffer[7]*256+buffer[8];
					long light=(long)((buffer[9]*256+buffer[10])*3012.9/(32768*4));
					
					sb.append(tem).append("_");
					sb.append(hum).append("_").append(light);
					return sb.toString();
				}
				/**
				 * 获取获取震动传感器的信息,标识位：31 -> 4
				 * 返回值分别为0:无震动, 1:有震动
				 */
				else if((buffer[3]==(byte)0x3)&&(buffer[4]==(byte)0x1)){
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * 获取获取烟雾传感器的信息,标识位：41 -> 5
				 * 返回值分别为0:无烟雾, 1:有烟雾
				 */
				else if((buffer[3]==(byte)0x4)&&(buffer[4]==(byte)0x1)){
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * 获取获取触摸传感器的信息,标识位：71 -> 8
				 * 返回值分别为0:无触摸, 1:有触摸
				 */
				else if((buffer[3]==0x7)&&(buffer[4]==0x1)){
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * 获取获取电机、LED传感器传感器的信息,标识位：6dd -> -47
				 * 返回值:电机_LED1_LED2_LED3_LED4
				 * 电机状态分别为：0停止1反转2正转
				 * LED状态：0关1开
				 */
				else if((buffer[3]==(byte)0x6)&&(buffer[4]==(byte)0xdd)){
					//System.out.println("############");
					int motor=(buffer[5]>>2)&0x3;
					int led1=(buffer[5]>>7)&0x1;
					int led2=(buffer[5]>>6)&0x1;
					int led3=(buffer[5]>>5)&0x1;
					int led4=(buffer[5]>>4)&0x1;
					sb.append(motor).append("_").append(led1).append("_");
					sb.append(led2).append("_").append(led3).append("_").append(led4);
				}
				/**
				 * 超时波距离检测信息获取,标识位：81 -> 9
				 * 返回值:检测的距离，单位是mm
				 */
				else if((buffer[3]==0x8)&&(buffer[4]==0x1)){
					sb.append(buffer[5]*256+buffer[6]);
					return sb.toString();
				}
				/**
				 * PWM十级调光传感器信息获取，标识位：9dd -> -44
				 * 返回值0-9十个光照等级
				 */
				else if((buffer[3]==(byte)0x9)&&(buffer[4]==(byte)0xdd)){
					
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * 获取继电器传感器节点的信息，标识位：add -> -37
				 * 返回值：
				 * 继电器1_继电器2_继电器3_继电器4
				 * 0_0_1_1代表继电器1和2打开，继电器3和4 闭合
				 */
				else if((buffer[3]==(byte)0xa)&&(buffer[4]==(byte)0xdd)){
					int relay1=buffer[6]&0x1;
					int relay2=(buffer[6]>>1)&0x1;
					int relay3=(buffer[6]>>2)&0x1;
					int relay4=(buffer[6]>>3)&0x1;
					
					sb.append(relay1).append("_").append(relay2).append("_");
					sb.append(relay3).append("_").append(relay4);
					return sb.toString();
				}
				/**
				 * 获取电流传感器的信息，标识位：b1 -> 18
				 * 返回值为：第一路电流值_第二路电流值
				 * 例如2.3_1.5: 代表第一路电流值为2.3A，第二路为1.5A
				 */
				else if((buffer[3]==(byte)0xb)&&(buffer[4]==(byte)0x1)){
					double current1=buffer[5]*3.3/127;
					double current2=buffer[7]*3.3/127;
					sb.append(current1).append("_").append(current2).append("_");
					return sb.toString();
				}
				/**
				 * 电压传感器信息获取(输入电压检测)，标识位：c1 -> 19
				 * 返回值为:第一路电压值_第二路电压值
				 */
				else if((buffer[3]==(byte)0xc)&&(buffer[4]==(byte)0x1)){
					double voltage1=buffer[5]*0.111;
					double voltage2=buffer[7]*0.111;
					
					sb.append(voltage1).append("_").append(voltage2).append("_");
					return sb.toString();
				}
				/**
				 * 电压传感器信息获取(输出电压检测)，标识位：ddd -> -34
				 * 返回值为:第一路_第二路_第三路_第四路
				 */
				else if((buffer[3]==(byte)0xd)&&(buffer[4]==(byte)0xdd)){
					double volout1=buffer[6]*0.04;
					double volout2=buffer[8]*0.04;
					double volout3=buffer[10]*0.04;
					double volout4=buffer[12]*0.04;
					
					sb.append(volout1).append("_").append(volout2).append("_");
					sb.append(volout3).append("_").append(volout4);
					return sb.toString();
				}
			}//获取到传感器消息
		}//检测串口是否打开
		return "";
	}
	/**
	 * 传感器查询、控制指令，如电机，电压输出等
	 * @author Tobey
	 *
	 */
	public enum MotorCmd{
		
		MC_QUERY,MC_MOTORSTOP,MC_MOTORFORWARD,MC_MOTORRESERVE,MC_LED1ON,MC_LED1OFF,MC_LED2ON,MC_LED2OFF,MC_LED3ON,
		MC_LED3OFF,MC_LED4ON,MC_LED4OFF
	}
	
	/**
	 * 电机控制
	 * @param cmd
	 */
	public void sendMotorCmd(MotorCmd cmd, int id){
		
		byte[] command=new byte[6];
		
		command[0]=0x40;
		command[1]=0x6;
//		command[2]=0x1;
		command[2]=(byte) id;
		command[3]=0x6;
		
		switch(cmd){
		// 停止
		case MC_MOTORSTOP:
			command[4]=0xc;
			break;
		// 正转
		case MC_MOTORFORWARD:
			command[4]=0xa;
			break;
		// 反转
		case MC_MOTORRESERVE:
			command[4]=0xb;
			break;
		// led1点亮
		case MC_LED1ON:
			command[4]=0x1;
			break;
		// led1关闭
		case MC_LED1OFF:
			command[4]=0x2;
			break;
		case MC_LED2ON:
			command[4]=0x3;
			break;
		case MC_LED2OFF:
			command[4]=0x4;
			break;
		case MC_LED3ON:
			command[4]=0x5;
			break;
		case MC_LED3OFF:
			command[4]=0x6;
			break;
		case MC_LED4ON:
			command[4]=0x7;
			break;
		case MC_LED4OFF:
			command[4]=0x8;
			break;
		// 查询所有状态
		case MC_QUERY:
			command[4]=(byte) 0xcc;
			break;
		
		
		}
		
		for(int i=0;i<5;i++){
			
			command[5]+=command[i];
		}
		
		for (int i = 0; i < 5; i++ ) {
			try {
				serialPortAction.WriteData(command);
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		
	}
	/**
	 * PWM十级调光节点控制指令，cmd参数0-9为十个等级的设置指令
	 * @param cmd
	 */
	public void sendPwmCmd(int cmd, int id){
		byte[] command=new byte[6];
		
		command[0]=0x40;
		command[1]=0x6;
//		command[2]=0x1;
		command[2]=(byte) id;
		command[3]=0x9;
		
		command[4]=(byte) cmd;
		
		for(int i=0;i<5;i++){
			
			command[5]+=command[i];
		}
		try{
			for (int i = 0; i < 5; i++ ) {
				try {
					serialPortAction.WriteData(command);
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
//			Thread.sleep(500); 
		}catch (Exception e) {
			Log.e("SensorDataGet","sendPwmCmd send data failure" + id + cmd);
		}
				
	}
	
	/**
	 * 插座控制指令，cmd参数0/2为关/开的设置指令
	 * @param cmd
	 */
	public void sendSocketCmd(int cmd, int id){
		byte[] command=new byte[7];
		
		command[0]=0x40;
		command[1]=0x7;
//		command[2]=0x1;
		command[2]=(byte) id;
		command[3]=(byte) 0xa1;
		
		command[4]=0x2;
		command[5]=(byte) cmd;
		command[6]=0x56;
		
		try{
			for (int i = 0; i < 5; i++ ) {
				try {
					serialPortAction.WriteData(command);
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
//			Thread.sleep(500); 
		}catch (Exception e) {
			Log.e("SensorDataGet","sendPwmCmd send data failure" + id + cmd);
		}
				
	}

	/**
	 * 空调控制指令，cmd参数0/2为关/开的设置指令
	 * @param cmd
	 */
	public void sendAirconditionCmd(int cmd, int id){
		byte[] command=new byte[7];
		
		command[0]=0x40;
		command[1]=0x7;
//		command[2]=0x1;
		command[2]=(byte) id;
		command[3]=(byte) 0xa2;
		
		command[4]=0x2;
		command[5]=(byte) cmd;
		command[6]=0x56;
		
		try{
			for (int i = 0; i < 5; i++ ) {
				try {
					serialPortAction.WriteData(command);
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
//			Thread.sleep(500); 
		}catch (Exception e) {
			Log.e("SensorDataGet","sendPwmCmd send data failure" + id + cmd);
		}
				
	}
	
	/**
	 * 电视控制指令，cmd参数0/2为关/开的设置指令
	 * @param cmd
	 */
	public void sendTVCmd(int cmd, int id){
		byte[] command=new byte[7];
		
		command[0]=0x40;
		command[1]=0x7;
//		command[2]=0x1;
		command[2]=(byte) id;
		command[3]=(byte) 0xa3;
		
		command[4]=0x2;
		command[5]=(byte) cmd;
		command[6]=0x56;
		
		try{
			for (int i = 0; i < 5; i++ ) {
				try {
					serialPortAction.WriteData(command);
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
//			Thread.sleep(500); 
		}catch (Exception e) {
			Log.e("SensorDataGet","sendPwmCmd send data failure" + id + cmd);
		}
				
	}
	
	/**
	 * 加湿器控制指令，cmd参数0/2为关/开的设置指令
	 * @param cmd
	 */
	public void sendHumidifierCmd(int cmd, int id){
		byte[] command=new byte[7];
		
		command[0]=0x40;
		command[1]=0x7;
		command[2]=(byte) id;
		command[3]=(byte) 0xa4;
		
		command[4]=0x2;
		command[5]=(byte) cmd;
		command[6]=0x56;
		
		try{
			
			for (int i = 0; i < 5; i++ ) {
				try {
					serialPortAction.WriteData(command);
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
//			Thread.sleep(500); 
		}catch (Exception e) {
			Log.e("SensorDataGet","sendPwmCmd send data failure" + id + cmd);
		}
				
	}
	
	/**
	 * 空气净化机控制指令，cmd参数0/2为关/开的设置指令
	 * @param cmd
	 */
	public void sendAircleanerCmd(int cmd, int id){
		byte[] command=new byte[7];
		
		command[0]=0x40;
		command[1]=0x7;
		command[2]=0x1;
//		command[2]=(byte) id;
		command[3]=(byte) 0xa5;
		
		command[4]=0x2;
		command[5]=(byte) cmd;
		command[6]=0x56;	
		
		StringBuilder cmdtest = new StringBuilder();
		try{
			for (int i = 0; i < 5; i++ ) {
				try {
					serialPortAction.WriteData(command);
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}catch (Exception e) {
			Log.e("SensorDataGet","sendPwmCmd send data failure" + id + cmd);
		}
				
	}
	
	/**
	 * PWM十级调光节点状态查询指令，用来查询当前光照等级，指令发送后，
	 * 节点会上传数据包，需要通过上面介绍的信息获取接口来读取。
	 */
	public void sendPwmQuery(){
		byte[] command={0x40,0x6,0x1,0x9,(byte) 0xcc,0x1c};
		serialPortAction.WriteData(command);
		
		
	}
	/**
	 * 继电器控制指令，参数分别为四个继电器的控制，0为打开1为闭合
	 * @param relay1
	 * @param relay2
	 * @param relay3
	 * @param relay4
	 */
	public void sendRelayCmd(int relay1,int relay2,int relay3,int relay4, int id){
		byte[] command=new byte[7];
		
		command[0]=0x40;
		command[1]=0x7;
		command[2]=(byte) id;
		command[3]=0xa;
		
		command[4]=0xf;
		
		if(relay1==1){
			command[5]|=0x1;
		}else{
			
			command[5]&=0xfe;
		}
		
		if(relay2==1){
			command[5]|=0x2;
		}else{
			
			command[5]&=0xfd;
		}
		
		if(relay3==1){
			command[5]|=0x4;
		}else{
			
			command[5]&=0xfb;
		}
		
		if(relay4==1){
			command[5]|=0x8;
		}else{
			
			command[5]&=0xf7;
		}
		
		for(int i=0;i<6;i++){
			
			command[6]+=command[i];
		}
		serialPortAction.WriteData(command);
		
		
	}
	/**
	 * 继电器状态查询指令
	 */
	public void sendRelayQuery(){
		
		byte[] command={0x40,0x7,0x1,0xa,(byte) 0xcc,0x0,0x1e};
		serialPortAction.WriteData(command);
	}
	
	/**
	 * 电压输出设置，参数分别为四路电压的设置值，设置范围为0-10V
	 * @param volout1
	 * @param volout2
	 * @param volout3
	 * @param volout4
	 */
	public void sendVoloutCmd(double volout1,double volout2,double volout3,double volout4,int id){
		
		byte[] command=new byte[14];
		
		command[0]=0x40;
		command[1]=0xe;
		command[2]=(byte) id;
		command[3]=0xd;
		command[4]=0x4f;
		
		command[5]=(byte)(volout1*256/9.9);
		command[6]=0x0;
		command[7]=(byte)(volout2*256/9.9);
		command[8]=0x0;
		command[9]=(byte)(volout3*256/9.9);
		command[10]=0x0;
		command[11]=(byte)(volout4*256/9.9);
		command[12]=0x0;
		
		for(int i=0;i<13;i++){
			
			command[13]+=command[i];
		}
		serialPortAction.WriteData(command);
		
	}
	/**
	 * 电压输出查询
	 */
	public void sendVoloutQuery(){
		
		byte[] command={0x40,0xe,0x1,0xd,0xc,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x28};
		serialPortAction.WriteData(command);
		
	}
	
	/**
	 * 获取串口打开状况信息
	 * 返回值:true,false
	 * @return
	 */
	public boolean getPortStatus(){
		return portStatus;
	}
}