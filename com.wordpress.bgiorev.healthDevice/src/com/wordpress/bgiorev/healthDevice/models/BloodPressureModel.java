package com.wordpress.bgiorev.healthDevice.models;

public class BloodPressureModel {
	private int id;
	private int topBorder;
	private int bottomBorder;
	private String updatedAt;
	
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTopBorder() {
		return topBorder;
	}
	public void setTopBorder(int topBorder) {
		this.topBorder = topBorder;
	}
	public int getBottomBorder() {
		return bottomBorder;
	}
	public void setBottomBorder(int bottomBorder) {
		this.bottomBorder = bottomBorder;
	}
	
	public BloodPressureModel(int id, int topBorder, int bottomBorder,
			String updatedAt) {
		super();
		this.id = id;
		this.topBorder = topBorder;
		this.bottomBorder = bottomBorder;
		this.updatedAt = updatedAt;
	}
	
	public BloodPressureModel(int id, int topBorder, int bottomBorder) {
		this.id = id;
		this.topBorder = topBorder;
		this.bottomBorder = bottomBorder;
	}
	
	public BloodPressureModel(int topBorder, int bottomBorder) {
		this.topBorder = topBorder;
		this.bottomBorder = bottomBorder;
	}
}
