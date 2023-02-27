package com.drones.springbootdrones.model;

import jakarta.validation.constraints.Pattern;

public class Medication {
	@Pattern(regexp = "^[\\w-]+$", message = "Name can only contain letters, numbers, '-' and '_'")
	private String name;

	private int weight;

	@Pattern(regexp = "^[A-Z_\\d]+$", message = "Code can only contain upper case letters, underscore and numbers")
	private String code;

	private String image;

	public Medication(String name, int weight, String code) {
		this.name = name;
		this.weight = weight;
		this.code = code;
	}

	// getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}