package com.wordpress.bgiorev.healthDevice.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.wordpress.bgiorev.healthDevice.models.BloodPressureModel;
import com.wordpress.bgiorev.healthDevice.models.BloodSaturationModel;
import com.wordpress.bgiorev.healthDevice.models.BodyTemperatureModel;
import com.wordpress.bgiorev.healthDevice.models.HeartRateModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{

	//CONSTANTS FOR THE DB
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "healthDB";
	private static final String TABLE_HEART_RATE = "heart_rate";
	private static final String TABLE_BLOOD_PRESSURE = "blood_pressure";
	private static final String TABLE_BODY_TEMPERATURE = "body_temperature";
	private static final String TABLE_BLOOD_SATURATION = "blood_saturation";
	
	//Names of columns
	private static final String KEY_ID = "id";
	private static final String KEY_UPDATED_AT = "updated_at";
	
	private static final String KEY_HEART_RATE = "heart_rate";
	
	private static final String KEY_BLOOD_PRESSURE_TOP = "top_border";
	private static final String KEY_BLOOD_PRESSURE_BOTTOM = "bottom_border";
	
	private static final String KEY_BODY_TEMPERATURE = "body_temperature";
	
	private static final String KEY_BLOOD_SATURATION = "blood_saturation";
	
	//CREATING TABLES STRINGS
	private static final String CREATE_HR_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HEART_RATE + " (" 
								+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_HEART_RATE + " INTEGER, " + KEY_UPDATED_AT + " DATETIME)";
	
	private static final String CREATE_BP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BLOOD_PRESSURE + " ("
								+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_BLOOD_PRESSURE_TOP + " INTEGER, "
								+ KEY_BLOOD_PRESSURE_BOTTOM + " INTEGER, " + KEY_UPDATED_AT + " DATETIME)";
	
	private static final String CREATE_BT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BODY_TEMPERATURE + " ("
								+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_BODY_TEMPERATURE + " REAL, " + KEY_UPDATED_AT + " DATETIME)";
	
	private static final String CREATE_BS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BLOOD_SATURATION + " ("
								+ KEY_ID + " INTEGER PRIMARI KEY, " + KEY_BLOOD_SATURATION + " REAL, " + KEY_UPDATED_AT + " DATETIME)";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BP_TABLE);
		db.execSQL(CREATE_BT_TABLE);
		db.execSQL(CREATE_HR_TABLE);
		db.execSQL(CREATE_BS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOOD_PRESSURE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BODY_TEMPERATURE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEART_RATE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOOD_SATURATION);
		onCreate(db);		
	}
	
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
	}
	
	public void updateHeartRate(HeartRateModel heartRate) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_HEART_RATE, heartRate.getHeartRate());
		values.put(KEY_UPDATED_AT, getDateTime());
		db.insert(TABLE_HEART_RATE, null, values);
		db.close();
	}

	public void updateBloodPressure(BloodPressureModel bloodPressure) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_BLOOD_PRESSURE_TOP, bloodPressure.getTopBorder());
		values.put(KEY_BLOOD_PRESSURE_BOTTOM, bloodPressure.getBottomBorder());
		values.put(KEY_UPDATED_AT, getDateTime());
		db.insert(TABLE_BLOOD_PRESSURE, null, values);
		db.close();
	}
	
	public void updateBodyTemperature(BodyTemperatureModel bodyTemperature) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_BODY_TEMPERATURE, bodyTemperature.getBodyTemperature());
		values.put(KEY_UPDATED_AT, getDateTime());
		db.insert(TABLE_BODY_TEMPERATURE, null, values);
		db.close();
	}
	
	public void updateBloodSaturation(BloodSaturationModel bloodSaturation) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_BLOOD_SATURATION, bloodSaturation.getSaturation());
		values.put(KEY_UPDATED_AT, getDateTime());
		db.insert(TABLE_BLOOD_SATURATION, null, values);
		db.close();
	}
	
	public int getCount() {
		String countQuery = "SELECT  * FROM " + TABLE_HEART_RATE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
	}
	
	 private String getDateTime() {
	        SimpleDateFormat dateFormat = new SimpleDateFormat(
	                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	        Date date = new Date();
	        return dateFormat.format(date);
	    }
}
