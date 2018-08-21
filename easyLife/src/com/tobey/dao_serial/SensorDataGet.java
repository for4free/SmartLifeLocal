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
//			System.out.println("�򿪴���ʧ��");
			return;
		}else if(re==-2){
//			System.out.println("��ȡ���������ʧ��");
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
	 * ��ȡ������Ϣ
	 * �������ݸ�ʽ��
	 * ������ʱ���أ�����������ID_����_�������ڵ�ID_��Ҫ��õ�����
	 * 		����������ID_�����ʶ������
	 * 		�������ڵ�ID����ʶ���崫����
	 * ʹ��split("_")�Է���ֵ�и��0������������ID��1�����2���������ڵ�ID��3~�������� + У��
	 * û�д���������ʱ���أ�""
	 * @return
	 */
	public String getSerialData(){
		byte[] buffer=new byte[128];
		StringBuilder sb = new StringBuilder();
		if(portStatus){
			serialPortAction.ReadData(buffer, buffer.length);
			if(buffer[0]==0x40){
				/**
				 * ��ȡ���͵紫������Ϣ����ʶλ��51 -> 6
				 * ����ֵ1: ���ˣ� 0: ����
				 */
				
				/**
				 *  ��ʶλ(������ID_���_���ID
				 *  typeID_cmd_NodeID
				 */
				sb.append(buffer[3]).append("_").append(buffer[4]).append("_");
				sb.append(buffer[2]).append("_");
				
				if((buffer[3]==(byte)0x5)&&(buffer[4]==(byte)0x1)){
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * ��ȡ��ȡ�����ڵ����Ϣ,��ʶλ��11 -> 2
				 * ����ֵ�ֱ�Ϊ0:�޴ų�, 1:�дų�
				 */
				else if((buffer[3]==(byte)0x1)&&(buffer[4]==(byte)0x1)){
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * ��ȡ��ʪ�ȡ�������Ϣ,��ʶλ��21 -> 3
				 * ����ֵ��type1_type2_id_tem_hum_light
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
				 * ��ȡ��ȡ�𶯴���������Ϣ,��ʶλ��31 -> 4
				 * ����ֵ�ֱ�Ϊ0:����, 1:����
				 */
				else if((buffer[3]==(byte)0x3)&&(buffer[4]==(byte)0x1)){
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * ��ȡ��ȡ������������Ϣ,��ʶλ��41 -> 5
				 * ����ֵ�ֱ�Ϊ0:������, 1:������
				 */
				else if((buffer[3]==(byte)0x4)&&(buffer[4]==(byte)0x1)){
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * ��ȡ��ȡ��������������Ϣ,��ʶλ��71 -> 8
				 * ����ֵ�ֱ�Ϊ0:�޴���, 1:�д���
				 */
				else if((buffer[3]==0x7)&&(buffer[4]==0x1)){
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * ��ȡ��ȡ�����LED����������������Ϣ,��ʶλ��6dd -> -47
				 * ����ֵ:���_LED1_LED2_LED3_LED4
				 * ���״̬�ֱ�Ϊ��0ֹͣ1��ת2��ת
				 * LED״̬��0��1��
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
				 * ��ʱ����������Ϣ��ȡ,��ʶλ��81 -> 9
				 * ����ֵ:���ľ��룬��λ��mm
				 */
				else if((buffer[3]==0x8)&&(buffer[4]==0x1)){
					sb.append(buffer[5]*256+buffer[6]);
					return sb.toString();
				}
				/**
				 * PWMʮ�����⴫������Ϣ��ȡ����ʶλ��9dd -> -44
				 * ����ֵ0-9ʮ�����յȼ�
				 */
				else if((buffer[3]==(byte)0x9)&&(buffer[4]==(byte)0xdd)){
					
					sb.append(buffer[5]);
					return sb.toString();
				}
				/**
				 * ��ȡ�̵����������ڵ����Ϣ����ʶλ��add -> -37
				 * ����ֵ��
				 * �̵���1_�̵���2_�̵���3_�̵���4
				 * 0_0_1_1����̵���1��2�򿪣��̵���3��4 �պ�
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
				 * ��ȡ��������������Ϣ����ʶλ��b1 -> 18
				 * ����ֵΪ����һ·����ֵ_�ڶ�·����ֵ
				 * ����2.3_1.5: �����һ·����ֵΪ2.3A���ڶ�·Ϊ1.5A
				 */
				else if((buffer[3]==(byte)0xb)&&(buffer[4]==(byte)0x1)){
					double current1=buffer[5]*3.3/127;
					double current2=buffer[7]*3.3/127;
					sb.append(current1).append("_").append(current2).append("_");
					return sb.toString();
				}
				/**
				 * ��ѹ��������Ϣ��ȡ(�����ѹ���)����ʶλ��c1 -> 19
				 * ����ֵΪ:��һ·��ѹֵ_�ڶ�·��ѹֵ
				 */
				else if((buffer[3]==(byte)0xc)&&(buffer[4]==(byte)0x1)){
					double voltage1=buffer[5]*0.111;
					double voltage2=buffer[7]*0.111;
					
					sb.append(voltage1).append("_").append(voltage2).append("_");
					return sb.toString();
				}
				/**
				 * ��ѹ��������Ϣ��ȡ(�����ѹ���)����ʶλ��ddd -> -34
				 * ����ֵΪ:��һ·_�ڶ�·_����·_����·
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
			}//��ȡ����������Ϣ
		}//��⴮���Ƿ��
		return "";
	}
	/**
	 * ��������ѯ������ָ���������ѹ�����
	 * @author Tobey
	 *
	 */
	public enum MotorCmd{
		
		MC_QUERY,MC_MOTORSTOP,MC_MOTORFORWARD,MC_MOTORRESERVE,MC_LED1ON,MC_LED1OFF,MC_LED2ON,MC_LED2OFF,MC_LED3ON,
		MC_LED3OFF,MC_LED4ON,MC_LED4OFF
	}
	
	/**
	 * �������
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
		// ֹͣ
		case MC_MOTORSTOP:
			command[4]=0xc;
			break;
		// ��ת
		case MC_MOTORFORWARD:
			command[4]=0xa;
			break;
		// ��ת
		case MC_MOTORRESERVE:
			command[4]=0xb;
			break;
		// led1����
		case MC_LED1ON:
			command[4]=0x1;
			break;
		// led1�ر�
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
		// ��ѯ����״̬
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
	 * PWMʮ������ڵ����ָ�cmd����0-9Ϊʮ���ȼ�������ָ��
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
	 * ��������ָ�cmd����0/2Ϊ��/��������ָ��
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
	 * �յ�����ָ�cmd����0/2Ϊ��/��������ָ��
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
	 * ���ӿ���ָ�cmd����0/2Ϊ��/��������ָ��
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
	 * ��ʪ������ָ�cmd����0/2Ϊ��/��������ָ��
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
	 * ��������������ָ�cmd����0/2Ϊ��/��������ָ��
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
	 * PWMʮ������ڵ�״̬��ѯָ�������ѯ��ǰ���յȼ���ָ��ͺ�
	 * �ڵ���ϴ����ݰ�����Ҫͨ��������ܵ���Ϣ��ȡ�ӿ�����ȡ��
	 */
	public void sendPwmQuery(){
		byte[] command={0x40,0x6,0x1,0x9,(byte) 0xcc,0x1c};
		serialPortAction.WriteData(command);
		
		
	}
	/**
	 * �̵�������ָ������ֱ�Ϊ�ĸ��̵����Ŀ��ƣ�0Ϊ��1Ϊ�պ�
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
	 * �̵���״̬��ѯָ��
	 */
	public void sendRelayQuery(){
		
		byte[] command={0x40,0x7,0x1,0xa,(byte) 0xcc,0x0,0x1e};
		serialPortAction.WriteData(command);
	}
	
	/**
	 * ��ѹ������ã������ֱ�Ϊ��·��ѹ������ֵ�����÷�ΧΪ0-10V
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
	 * ��ѹ�����ѯ
	 */
	public void sendVoloutQuery(){
		
		byte[] command={0x40,0xe,0x1,0xd,0xc,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x28};
		serialPortAction.WriteData(command);
		
	}
	
	/**
	 * ��ȡ���ڴ�״����Ϣ
	 * ����ֵ:true,false
	 * @return
	 */
	public boolean getPortStatus(){
		return portStatus;
	}
}