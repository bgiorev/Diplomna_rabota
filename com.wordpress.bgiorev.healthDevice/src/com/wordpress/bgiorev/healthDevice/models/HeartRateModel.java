package com.wordpress.bgiorev.healthDevice.models;

public class HeartRateModel {
	private int id;
	private int heartRate;
	private String updated_at;
	public HeartRateModel(int id, int heartRate, String updated_at) {
		super();
		this.id = id;
		this.heartRate = heartRate;
		this.updated_at = updated_at;
	}
	
	public HeartRateModel(int id, int heartRate) {
		this.id = id;
		this.heartRate = heartRate;
	}
	
	public HeartRateModel(int heartRate) {
		this.heartRate = heartRate;
	}
	
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHeartRate() {
		return heartRate;
	}
	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}
}
