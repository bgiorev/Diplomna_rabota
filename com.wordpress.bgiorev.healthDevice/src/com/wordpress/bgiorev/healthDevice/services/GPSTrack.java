package com.wordpress.bgiorev.healthDevice.services;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSTrack implements LocationListener{

	private Location location;
	private Context context;
	
	public GPSTrack(Context context) {
		this.context = context;
	}
	
	public String getLocation(){
		String myLocation = null;
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000*60*1,
                5, this);
		if(manager != null) {
			location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if(location != null) {
			myLocation = getAddress(location.getLatitude(), 
					location.getLongitude());
		}
		manager.removeUpdates(this);
		return myLocation;
	}
	
	private String getAddress(double latitude, double longitude) {
		String myLocation = null;
		
		Geocoder geocoder;
		List<Address> addresses;
		geocoder = new Geocoder(context);
		if(latitude != 0 || longitude != 0) {
			try {
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
				String address = addresses.get(0).getAddressLine(0);
				String city = addresses.get(0).getAddressLine(1);
				String country = addresses.get(0).getAddressLine(2);
				myLocation = address + ", " + city + ", " + country;
				myLocation = myLocation.replaceAll("null", "");
				myLocation = myLocation.replaceAll("Unnamed", "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return myLocation;
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
}
