package com.tobey.bean_serial;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;
import android_serialport_api.SerialPort;

/**
 *���ڸ���������
 */
public class SerialPortAction{
	private SerialPort mSerialPort;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private String sPort="/dev/s3c2410_serial1";
	private int iBaudRate=115200;
	private int rcvNum=0;
	//---���캯��
	public SerialPortAction(String sPort,int iBaudRate){
		this.sPort = sPort;
		this.iBaudRate=iBaudRate;
	}
	public SerialPortAction(){
		this("/dev/s3c2410_serial1",115200);
	}
	public SerialPortAction(String sPort){
		this(sPort,115200);
	}
	public SerialPortAction(String sPort,String sBaudRate){
		this(sPort,Integer.parseInt(sBaudRate));
	}
	//---�򿪴���
	public int OpenPort(){
		try {
			mSerialPort =  new SerialPort(new File(sPort), iBaudRate, 0);
		} catch (Exception e) {
			return -1;
		}
		try {
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
		} catch (Exception e) {
			return -2;
		}
		return 0;
	}
	//----�رմ���
	public void ClosePort(){
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
	//----����������
	public int WriteData(byte[] bOutArray){
		try
		{
			mOutputStream.write(bOutArray);
			mOutputStream.flush();
		} catch (IOException e)
		{
//			e.printStackTrace();
			Log.e("SerialPortAction","send data failure");
			return -1;
		}
		return 0;
	}
	//--------����������
	public int ReadData(byte[] b,int length){
		
		rcvNum=0;
		int count=0;
		try {
			while (mInputStream.available() > 0) {
				
				
				
				if(rcvNum>=length-10){
					break;
				}
				count=mInputStream.read(b,rcvNum,length-rcvNum);
				rcvNum+=count;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		
		return rcvNum;
	}
}
