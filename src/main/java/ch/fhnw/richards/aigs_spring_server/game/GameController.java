package ch.fhnw.richards.aigs_spring_server.game;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.richards.aigs_spring_server.utility.Token;

@RestController
public class GameController {
	private final GameRepository repository;
	
	GameController(GameRepository repository) {
		this.repository = repository;
	}
	
	// User creates a new game
	@PostMapping("/game/new")
	Game newGame(@RequestBody Game game) {
		// First, check that the token is valid
		if (Token.validate(game.getToken())) {
			return repository.save(game);
		} else {
			throw new GameException("Invalid token");
		}
	}

}
