<?php
require_once("response_service.php");
$service = new ResponseService();
$request_text = $_POST["request"];
$request = json_decode($request_text, true);
$response = '';
switch($request['id']) {
	case 1: $response = $service->bodyTemperature($request['value']);
			break;
	case 2: $response = $service->bloodPressure($request['value'], $request['value2']);
			break;
	case 3: $response = $service->heartRate($request['value']);
			break;
	case 4: $response = $service->bloodSaturation($request['value']);
			break;
}
echo $response;
?>