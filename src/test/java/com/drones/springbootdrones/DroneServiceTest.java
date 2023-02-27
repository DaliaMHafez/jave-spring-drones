package com.drones.springbootdrones;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.drones.springbootdrones.enums.DroneState;
import com.drones.springbootdrones.exceptions.ResourceNotFoundException;
import com.drones.springbootdrones.model.Drone;
import com.drones.springbootdrones.model.Medication;
import com.drones.springbootdrones.service.DroneService;
import com.drones.springbootdrones.service.DroneServiceImpl;

@SpringJUnitConfig
@SpringBootTest
public class DroneServiceTest {

	@Autowired
	private DroneService droneService;

	@BeforeEach
	void setUp() {
		droneService = new DroneServiceImpl();
	}

	@Test
	void testGetAllDrones() {
		List<Drone> drones = droneService.getAllDrones();

		assertNotNull(drones);
		assertTrue(drones.isEmpty());

		Drone drone1 = new Drone("SN001", DroneState.IDLE, 100, 90);
		Drone drone2 = new Drone("SN002", DroneState.IDLE, 200, 90);
		droneService.registerDrone(drone1);
		droneService.registerDrone(drone2);

		drones = droneService.getAllDrones();

		assertEquals(2, drones.size());
		assertTrue(drones.contains(drone1));
		assertTrue(drones.contains(drone2));
	}

	@Test
	void testGetAvailableDrones() {
		Drone drone1 = new Drone("1234", DroneState.IDLE, 100, 100);
		Drone drone2 = new Drone("5678", DroneState.LOADED, 200, 100);
		Drone drone3 = new Drone("9012", DroneState.DELIVERING, 450, 80);

		List<Drone> expectedAvailableDrones = new ArrayList<>();
		expectedAvailableDrones.add(drone1);

		droneService.registerDrone(drone1);
		droneService.registerDrone(drone2);
		droneService.registerDrone(drone3);

		List<Drone> availableDrones = droneService.getAvailableDrones();

		assertNotNull(availableDrones);
		assertEquals(expectedAvailableDrones.size(), availableDrones.size());
		assertEquals(expectedAvailableDrones.get(0).getSerialNumber(), availableDrones.get(0).getSerialNumber());

		Drone drone4 = new Drone("SN004", DroneState.LOADED, 100, 100);
		droneService.registerDrone(drone4);
		availableDrones = droneService.getAvailableDrones();

		assertFalse(availableDrones.contains(drone4));
	}

	@Test
	void testRegisterDrone() {
		Drone drone = new Drone("1234", DroneState.IDLE, 500, 90);

		Drone registeredDrone = droneService.registerDrone(drone);

		assertNotNull(registeredDrone);
		assertEquals(drone.getSerialNumber(), registeredDrone.getSerialNumber());
	}

	@Test
	void testLoadMedications() {
		Drone drone = new Drone("1234", DroneState.IDLE, 500, 100);
		droneService.registerDrone(drone);

		Medication medication1 = new Medication("Medication 1", 10, "C_1");
		Medication medication2 = new Medication("Medication 2", 20, "C_2");
		List<Medication> medications = new ArrayList<>();
		medications.add(medication1);
		medications.add(medication2);

		Drone loadedDrone = droneService.loadMedications(drone.getSerialNumber(), medications);

		assertNotNull(loadedDrone);
		assertEquals(drone.getSerialNumber(), loadedDrone.getSerialNumber());
		assertEquals(medications.size(), loadedDrone.getMedications().size());
		assertEquals(DroneState.LOADED, loadedDrone.getState());
	}

	@Test
	void testLoadMedicationsThrowsIllegalStateExceptionWhenDroneNotIdle() {
		Drone drone = new Drone("SN001", DroneState.LOADED, 300, 100);
		droneService.registerDrone(drone);
		List<Medication> medications = new ArrayList<>();

		assertThrows(IllegalStateException.class, () -> droneService.loadMedications("SN001", medications));
	}

	@Test
	void testLoadMedicationsThrowsResourceNotFoundExceptionWhenSerialNumberNotFound() {
		List<Medication> medications = new ArrayList<>();
		medications.add(new Medication("Medication1", 10, "C_1"));
		medications.add(new Medication("Medication2", 20, "C_2"));
		String invalidSerialNumber = "invalidSerialNumber";

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			droneService.loadMedications(invalidSerialNumber, medications);
		});
	}

	@Test
	void testGetLoadedMedicationsReturnsEmptyListWhenDroneNotLoaded() {
		String serialNumber = "123";
		Drone drone = new Drone(serialNumber, DroneState.IDLE, 1000, 100);
		droneService.registerDrone(drone);

		assertThrows(IllegalStateException.class, () -> droneService.getLoadedMedications(serialNumber));
	}

	@Test
	void testGetLoadedMedicationsReturnsMedicationsWhenDroneLoaded() {
		String serialNumber = "123";
		Drone drone = new Drone(serialNumber, DroneState.LOADED, 1000, 90);
		List<Medication> medications = new ArrayList<>();
		medications.add(new Medication("Medication 1", 10, "C_1"));
		medications.add(new Medication("Medication 2", 20, "C_2"));
		drone.setMedications(medications);
		droneService.registerDrone(drone);
		List<Medication> loadedMedications = droneService.getLoadedMedications(serialNumber);

		assertEquals(medications, loadedMedications);
	}

	@Test
	void testGetLoadedMedicationsThrowsIllegalStateExceptionWhenDroneNotLoadedOrDelivering() {
		String serialNumber = "123";
		Drone drone = new Drone(serialNumber, DroneState.IDLE, 1000, 90);
		droneService.registerDrone(drone);

		assertThrows(IllegalStateException.class, () -> droneService.getLoadedMedications(serialNumber));
	}

	@Test
	public void testGetBatteryLevelReturnsBatteryLevel() {
		Drone drone = new Drone("1234", DroneState.IDLE, 1000, 90);
		droneService.registerDrone(drone);
		int batteryLevel = droneService.getBatteryLevel("1234");
		assertEquals(90, batteryLevel);
	}

	@Test
	public void testGetBatteryLevelThrowsResourceNotFoundExceptionWhenDroneNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> {
			droneService.getBatteryLevel("5678");
		});
	}
}
