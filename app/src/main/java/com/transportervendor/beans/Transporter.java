package com.transportervendor.beans;

import java.util.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transporter {

	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("transporterId")
	@Expose
	private String transporterId;
	@SerializedName("type")
	@Expose
	private String type;
	@SerializedName("image")
	@Expose
	private String image;
	@SerializedName("contactNumber")
	@Expose
	private String contactNumber;
	@SerializedName("address")
	@Expose
	private String address;
	@SerializedName("aadharCardNumber")
	@Expose
	private String aadharCardNumber;
	@SerializedName("gstNo")
	@Expose
	private String gstNo;
	@SerializedName("rating")
	@Expose
	private String rating;
	@SerializedName("token")
	@Expose
	private String token;


	public Transporter() {
	}
	public Transporter(String name, String transporterId, String type, String image, String contactNumber, String address, String aadharCardNumber, String gstNo, String rating, String token) {
		super();
		this.name = name;
		this.transporterId = transporterId;
		this.type = type;
		this.image = image;
		this.contactNumber = contactNumber;
		this.address = address;
		this.aadharCardNumber = aadharCardNumber;
		this.gstNo = gstNo;
		this.rating = rating;
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTransporterId() {
		return transporterId;
	}

	public void setTransporterId(String transporterId) {
		this.transporterId = transporterId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAadharCardNumber() {
		return aadharCardNumber;
	}

	public void setAadharCardNumber(String aadharCardNumber) {
		this.aadharCardNumber = aadharCardNumber;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}