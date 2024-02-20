package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CommandDemo;
import demo.Loaders_2024_Check;
import structures.GameState;
import structures.basic.Card;
import structures.basic.CardWrapper;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.TileWrapper;
import structures.basic.Unit;
import structures.basic.UnitWrapper;
import utils.BasicObjectBuilders;
import utils.OrderedCardLoader;
import utils.StaticConfFiles;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Initalize implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		initializeGameState(gameState);

		renderHand(out, gameState);
		Unit playerAvatar = setPlayerAvatarFrontend(out, gameState);
		Player humanPlayer = setPlayerAvatarBackend(gameState, playerAvatar);
		setPlayerHealthAndMana(out, humanPlayer);

		Unit  aiAvatar = setAiAvatarFrontend(out, gameState);
		Player aiPlayer = setAiAvatarBackend(gameState, aiAvatar);
		setAIHealthAndMana(out, aiPlayer);
		// highlightBoard(out, gameState);

		// CommandDemo.executeDemo(out); // this executes the command demo, comment out this when implementing your solution
	}

	public void initializeGameState(GameState gameState) {
		gameState.initializeGame();
	}

	public void setPlayerHealthAndMana(ActorRef out, Player player) {
		BasicCommands.setPlayer1Health(out, player);
		BasicCommands.setPlayer1Mana(out, player);
	}

	public void setAIHealthAndMana(ActorRef out, Player player) {
		BasicCommands.setPlayer2Health(out, player);
		BasicCommands.setPlayer2Mana(out, player);
	}

	public void renderHand(ActorRef out, GameState gameState) {
		for (int iteration = 0; iteration < 3; iteration++) {
			int handPosition = gameState.getPlayerDeck().getTopCardIndex() + 1;
			for (Card card : OrderedCardLoader.getPlayer1Cards(1)) {
				BasicCommands.drawCard(out, card, handPosition, 1);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handPosition++;
	
				if (handPosition == 4) {
					break;
				}
			}
		}
	}

	public void highlightBoard(ActorRef out, GameState gameState) {
		TileWrapper[][] board = gameState.getBoard().getBoard();

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 5; j++) {
				Tile tile = board[i][j].getTile();
				BasicCommands.drawTile(out, tile, 1);
			}
		}
	}

	public Player setPlayerAvatarBackend(GameState gameState, Unit unit) {
		Player player = gameState.getPlayer();
		UnitWrapper unitWrapper = new UnitWrapper(unit, "Player", 20, 2, player, null);

		TileWrapper[][] board = gameState.getBoard().getBoard();
		TileWrapper tileWrapper = board[1][2];
		tileWrapper.setUnitWrapper(unitWrapper);

		return player;
	}

	public Unit setPlayerAvatarFrontend(ActorRef out, GameState gameState) {
		TileWrapper[][] board = gameState.getBoard().getBoard();
		Tile tile = board[1][2].getTile();
		Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
		unit.setPositionByTile(tile); 
		BasicCommands.drawUnit(out, unit, tile);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

		BasicCommands.setUnitAttack(out, unit, 2);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

		BasicCommands.setUnitHealth(out, unit, 20);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

	
		return unit;
	}

	public Player setAiAvatarBackend(GameState gameState, Unit unit) {
		Player player = gameState.getPlayer();
		UnitWrapper unitWrapper = new UnitWrapper(unit, "AI", 20, 2, player, null);

		TileWrapper[][] board = gameState.getBoard().getBoard();
		TileWrapper tileWrapper = board[7][2];
		tileWrapper.setUnitWrapper(unitWrapper);

		return player;
	}

	public Unit setAiAvatarFrontend (ActorRef out, GameState gameState) {
		TileWrapper[][] board = gameState.getBoard().getBoard();
		Tile tile = board[7][2].getTile();
		Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 0, Unit.class);
		unit.setPositionByTile(tile); 
		BasicCommands.drawUnit(out, unit, tile);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

		BasicCommands.setUnitAttack(out, unit, 2);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

		BasicCommands.setUnitHealth(out, unit, 20);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

		return unit;
	}

	public void setPlayerHealth(ActorRef out, Player player) {
		BasicCommands.setPlayer1Health(out, player);
	}
	
}


