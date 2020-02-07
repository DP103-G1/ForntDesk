package com.example.ezeats.member;

public class Member {

	private String member_Id;
	private String account;
	private String password;
	private String name;
	private String phone;
	

	public Member(String member_Id, String account, String password, String name, String phone) {
		this.member_Id = member_Id;
		this.account = account;
		this.password = password;
		this.name = name;
		this.phone = phone;
	}
	
	public Member(String account, String password, String name, String phone) {
		this(null, account, password, name, phone);
	}

	public String getmember_Id() {
		return member_Id;
	}

	public void setmember_Id(String member_Id) {
		this.member_Id = member_Id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getpassword() {
		return password;
	}

	public void setpassword(String password) {
		this.password = password;
	}

	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}
	
	public String getphone() {
		return phone;
	}

	public void setphone(String phone) {
		this.phone = phone;
	}
	
}
