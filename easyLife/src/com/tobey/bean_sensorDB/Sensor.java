package com.tobey.bean_sensorDB;

public class Sensor {  
    public int type;  
    public int cmd;  
    public int id;  
    public String statusAndData;
      
    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatusAndData() {
		return statusAndData;
	}

	public void setStatusAndData(String statusAndData) {
		this.statusAndData = statusAndData;
	}

	public Sensor() {  
    }  
      
    public Sensor(int type, int cmd, int id, String statusAndData) {  
        this.type = type;  
        this.cmd = cmd;  
        this.id = id;  
        this.statusAndData = statusAndData;
    }  
    
    public Sensor(String[] str, String statusAndData){
    	this.type = Integer.parseInt(str[0]);
    	this.cmd = Integer.parseInt(str[1]);
    	this.id = Integer.parseInt(str[2]);
    	this.statusAndData = statusAndData;
    }
}  
