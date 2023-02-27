package com.drones.springbootdrones.model;

import java.util.List;

import com.drones.springbootdrones.enums.DroneModel;
import com.drones.springbootdrones.enums.DroneState;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class Drone {
	@Size(max = 6, message = "Serial number must be at most 100 characters")
	private String serialNumber;

	private DroneModel model;

	@Min(value = 0, message = "Weight limit must be at least 0")
	@Max(value = 500, message = "Weight limit cannot exceed 500")
	private int weightLimit;

	@Max(value = 100, message = "Battery Capacity cannot exceed 100")
	private int batteryCapacity;

	private DroneState state;

	private List<Medication> medications;

	public Drone(String serialNumber, DroneState state, int weightLimit, int batteryCapacity) {
		this.serialNumber = serialNumber;
		this.state = state;
		this.weightLimit = weightLimit;
		this.batteryCapacity = batteryCapacity;
	}

	// getters and setters
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public DroneModel getModel() {
		return model;
	}

	public void setModel(DroneModel model) {
		this.model = model;
	}

	public int getWeightLimit() {
		return weightLimit;
	}

	public void setWeightLimit(int weightLimit) {
		this.weightLimit = weightLimit;
	}

	public int getBatteryCapacity() {
		return batteryCapacity;
	}

	public void setBatteryCapacity(int batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}

	public DroneState getState() {
		return state;
	}

	public void setState(DroneState state) {
		this.state = state;
	}

	public List<Medication> getMedications() {
		return medications;
	}

	public void setMedications(List<Medication> medications) {
		this.medications = medications;
	}
}