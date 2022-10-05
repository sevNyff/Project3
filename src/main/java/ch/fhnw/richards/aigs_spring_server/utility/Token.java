package ch.fhnw.richards.aigs_spring_server.utility;

import java.time.LocalDateTime;
import java.util.List;

import ch.fhnw.richards.aigs_spring_server.user.User;
import ch.fhnw.richards.aigs_spring_server.user.UserController;
import ch.fhnw.richards.aigs_spring_server.user.UserRepository;

/**
 * A class that provides helper functions for dealing with tokens
 */
public class Token {

	/**
	 * Create a token. A token is just a large, secret number.
	 */
	public static String generate() {
		long tokenAsLong = (long) (Math.random() * Long.MAX_VALUE);
		return Long.toHexString(tokenAsLong);
	}

	public static boolean validate(String token) {
		boolean valid = false;
		UserRepository repository = UserController.getRepository();
		try {
			System.out.println("Incoming token: " + token);
			Long.parseLong(token, 16);
			System.out.println("Token parsed");

			List<User> users = repository.findByToken(token); // Should be only one
			if (users.size() > 0) {
				System.out.println("User found");
				LocalDateTime expiry = users.get(0).getTokenExpiry();
				System.out.println("Token expiry is " + expiry);
				valid = expiry.compareTo(LocalDateTime.now()) > 0;
			}

		} catch (NumberFormatException e) {
		}
		System.out.println("Valid = " + valid);
		return valid;
	}
}
