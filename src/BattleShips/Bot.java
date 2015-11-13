package BattleShips;

import java.util.Random;
import java.util.Stack;

/**
 * A bot with a simple AI.
 * Whenever a players has shot at it's board
 * the bot also shoots. The bot class also places ships on the
 * the board for the player to try hitting.
 * The bot shoots at a random position but if it hits
 * the bot shoots around the hit in order to locate the ship.
 */
public class Bot {

	private Game game;
	private Board compBoard;
	private Board playerBoard;
	private Random random;
	private Ship[] ships;
	private char[][] used;
	private Stack<Position> stack;

	public Bot(Game _game, Board _compBoard, Board _playerBoard, Ship[] _ships) {
		compBoard = _compBoard;
		playerBoard = _playerBoard;
		ships = _ships;
		game = _game;
		random = new Random();
		used = new char[10][10];
		stack = new Stack<Position>();
	}

	/**
	 * Placing the bots ships in a Random valid position on the board
	 */
	public void placeShips() {
		char randRow, randCol, randAlign;
		Ship newShip;

		for(int i = 0; i < ships.length; i++) {
			newShip = ships[i];
			do{
				randRow = (char)(random.nextInt(10) + 65);
				randCol = (char)(random.nextInt(10) + 48);
				randAlign = (random.nextInt(2) == 0) ? 'V' : 'H';
				newShip.setPosition(randRow, randCol, randAlign);
			} while(!(compBoard.noOverlap(newShip) && newShip.isInBoundary()));

			compBoard.addShipToBoard(game, newShip);
			compBoard.makeGraphicBoard();
		}
	}

	/**
	 * Call when you wan to make the bot shoot on the players board
	 */
	public void shoot() {
		char[][] board_copy = playerBoard.getBoard();
		int randRow, randCol;

		if(!stack.isEmpty()){
			Position tempPos = stack.pop();
			int row = tempPos.row;
			int col = tempPos.col;
			char dir = tempPos.direction;

			// We have a new hit!
			// Depending on which direction we are going we want to
			// add new positions to try shoot at to the queue
			// For example if we are going south we want to keep shoot
			// until we get a miss and then try another direction
			if(board_copy[row][col] != '-') {
				playerBoard.getButtons()[row][col].botShot();
				switch (dir) {
				case 'S':
					if(row != 9)
						stack.add(new Position(row+1, col, dir));
					break;
				case 'N':
					if(row != 0)
						stack.add(new Position(row-1, col, dir));
					break;
				case 'E':
					if(col != 9)
						stack.add(new Position(row, col+1, dir));
					break;
				case 'W':
					if(col != 0)
						stack.add(new Position(row, col-1, dir));
					break;
				default:
					System.out.println("ERROR, try shot at close hit");
					break;
				}
				used[row][col] = 'X';
			} else {
				playerBoard.getButtons()[row][col].botShot();
				used[row][col] = '-';
			}
		} else {
			// Generate random shot that has not been used before
			do {
				randRow = random.nextInt(10);
				randCol = random.nextInt(10);
			}while(used[randRow][randCol] != 0);

			// It's a HIT!
			if(board_copy[randRow][randCol] != '-') {
				playerBoard.getButtons()[randRow][randCol].botShot();
				initStack(randRow, randCol);
				used[randRow][randCol] = 'X';
			}
			// It's a miss
			else { 
				playerBoard.getButtons()[randRow][randCol].botShot();
				used[randRow][randCol] = '-';
			}
		}
	}

	/**
	 * Adds the +1 rows and columsn from a random hit to be shot around.
	 * @param hitRow row we hit
	 * @param hitCol col we hit
	 */
	private void initStack(int hitRow, int hitCol) {
		if(hitRow != 9 && used[hitRow+1][hitCol] == 0) {
			stack.add(new Position(hitRow + 1, hitCol, 'S'));
		}
		if(hitRow != 0 && used[hitRow-1][hitCol] == 0) {
			stack.add(new Position(hitRow - 1, hitCol, 'N'));
		}
		if(hitCol != 9 && used[hitRow][hitCol+1] == 0) {
			stack.add(new Position(hitRow, hitCol + 1, 'E'));
		}
		if(hitCol != 0 && used[hitRow][hitCol-1] == 0) {
			stack.add(new Position(hitRow, hitCol - 1, 'W'));
		}
	}

	/**
	 * Clear the stack
	 */
	public void clearStack() {
		stack.clear();
	}

}

class Position {
	int row;
	int col;
	char direction;

	public Position(int row, int col, char direction) {
		this.row = row;
		this.col = col;
		this.direction = direction;
	}
}
