package com.transportervendor.beans;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Vehicle {
	@SerializedName("vehicleId")
	@Expose
	private String vehicleId;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("count")
	@Expose
	private String count;
	@SerializedName("imageUrl")
	@Expose
	private String imageUrl;

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Vehicle(String vehicleId, String name, String count, String imageUrl) {
		super();
		this.vehicleId = vehicleId;
		this.name = name;
		this.count = count;
		this.imageUrl = imageUrl;
	}

	public Vehicle() {
		super();
	}
	
}
