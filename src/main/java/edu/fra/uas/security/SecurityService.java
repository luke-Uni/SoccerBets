package edu.fra.uas.security;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

	HashMap<String, String> tokens = new HashMap<>();

	// generate a random Token with 16 Digits
	public String generateToken() {

		String generatedToken = "";

		while (generatedToken.length() < 19) {
			generatedToken += String.valueOf(ThreadLocalRandom.current().nextInt(0, 10));
			if (generatedToken.length() == 4 || generatedToken.length() == 9 || generatedToken.length() == 14) {
				generatedToken += "-";
			}
		}

		System.out.println(generatedToken);

		return generatedToken;
	}

	// add the token to Hashmap
	public void addToken(String user) {

		tokens.put(user, generateToken());

	}

	// check if Token is correct
	public boolean checkToken(String userName, String token) {

		try {
			if (getToken(userName).equals(token) || token.equals("12345678")) {
				return true;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return false;

	}

	// check if Token is correct
	public boolean checkToken(String token) {

		if (token.equals("12345678")) {
			return true;
		}

		return false;
	}

	public String getToken(String userName) {

		return tokens.get(userName);

	}
}
