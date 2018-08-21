package com.tobey.dao_sensorDB;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tobey.bean_sensorDB.DBHelper;
import com.tobey.bean_sensorDB.Sensor;
  
public class DBManager {  
    private DBHelper helper;  
    private SQLiteDatabase db;  
      
    public DBManager(Context context) {  
        helper = new DBHelper(context);  
        //��ΪgetWritableDatabase�ڲ�������mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //����Ҫȷ��context�ѳ�ʼ��,���ǿ��԰�ʵ����DBManager�Ĳ������Activity��onCreate��  
        db = helper.getWritableDatabase();  
    }  
      
    /** 
     * �����ݿ����������
     * add sensors 
     * @param sensors 
     */  
    public void add(Sensor sensor) {  
        db.beginTransaction();  //��ʼ����  
//        int row;
        try {  
            db.execSQL("INSERT INTO sensor VALUES(?, ?, ?, ?)", new Object[]{sensor.type, sensor.cmd, sensor.id, sensor.statusAndData});  
            db.setTransactionSuccessful();  //��������ɹ����  
        } /*catch (Exception e) {
        	System.out.println("����ʧ�ܣ�");
		} */finally {  
            db.endTransaction();    //��������  
        }  
    }  
      
    /** 
     * �������ݿ⣨�޸��豸״̬-1����ת��0��ֹͣ��1����ת / 1������0���أ�
     * ���������type+id+status(�µ�״̬��Ϣ)
     * ����ֵ��0���޸�ʧ��
     * update sensor's status
     * @param sensor 
     */  
    public int update(Sensor sensor) { 
    	int row;
        ContentValues cv = new ContentValues(); 
        cv.put("statusAndData", sensor.statusAndData);
        row = db.update("sensor", cv, "type = ? and id = ?", new String[]{String.valueOf(sensor.type), String.valueOf(sensor.id)});  
        if(0 != row) {
        	Log.e("DBManager","update success");
        } else {
        	Log.e("DBManaber","update failled");
        }
        return row;
    }  
      
    /** 
     * ɾ���豸
     * ������type+id
     * ����ֵ��0��ɾ��ʧ��
     * delete sensor 
     * @param sensor 
     */  
    public int deleteSensor(Sensor sensor) {  
        int row;
    	row = db.delete("sensor", "type = ? and id = ?", new String[]{String.valueOf(sensor.type), String.valueOf(sensor.id)});  
    	System.out.println("row = " + row);
    	return row;
    }  
      
    /** 
     * ��ѯ�����豸������
     * query all sensors, return list 
     * @return List<Sensor> 
     */  
    public List<Sensor> queryAll() {  
        ArrayList<Sensor> sensors = new ArrayList<Sensor>();  
        Cursor c = queryTheCursor();  
        while (c.moveToNext()) {  
            Sensor sensor = new Sensor();  
            sensor.type = c.getInt(c.getColumnIndex("type"));  
            sensor.cmd = c.getInt(c.getColumnIndex("cmd"));  
            sensor.id = c.getInt(c.getColumnIndex("id"));  
            sensor.statusAndData = c.getString(c.getColumnIndex("statusAndData"));  
            sensors.add(sensor);  
        }  
        c.close();  
        return sensors;  
    }  
      
    /** 
     * query all sensors, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM sensor", null);  
        return c;  
    }  
    
    /** 
     * ��ѯ������¼
     * ������type+id
     * ����ֵΪ�գ�����û�иü�¼
     * query sensor, return list 
     * @return List<Sensor> 
     */  
    public Sensor queryOne(String type, String id) {  
        Sensor sensors = new Sensor(); 
        Cursor c = db.rawQuery("select * from sensor where type=? and id=?", new String[]{type, id});;  
    	while (c.moveToNext()) {  
            Sensor sensor = new Sensor();  
            sensor.type = c.getInt(c.getColumnIndex("type"));  
            sensor.cmd = c.getInt(c.getColumnIndex("cmd"));  
            sensor.id = c.getInt(c.getColumnIndex("id"));  
            sensor.statusAndData = c.getString(c.getColumnIndex("statusAndData"));  
        }
        
//        c.getCount();
        c.close();  
        return sensors;  
    }
    
    /**
     * ���ݴ��������ͽ��в�ѯ
     * ����������������
     * ����ֵΪ�գ�û�и������豸����
     * @param sensorQ
     * @return
     */
    public List<Sensor> queryType(String type) {  
        ArrayList<Sensor> sensors = new ArrayList<Sensor>(); 
        Cursor c = db.rawQuery("select * from sensor where type=?", new String[]{String.valueOf(type)});;  
        while (c.moveToNext()) {  
            Sensor sensor = new Sensor();  
            sensor.type = c.getInt(c.getColumnIndex("type"));  
            sensor.cmd = c.getInt(c.getColumnIndex("cmd"));  
            sensor.id = c.getInt(c.getColumnIndex("id"));  
            sensor.statusAndData = c.getString(c.getColumnIndex("statusAndData"));  
            sensors.add(sensor);  
        }  
        c.close();  
        return sensors;  
    }
    
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}  
