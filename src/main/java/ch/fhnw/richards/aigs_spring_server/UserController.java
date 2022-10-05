package ch.fhnw.richards.aigs_spring_server;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	private final UserRepository repository;
	
	UserController(UserRepository repository) {
		this.repository = repository;
	}


	// List all Users -- DEBUG ONLY
	@GetMapping("/users")
	List<User> all() {
		return repository.findAll();
	}

	// Register a User
	@PostMapping("/users/register")
	User registerUser(@RequestBody User User) {
		Optional<User> oldUser = repository.findById(User.getUserName());
		if (oldUser.isEmpty()) {
			User.setUserExpiry(LocalDateTime.now().plusDays(7));
			return repository.save(User);
		} else {
			throw new UserException("'" + User.getUserName() + "' already exists" );
		}
	}

	// Login a User
	@PostMapping("/users/login")
	User loginUser(@RequestBody User User) {
		Optional<User> oldUser = repository.findById(User.getUserName());
		if (oldUser.isPresent() && oldUser.get().getPassword().equals(User.getPassword())) {
			User.setUserExpiry(LocalDateTime.now().plusDays(7));
			User.setToken(generateToken());
			User.setTokenExpiry(LocalDateTime.now().plusDays(1));
			return repository.save(User);
		} else {
			throw new UserException("Wrong password for user '" + User.getUserName() + "'" );
		}
	}

	// Logout a User
	@PostMapping("/users/logout")
	User logoutUser(@RequestBody User User) {
		Optional<User> oldUser = repository.findById(User.getUserName());
		if (oldUser.isPresent()) {
			User thisUser = oldUser.get();
			thisUser.setToken(null);
			thisUser.setTokenExpiry(null);
			return repository.save(thisUser);
		} else {
			throw new UserException("\"" + User.getUserName() + "\" does not exist" );
		}
	}

	// Read a User -- DEBUG ONLY
	@GetMapping("users/{userName}")
	User one(@PathVariable String userName) {
		return repository.findById(userName).orElseThrow(() -> new UserException("\"" + userName + "\" does not exist" ));
	}
	
	// Create a token - a token is just a large, secret number.
	// It is only valid for this one login session.
	private String generateToken() {
		long tokenAsLong = (long) (Math.random() * Long.MAX_VALUE);
		return Long.toHexString(tokenAsLong);
	}

}
