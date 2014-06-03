<?php
class Database {
	private $mysqli;
	private $result;
	public function __construct() {
		$this->mysqli = new mysqli("localhost", "root", "", "my_db");
		if (mysqli_connect_errno()) {
			printf("Connect failed: %s\n", mysqli_connect_error());
			exit();
		}
	}
	public function query($sql) {
		$this->result = $this->mysqli->query($sql) or die($this->mysqli->error);
	}
	public function fetch() {
		if (!$this->result) {
			return null;
		}
		return $this->result->fetch_assoc();
	}
}