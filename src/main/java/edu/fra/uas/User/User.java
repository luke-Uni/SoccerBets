package edu.fra.uas.User;

import com.fasterxml.jackson.annotation.JsonCreator;

public class User {

	private String username;

	private double balance;

	@JsonCreator
	public User() {
		this.balance = 500;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "User " + username + " Balance= " + balance;
	}

}
