package com.drones.springbootdrones.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drones.springbootdrones.model.Drone;
import com.drones.springbootdrones.model.Medication;
import com.drones.springbootdrones.service.DroneService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/drones")
public class DroneController {

    @Autowired
    private DroneService droneService;
	
	@GetMapping
	public ResponseEntity<List<Drone>> getDrones() {
		List<Drone> drones = droneService.getAllDrones();
		return new ResponseEntity<>(drones, HttpStatus.OK);
	}
	
	@GetMapping("/history")
	public ResponseEntity<Map<String, Integer>> getDronesHistory() {
		Map<String, Integer> dronesBattery = droneService.getBatteryLevelHistory();
		return new ResponseEntity<>(dronesBattery, HttpStatus.OK);
	}

	@Valid
	@PostMapping
	public ResponseEntity<Drone> registerDrone(@Valid @RequestBody Drone drone) {
		Drone registeredDrone = droneService.registerDrone(drone);
		return new ResponseEntity<>(registeredDrone, HttpStatus.CREATED);
	}

	@GetMapping("/{serialNumber}/medications")
	public ResponseEntity<List<Medication>> getLoadedMedications(@PathVariable String serialNumber) {
		List<Medication> medications = droneService.getLoadedMedications(serialNumber);
		return new ResponseEntity<>(medications, HttpStatus.OK);
	}

	@PostMapping("/{serialNumber}/medications")
	public ResponseEntity<Drone> loadMedications(@PathVariable String serialNumber,
			@Valid @RequestBody List<Medication> medications) {
		Drone loadedDrone = droneService.loadMedications(serialNumber, medications);
		return new ResponseEntity<>(loadedDrone, HttpStatus.OK);
	}

	@GetMapping("/available-for-loading")
	public ResponseEntity<List<Drone>> getAvailableDrones() {
		List<Drone> availableDrones = droneService.getAvailableDrones();
		return ResponseEntity.ok(availableDrones);
	}

	@GetMapping("/{serialNumber}/battery")
	public ResponseEntity<Integer> getBatteryLevel(@PathVariable String serialNumber) {
		Integer batteryLevel = droneService.getBatteryLevel(serialNumber);
		return new ResponseEntity<>(batteryLevel, HttpStatus.OK);
	}
}
