package com.example.ezeats.order;

public class Card {
	
	private int member_id;
	
	private String member_name;
	
	private String card_number;
	
	private String card_date;
	
	private int card_last;
	
	public Card(int member_id, String member_name, String card_number, String card_date, int card_last) {
		super();
		this.member_id = member_id;
		this.member_name = member_name;
		this.card_number = card_number;
		this.card_date = card_date;
		this.card_last = card_last;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getCard_date() {
		return card_date;
	}

	public void setCard_date(String card_date) {
		this.card_date = card_date;
	}

	public int getCard_last() {
		return card_last;
	}

	public void setCard_last(int card_last) {
		this.card_last = card_last;
	}
}
