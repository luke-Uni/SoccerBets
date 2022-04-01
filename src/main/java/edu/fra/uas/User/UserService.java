package edu.fra.uas.User;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.fra.uas.Bet.BetRepository;
import edu.fra.uas.Event.EventRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRep;

	@Autowired
	BetRepository betRep;

	@Autowired
	EventRepository eventRep;

	// Add the passed User if he is not a duplicate
	public void addUser(User user) {

		if (!userExists(user)) {
			userRep.allUser.add(user);

			System.out.println(user + " got added");
		}

	}

	public ArrayList<User> getUsers() {

		return userRep.allUser;

	}

	// A user with the passed name will be deleted
	// The return is important for the response
	public boolean deleteUser(String name) {

		for (int i = 0; i < userRep.allUser.size(); i++) {
			if (userRep.allUser.get(i).getUsername().equals(name)) {
				userRep.allUser.remove(i);
				System.out.println(name + " got deleted!");
				return true;

			}
		}

		return false;

	}

	public boolean userExists(User user) {

		for (int i = 0; i < userRep.allUser.size(); i++) {
			if (userRep.allUser.get(i).getUsername().equals(user.getUsername())) {
				System.out.println("Username already Exists");
				return true;
			}
		}

		return false;
	}

	public User getUser(String userName) {
		for (int i = 0; i < userRep.allUser.size(); i++) {
			if (userRep.allUser.get(i).getUsername().equals(userName)) {
				return userRep.allUser.get(i);
			}
		}
		return null;
	}

}
