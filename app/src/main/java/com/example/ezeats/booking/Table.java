package com.example.ezeats.booking;


public class Table {
	
	private String tableId;
	private String tablePeople;
	private int tableStatus;
	
	
	public Table(String tableId, String tablePeople, int tableStatus) {
		super();
		this.tableId = tableId;
		this.tablePeople = tablePeople;
		this.tableStatus = tableStatus;
		
	}


	
	@Override
	public String toString() {
		String text = "Table Id" + tableId + "\nTable People" + tablePeople ;
		return text;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.tableId.equals(((Table)obj).tableId);
	}
	
	@Override
	public int hashCode() {
		return tableId.hashCode();
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTablePeople() {
		return tablePeople;
	}

	public void setTablePeople(String tablePeople) {
		this.tablePeople = tablePeople;
	}

	public int getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(int tableStatus) {
		this.tableStatus = tableStatus;
	}

	
	
	
}
