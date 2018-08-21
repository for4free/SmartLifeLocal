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
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
        db = helper.getWritableDatabase();  
    }  
      
    /** 
     * 向数据库中添加数据
     * add sensors 
     * @param sensors 
     */  
    public void add(Sensor sensor) {  
        db.beginTransaction();  //开始事务  
//        int row;
        try {  
            db.execSQL("INSERT INTO sensor VALUES(?, ?, ?, ?)", new Object[]{sensor.type, sensor.cmd, sensor.id, sensor.statusAndData});  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } /*catch (Exception e) {
        	System.out.println("插入失败！");
		} */finally {  
            db.endTransaction();    //结束事务  
        }  
    }  
      
    /** 
     * 更新数据库（修改设备状态-1：反转，0：停止，1：正转 / 1：开，0：关）
     * 传入参数：type+id+status(新的状态信息)
     * 返回值：0：修改失败
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
     * 删除设备
     * 参数：type+id
     * 返回值：0：删除失败
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
     * 查询所有设备的数据
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
     * 查询单条记录
     * 参数：type+id
     * 返回值为空：表中没有该记录
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
     * 根据传感器类型进行查询
     * 参数：传感器类型
     * 返回值为空：没有该类型设备接入
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
