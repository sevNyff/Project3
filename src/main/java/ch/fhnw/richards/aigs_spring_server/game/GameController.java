package ch.fhnw.richards.aigs_spring_server.game;

import java.lang.reflect.InvocationTargetException;
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

import ch.fhnw.richards.aigs_spring_server.gameEngines.GameEngine;
import ch.fhnw.richards.aigs_spring_server.user.User;
import ch.fhnw.richards.aigs_spring_server.user.UserRepository;
import ch.fhnw.richards.aigs_spring_server.utility.Token;

@RestController
public class GameController {
	private static GameRepository repository;

	GameController(GameRepository repository) {
		this.repository = repository;
	}

	// User creates a new game. We accept user-inputs as a game object, even though
	// no game exists yet. The game-engine takes the difficulty and options from
	// this, create a new game object, and saves this in the repository.
	@PostMapping("/game/new")
	Game newGame(@RequestBody Game game) {
		// Check the token for validity
		String token = game.getToken();
		if (Token.validate(token)) {
			// Only allow one game - if a game already exists, we cannot create a new one
			Game oldGame = repository.findById(token).get();
			if (oldGame == null) {

				// Get game engine
				GameEngine ge = GameEngine.getGameEngine(game.getGameType());
				if (ge != null) {
					game = ge.newGame(game);
					return repository.save(game);
				} else {
					throw new GameException("Invalid game type");
				}
			} else {
				throw new GameException("Game already running for this user");
			}

		} else {
			throw new GameException("Invalid token");
		}
	}

	// User quits a game
	@PostMapping("/game/quit")
	Game quitGame(@RequestBody Game game) {
		// We only care about the token
		if (Token.validate(game.getToken())) {
			repository.delete(game);
			game.setResult(true); // Game is over
			return game;
		} else {
			throw new GameException("Invalid token");
		}
	}

	// User makes a move - the format differs by game, so we just pass a map to the
	// game engine
	@PostMapping("/game/move")
	Game gameMove(@RequestBody String json) throws JsonProcessingException {
		// Convert incoming JSON to a map, then fetch the value of the token-property
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> map = new HashMap<>();
		map = mapper.readValue(json, HashMap.class);

		// Check the token for validity
		String token = map.get("token");
		if (Token.validate(token)) {
			// Get game object
			Game game = repository.findById(token).get();
			if (game != null) {
				// Get game engine
				GameEngine ge = GameEngine.getGameEngine(game.getGameType());
				if (ge != null) {
					// Pass map, get back updated game (or null, if move was invalid)
					game = ge.move(game, map);
					if (game != null) {
						return repository.save(game);
					} else {
						throw new GameException("Invalid move");
					}
				} else {
					throw new GameException("Invalid game type");
				}

			} else {
				throw new GameException("Game not found");
			}
		} else {
			throw new GameException("Invalid token");
		}
	}

	// --- The following methods are for debugging - a real web service would not
	// offer them ---

	// List all Games
	@GetMapping("/games")
	List<Game> all() {
		return repository.findAll();
	}

	// ||Game data needs checked from various places
	public static GameRepository getRepository() {
		return repository;
	}

}
