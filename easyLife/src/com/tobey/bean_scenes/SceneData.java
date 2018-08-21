package com.tobey.bean_scenes;

import java.util.List;

public class SceneData {
	private String status;
	private String MODELID;
	private List<ModelData> data;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMODELID() {
		return MODELID;
	}
	public void setMODELID(String mODELID) {
		MODELID = mODELID;
	}
	public List<ModelData> getData() {
		return data;
	}
	public void setData(List<ModelData> data) {
		this.data = data;
	}

	
	
}
