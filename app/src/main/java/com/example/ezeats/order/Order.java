package com.example.ezeats.order;

import java.util.List;

public class Order {

	private int ORD_ID;
	private String MEMBER_ID;
	private String TABLE_ID;
	private boolean TABLE_BELL;
	private int ORD_TOTAL;
	private boolean ORD_STATUS;
	private boolean ORD_BILL;
	private List<MenuDetail> menuDetails; 
	
	public Order(int ORD_ID, String MEMBER_ID, String TABLE_ID, boolean TABLE_BELL, int ORD_TOTAL,
			boolean ORD_STATUS, boolean ORD_BILL) {
		this.ORD_ID = ORD_ID;
		this.MEMBER_ID = MEMBER_ID;
		this.TABLE_ID = TABLE_ID;
		this.TABLE_BELL = TABLE_BELL;
		this.ORD_TOTAL = ORD_TOTAL;
		this.ORD_STATUS = ORD_STATUS;
		this.ORD_BILL = ORD_BILL;
	}

	public Order(int ORD_ID, String MEMBER_ID, String TABLE_ID, boolean TABLE_BELL, int ORD_TOTAL,
			boolean ORD_STATUS, boolean ORD_BILL, List<MenuDetail> menuDetails) {
		this.ORD_ID = ORD_ID;
		this.MEMBER_ID = MEMBER_ID;
		this.TABLE_ID = TABLE_ID;
		this.TABLE_BELL = TABLE_BELL;
		this.ORD_TOTAL = ORD_TOTAL;
		this.ORD_STATUS = ORD_STATUS;
		this.ORD_BILL = ORD_BILL;
		this.menuDetails = menuDetails;
	}
	
	public int getORD_ID() {
		return ORD_ID;
	}

	public void setORD_ID(int ord_id) {
		ORD_ID = ord_id;
	}

	public String getMEMBER_ID() {
		return MEMBER_ID;
	}

	public void setMEMBER_ID(String member_id) {
		MEMBER_ID = member_id;
	}

	public String getTABLE_ID() {
		return TABLE_ID;
	}

	public void setTABLE_ID(String table_id) {
		TABLE_ID = table_id;
	}

	public boolean isTABLE_BELL() {
		return TABLE_BELL;
	}

	public void setTABLE_BELL(boolean table_bell) {
		TABLE_BELL = table_bell;
	}

	public int getORD_TOTAL() {
		return ORD_TOTAL;
	}

	public void setORD_TOTAL(int ord_total) {
		ORD_TOTAL = ord_total;
	}

	public boolean isORD_STATUS() {
		return ORD_STATUS;
	}

	public void setORD_STATUS(boolean ord_status) {
		ORD_STATUS = ord_status;
	}

	public boolean isORD_BILL() {
		return ORD_BILL;
	}

	public void setORD_BILL(boolean ord_bill) {
		ORD_BILL = ord_bill;
	}
	
	public List<MenuDetail> getMenuDetails() {
		return menuDetails;
	}

	public void setMenuDetails(List<MenuDetail> menuDetails) {
		this.menuDetails = menuDetails;
	}


}
