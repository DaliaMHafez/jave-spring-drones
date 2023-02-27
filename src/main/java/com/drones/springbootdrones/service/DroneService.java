package com.drones.springbootdrones.service;

import java.util.List;
import java.util.Map;

import com.drones.springbootdrones.model.Drone;
import com.drones.springbootdrones.model.Medication;

public interface DroneService {
	List<Drone> getAllDrones();

	List<Drone> getAvailableDrones();

	Drone registerDrone(Drone drone);

	Drone loadMedications(String serialNumber, List<Medication> medications);

	List<Medication> getLoadedMedications(String serialNumber);

	int getBatteryLevel(String serialNumber);

	Map<String, Integer> getBatteryLevelHistory();
}
