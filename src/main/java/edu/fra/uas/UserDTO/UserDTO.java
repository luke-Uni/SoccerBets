package edu.fra.uas.UserDTO;

import org.springframework.hateoas.RepresentationModel;

import edu.fra.uas.User.User;

public class UserDTO extends RepresentationModel<UserDTO> {

	private String username;
	private double balance;

	public UserDTO(User user) {
		super();
		this.username = user.getUsername();
		this.balance = user.getBalance();
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
		return " [ " + username + " , " + balance + " ]";
	}

}
