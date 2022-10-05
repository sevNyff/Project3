package ch.fhnw.richards.aigs_spring_server.user;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.fhnw.richards.aigs_spring_server.utility.Token;

@RestController
public class UserController {
	private static UserRepository repository;

	UserController(UserRepository repository) {
		UserController.repository = repository;
	}

	// Register a User
	@PostMapping("/users/register")
	User registerUser(@RequestBody User User) {
		Optional<User> oldUser = repository.findById(User.getUserName());
		if (oldUser.isEmpty()) {
			User.setUserExpiry(LocalDateTime.now().plusDays(7));
			return repository.save(User);
		} else {
			throw new UserException("'" + User.getUserName() + "' already exists");
		}
	}

	// Login a User
	// Passwords should be encrypted on the client. For testing, we can omit
	// encryption. It doesn't matter to the server.
	@PostMapping("/users/login")
	User loginUser(@RequestBody User User) {
		Optional<User> oldUser = repository.findById(User.getUserName());
		if (oldUser.isPresent() && oldUser.get().getPassword().equals(User.getPassword())) {
			User.setUserExpiry(LocalDateTime.now().plusDays(7));
			User.setToken(Token.generate());
			User.setTokenExpiry(LocalDateTime.now().plusDays(1));
			return repository.save(User);
		} else {
			throw new UserException("Wrong password for user '" + User.getUserName() + "'");
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
			throw new UserException("\"" + User.getUserName() + "\" does not exist");
		}
	}

	// --- The following methods are for debugging - a real web service would not
	// offer them ---

	// List all Users
	@GetMapping("/users")
	List<User> all() {
		return repository.findAll();
	}

	// Read a User
	@GetMapping("users/{userName}")
	User one(@PathVariable String userName) {
		return repository.findById(userName)
				.orElseThrow(() -> new UserException("\"" + userName + "\" does not exist"));
	}

	// Ping the server, hoping for a response
	@GetMapping("/ping")
	String ping() {
		return "{ \"ping\":\"success\" }";
	}

	// Ping the server, test validity of a token
	@PostMapping("/ping")
	String pingToken(@RequestBody String json) throws JsonProcessingException {
		// Convert incoming JSON to a map, then fetch the value of the token-property
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> map = new HashMap<>();
		map = mapper.readValue(json, HashMap.class);

		// Check the token for validity
		return (Token.validate(map.get("token"))) ? "{ \"ping\":\"success\" }" : "{ \"ping\":\"failure\" }";
	}

	// User data needs checked from various places
	public static UserRepository getRepository() {
		return repository;
	}
}
