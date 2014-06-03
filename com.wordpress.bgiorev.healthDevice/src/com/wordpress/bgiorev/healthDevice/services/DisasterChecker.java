package com.wordpress.bgiorev.healthDevice.services;

import android.content.Context;

public class DisasterChecker {
	private Context context;
	private boolean isDisaster;

	public DisasterChecker(Context context) {
		this.context = context;
		this.isDisaster = false;
	}
	
	public void checkForDisaster(int heartRate, double bodyTemperature, int bloodPressureTopBorder, int bloodPressureBottomBorder, int bloodSaturation) {
		if(bloodPressureTopBorder > 160 && bloodPressureBottomBorder > 120){
			sendDisasterMessage("High blood pressure - " + bloodPressureTopBorder + "/" + bloodPressureBottomBorder);
			isDisaster = true;
		}
		if(heartRate > 180){
			sendDisasterMessage("High Pulse - " + heartRate);
			isDisaster = true;
		}
		if(bloodSaturation < 90 && bodyTemperature < 36){
			sendDisasterMessage("Not enought oxygen - " + bloodSaturation + "%, body temperature - " + bodyTemperature);
			isDisaster = true;
		}
	}
	
	public boolean isDisaster() {
		return isDisaster;
	}
	
	private void sendDisasterMessage(String disasterMessage) {
		GPSTrack address = new GPSTrack(context);
		SmsSender sender = new SmsSender();
		sender.sendMessage(disasterMessage, address.getLocation());
	}
}
