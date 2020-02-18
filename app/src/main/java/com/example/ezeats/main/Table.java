package com.example.ezeats.main;


public class Table {
	
	private int tableId;
	private String tablePeople;
	private int tableStatus;
	
	
	public Table(int tableId, String tablePeople, int tableStatus) {
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
		return this.tableId == tableId;
	}
	
	@Override
	public int hashCode() {
		return tableId;
	}

	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
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
