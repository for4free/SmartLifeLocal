package com.tobey.bean_forecast;

import java.util.List;

public class Hourly {
	private String description;
	private List<Skycon> skycon;
	private List<Temperature> temperature;
	private List<Pm25> pm25;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Skycon> getSkycon() {
		return skycon;
	}
	public void setSkycon(List<Skycon> skycon) {
		this.skycon = skycon;
	}
	public List<Temperature> getTemperature() {
		return temperature;
	}
	public void setTemperature(List<Temperature> temperature) {
		this.temperature = temperature;
	}
	public List<Pm25> getPm25() {
		return pm25;
	}
	public void setPm25(List<Pm25> pm25) {
		this.pm25 = pm25;
	}
	
	
}
