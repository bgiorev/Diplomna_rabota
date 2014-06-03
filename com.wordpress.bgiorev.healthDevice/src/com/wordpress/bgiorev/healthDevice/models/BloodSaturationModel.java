package com.wordpress.bgiorev.healthDevice.models;

public class BloodSaturationModel {
	private int id;
	private double saturation;
	private String updatedAt;
	public BloodSaturationModel(int id, double saturation, String updatedAt) {
		super();
		this.id = id;
		this.saturation = saturation;
		this.updatedAt = updatedAt;
	}
	public BloodSaturationModel(double saturation, String updatedAt) {
		super();
		this.saturation = saturation;
		this.updatedAt = updatedAt;
	}
	public BloodSaturationModel(double saturation) {
		super();
		this.saturation = saturation;
	}
	public BloodSaturationModel(int id, double saturation) {
		super();
		this.id = id;
		this.saturation = saturation;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getSaturation() {
		return saturation;
	}
	public void setSaturation(double saturation) {
		this.saturation = saturation;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}	
}
