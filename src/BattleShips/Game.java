package BattleShips;

import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Game {
	/* Here you can customize what ship you want to have in your game
	 * A = Aircraft carrier
	 * B = Battleship
	 * D = Destroyer
	 * S = Submarine
	 * P = Patrol boat */
	private Ship[] playerShips = new Ship[]{new Ship('A'), new Ship('B'), new Ship('D'), new Ship('S'), new Ship('P')};
	private Ship[] compShips = new Ship[]{new Ship('A'), new Ship('B'), new Ship('D'), new Ship('S'), new Ship('P')};

	private Window window;
	private Board playerBoard;
	private Board compBoard;
	private Stats stats;
	
	private int count;
	private int playerSunkShips;
	private int playerNumOfShips;
	private int compSunkShips;
	private int compNumOfShips;

	private Bot bot;

	private String userName;
	private String placedShips = "";
	private String boardChart = "";

	public Game() {
		count = 1; 		// For printing ship number.
		playerSunkShips = 0;
		compSunkShips = 0;
		playerNumOfShips = playerShips.length;
		compNumOfShips = compShips.length;
		stats = new Stats();
		playerBoard = new Board();
		compBoard = new Board();
		bot = new Bot(this, compBoard, playerBoard, compShips);
	}

	/**
	 * PLAY THE GAME!!
	 */
	private void play() {
		bot.placeShips();
		userName = askForPlayerName();
		stats.setUsername(userName);
		stats.loadHighScore();
		for (int i = 0; i < playerShips.length; i++) {
			askForShips(playerShips[i]);			
		}

		window = new Window(1200, 700);
		window.addPlayerBoard(playerBoard);
		window.addCompBoard(compBoard);
		window.addStats(stats);

		playerBoard.fillBoard(this);
		compBoard.fillBoard(this);
		playerBoard.disableAllButtons();

	}

	/**
	 * Prints out a frame which asks for the players name
	 * @return String with the username
	 */
	private String askForPlayerName() {
		String userName;
		do {
			userName = JOptionPane.showInputDialog("Hello fellow player!\nType your awesome username");
		} while (userName.isEmpty());
		return userName;
	}

	/**
	 * Asking for the ship in the arguemnt with graphical input and checks if the input is valid
	 * Then adds ship to the board
	 * @param ship - Ship to be placed
	 */
	private void askForShips(Ship ship) {
		char row, col, align;
		String lines = "";

		while(true) {
			// Nice little line to separate the already added ships and the input
			if(count != 1) {
				lines = "------\n";				
			}

			// Takes input to place ship. Syntax is "A5V" to place at A5 in vertical position
			String input = JOptionPane.showInputDialog(null,
					boardChart + "\n" + placedShips + lines +
					"Commander, Please input the location for "+ ship.getName() +
					"?\nEnter row[A-J], column[0-9] and position[H or V], e.g. [A1H]",
					"Input",
					JOptionPane.PLAIN_MESSAGE);

			// Needs to follow syntax for input
			if(input.length() != 3) {
				JOptionPane.showMessageDialog(null, "Error, invalid length of input. Try again!");
				continue;
			}

			// Divides the three parts to chech them for correct syntax
			row = input.charAt(0);
			col = input.charAt(1);
			align = input.charAt(2);

			// Checks if the input has wrong syntax for the row
			if (row < 65 || row > 74 ) {
				JOptionPane.showMessageDialog(null, "Error, invalid row. Try again!", "Error", JOptionPane.ERROR_MESSAGE);
				continue;
			}

			// Checks if the input has wrong syntax for the column
			if(col < 48 || col > 57) {
				JOptionPane.showMessageDialog(null, "Error, invalid col. Try again!", "Error", JOptionPane.ERROR_MESSAGE);
				continue;
			}

			// Checks if the input has wrong syntax for the aligntment
			if (align != 'H' && align != 'V') {
				JOptionPane.showMessageDialog(null, "Error, invalid alignment. Try again!", "Error", JOptionPane.ERROR_MESSAGE);
				continue;
			}
			ship.setPosition(row, col, align);

			if(playerBoard.noOverlap(ship) && ship.isInBoundary()) {
				playerBoard.addShipToBoard(this, ship);
				placedShips += count + ": "+ship.getName()+ " at row " + row + ", col " +col+ ", position " + align+"\n";
				count++;
				boardChart = playerBoard.makeGraphicBoard();
				break;
			}
			else {
				JOptionPane.showMessageDialog(null, "Error, invalid input, the ship is either overlapping or out of bounds",
						"Error", JOptionPane.ERROR_MESSAGE);
				continue;
			}

		}
	}


	/**
	 * Chechs if all the ships are sunk or if the player ran out of shots and if any case is true, notice the player and quit the game
	 */
	public void checkGameStatus() {
		if(playerSunkShips == playerNumOfShips) {
			stats.saveHighScore(userName);
			JOptionPane.showMessageDialog(null, "Congratulations!\nYou won the game.");
			window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
		}
		if(compSunkShips == compNumOfShips) {
			JOptionPane.showMessageDialog(null, "Sorry, you lost the game!");
			window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	/**
	 * Increase number of ships sunk by the player
	 */
	public void playerShipHasBeenSunk() {
		playerSunkShips++;
	}
	
	/**
	 * Increase number of ships sunk by the bot
	 */
	public void compShipHasBeenSunk() {
		compSunkShips++;
	}
	
	public Stats getStats() {
		return stats;
	}

	public Bot getBot() {
		return bot;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Game game = new Game();
				game.play();

			}
		});
	}

}
