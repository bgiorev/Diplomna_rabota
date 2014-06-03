<?php
require_once("response.php");
require_once("database.php");

class ResponseService {
	public $database;
	
	public function __construct() {
		$this->database = new Database();
	}
	
	public function bodyTemperature($body_temperature) {
		$this->database->query(sprintf(	"INSERT INTO body_temperature (body_temperature) VALUES (%f)", $body_temperature));
		$response = new Response();
		$response->id = 1;
		$response->text = "Everything is OK";
		return json_encode($response);
	}
	
	public function bloodPressure($blood_pressure_top_border, $blood_pressure_bottom_border) {
		$this->database->query(sprintf("INSERT INTO blood_pressure (blood_pressure_top_border, blood_pressure_bottom_border) VALUES (%d, %d)",$blood_pressure_top_border, $blood_pressure_bottom_border));
		$response = new Response();
		$response->id = 2;
		$response->text = "Everything is OK";
		return json_encode($response);
	}
	
	public function heartRate($heart_rate) {
		$this->database->query(sprintf("INSERT INTO heart_rate (heart_rate) VALUES(%d)", $heart_rate));
		$response = new Response();
		$response->id = 3;
		$response->text = "Everything is OK";
		return json_encode($response);
	}
	
	public function bloodSaturation($blood_saturation) {
		$this->database->query(sprintf("INSERT INTO blood_saturation (blood_saturation) VALUES(%f)", $blood_saturation));
		$response = new Response();
		$response->id = 4;
		$response->text = "Everything is OK";
		return json_encode($response);
	}
}

?>