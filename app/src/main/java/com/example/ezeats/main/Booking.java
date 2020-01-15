package com.example.ezeats.main;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {

	private String bkTime;
	private Date bkDate;
	private String bkChild;
	private String bkAdult;
	private String bkPhone;

public Booking(
		String bkTime, Date bkDate, String bkChild, String bkAdult,
		String bkPhone) {
	super();

	this.bkTime = bkTime;
	this.bkDate = bkDate;
	this.bkChild = bkChild;
	this.bkAdult = bkAdult;
	this.bkPhone = bkPhone;
}



	@NonNull
	@Override
	public String toString() {
		return "Booking [bkTime=" + bkTime + ", bkDate=" + bkDate + ", bkChild=" + bkChild + ", bkAdult=" + bkAdult + ", bkPhone=" + bkPhone + "]";
	}









public String getBkTime() {
	return bkTime;
}

public void setBkTime(String bkTime) {
	this.bkTime = bkTime;
}

public Date getBkDate() {
	return bkDate;
}

public void setBkDate(Date bkDate) {
	this.bkDate = bkDate;
}

public String getBkChild() {
	return bkChild;
}

public void setBkChild(String bkChild) {
	this.bkChild = bkChild;
}

public String getBkAdult() {
	return bkAdult;
}

public void setBkAdult(String bkAdult) {
	this.bkAdult = bkAdult;
}

public String getBkPhone() {
	return bkPhone;
}

public void setBkPhone(String bkPhone) {
	this.bkPhone = bkPhone;
}


}
