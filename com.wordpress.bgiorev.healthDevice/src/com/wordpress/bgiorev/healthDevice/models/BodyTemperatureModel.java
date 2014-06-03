package com.wordpress.bgiorev.healthDevice.models;

public class BodyTemperatureModel {
	private int id;
	private double bodyTemperature;
	private String updated_at;
	
	public BodyTemperatureModel(int id, double bodyTemperature, String updated_at) {
		super();
		this.id = id;
		this.bodyTemperature = bodyTemperature;
		this.updated_at = updated_at;
	}
	
	public BodyTemperatureModel(int id, double bodyTemperature) {
		this.id = id;
		this.bodyTemperature = bodyTemperature;
	}
	
	public BodyTemperatureModel(double bodyTemperature) {
		this.bodyTemperature = bodyTemperature;
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
	public double getBodyTemperature() {
		return bodyTemperature;
	}
	public void setBodyTemperature(int bodyTemperature) {
		this.bodyTemperature = bodyTemperature;
	}
	
}
