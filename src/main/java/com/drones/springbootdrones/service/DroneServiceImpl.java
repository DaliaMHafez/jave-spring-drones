package com.drones.springbootdrones.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.drones.springbootdrones.enums.DroneState;
import com.drones.springbootdrones.exceptions.ResourceNotFoundException;
import com.drones.springbootdrones.model.Drone;
import com.drones.springbootdrones.model.Medication;

@Service
public class DroneServiceImpl implements DroneService {
	private final List<Drone> drones = new ArrayList<>();
	private final Map<String, Integer> batteryLevelHistory = new HashMap<>();

	@Override
	public List<Drone> getAllDrones() {
		return drones;
	}

	@Override
	public List<Drone> getAvailableDrones() {
		return drones.stream().filter(drone -> drone.getState().equals(DroneState.IDLE)).collect(Collectors.toList());
	}

	@Override
	public Drone registerDrone(Drone drone) {
		drones.add(drone);
		return drone;
	}

	@Override
	public Drone loadMedications(String serialNumber, List<Medication> medications) {
		Drone drone = drones.stream().filter(d -> d.getSerialNumber().equals(serialNumber)).findFirst().orElseThrow(
				() -> new ResourceNotFoundException("Drone with serial number " + serialNumber + " not found."));

		if (!drone.getState().equals(DroneState.IDLE)) {
			throw new IllegalStateException("Drone is not available for loading");
		}

		drone.setState(DroneState.LOADING);

		int totalWeight = medications.stream().mapToInt(Medication::getWeight).sum();

		if (totalWeight > drone.getWeightLimit()) {
			drone.setState(DroneState.IDLE);
			throw new IllegalStateException("Total weight exceeds drone weight limit");
		}

		if (drone.getBatteryCapacity() < 25) {
			drone.setState(DroneState.IDLE);
			throw new IllegalStateException("Drone battery level is below 25%");
		}
		drone.setMedications(medications);
		drone.setState(DroneState.LOADED);

		return drone;
	}

	@Override
	public List<Medication> getLoadedMedications(String serialNumber) {
		Drone drone = drones.stream().filter(d -> d.getSerialNumber().equals(serialNumber)).findFirst().orElseThrow(
				() -> new ResourceNotFoundException("Drone with serial number :: " + serialNumber + " not found."));
		if (!drone.getState().equals(DroneState.LOADED) && !drone.getState().equals(DroneState.DELIVERING)) {
			throw new IllegalStateException(
					"Drone with serial number " + serialNumber + " is not loaded or delivering.");
		}
		return drone.getMedications();
	}

	@Override
	public int getBatteryLevel(String serialNumber) {
		Drone drone = drones.stream().filter(d -> d.getSerialNumber().equals(serialNumber)).findFirst().orElseThrow(
				() -> new ResourceNotFoundException("Drone with serial number :: " + serialNumber + " not found."));

		return drone.getBatteryCapacity();
	}

	@Override
	public Map<String, Integer> getBatteryLevelHistory() {
		return batteryLevelHistory;
	}

	@Scheduled(fixedRate = 3600000) // 1 hour
	public void checkBatteryLevels() {
		for (Drone drone : drones) {
			int batteryLevel = drone.getBatteryCapacity();
			String key = drone.getSerialNumber() + " at " + new Date().getTime();
			batteryLevelHistory.put(key, batteryLevel);
			if (batteryLevel < 10) {
				drone.setState(DroneState.RETURNING);
			}
		}
	}
}
