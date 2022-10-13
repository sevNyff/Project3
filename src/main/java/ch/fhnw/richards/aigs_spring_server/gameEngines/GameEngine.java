package ch.fhnw.richards.aigs_spring_server.gameEngines;

import java.util.HashMap;

import ch.fhnw.richards.aigs_spring_server.game.Game;
import ch.fhnw.richards.aigs_spring_server.game.GameType;

public interface GameEngine {
	public static GameEngine getGameEngine(GameType gameType) {
		try {
			String packageName = GameEngine.class.getPackageName();
			String className = packageName + '.' + gameType.toString() + '.' + gameType.toString();
			Class<? extends GameEngine> c = (Class<? extends GameEngine>) Class.forName(className);
			GameEngine ge = c.getDeclaredConstructor().newInstance();
			return ge;
		} catch (Exception e) { // Lots of possible exceptions
			return null;
		}
	}
	
	// Create a new, empty game. We use the incoming object as a basis, but set "board" and "result"
	public abstract Game newGame(Game game);
	
	// Return an updated Game object, or null if the move was invalid
	public abstract Game move(Game game, HashMap<String,String> move);
}
