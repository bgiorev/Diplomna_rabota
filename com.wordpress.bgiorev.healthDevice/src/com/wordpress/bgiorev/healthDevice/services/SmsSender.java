package com.wordpress.bgiorev.healthDevice.services;

import java.util.ArrayList;

import android.telephony.SmsManager;

public class SmsSender {
	private SmsManager manager;
	private String phoneNumber;
	private String message; 
	
	public SmsSender() {
		manager = SmsManager.getDefault();
		phoneNumber = "0885118805";
	}
	
	public void sendMessage(String disaster, String address){
		createTheMessage(disaster, address);
		System.out.println(phoneNumber);
		System.out.println(message);
		ArrayList<String> parts = manager.divideMessage(message);
		manager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
	}

	private void createTheMessage(String disaster, String address) {
		message = "The patient is suffering " + disaster +" on address " + address;
	}
}
