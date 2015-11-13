package BattleShips;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * Board contains 10x10 buttons in a gridlayout and displays and
 * contains all the buttons we are pressing in the game. 
 * It aligns all the buttons and checks for valid positions for 
 * the ship to be placed at to avoid overlapping ships. 
 */
@SuppressWarnings("serial")
public class Board extends JPanel {
	// Uses to print an letter instead of number for the rows
	private char[] alphabet = new char[]{'A','B','C','D','E','F','G','H','I','J'};
	
	private GridBagConstraints cont;		// For the row/col labels
	private GridBagConstraints contBtn;		// For the buttons
	

	// Stores the placement of the ships in a char representation to make it easier for
	// the functions to work with instead of buttons
	private char[][] board;
	
	private GameButton[][] buttons;
	
	/**
	 * Create a Board containing an empty char array and
	 * graphcical columns and rows description. 
	 */
	public Board() {
		board = new char[10][10];
		buttons = new GameButton[10][10];
		makeEmptyBoard();
		cont = new GridBagConstraints(); 		
		contBtn = new GridBagConstraints();
		contBtn.fill = GridBagConstraints.VERTICAL;

		// Hard coded size. Opps
		Dimension size = getPreferredSize();
		size.width = 595;
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
		setBackground(Color.WHITE);;
		Border padding = new EmptyBorder(1, 10, 10, 20);
		Border border = BorderFactory.createLineBorder(Color.GRAY);
		setBorder(new CompoundBorder(border, padding));
		

		// Use a GridbagLayout for the board to organize in a grid
		setLayout(new GridBagLayout());
		
		// Adds graphics labels to the board
		addRowsCols();
	}
	
	/**
	 * Places lines in the board[][] array to represent an empty board with no ships
	 * Makes it easier to add different buttons to the board and to check if the ships are 
	 * in a valid position.
	 */
	private void makeEmptyBoard() {
		for (int i = 0; i < board.length; i++) {	
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = '-';
			}
		}
	}

	/**
	 * Adds A-J labels to the rows and 0-9 labels to the columns graphically
	 */
	private void addRowsCols() {
		// Add Rows A-J
		cont.gridx = 0;
		cont.insets = new Insets(5, 10, 5, 10);
		for(int i = 0; i<10; i++){
			cont.gridy = 1 + i;
			cont.weighty = 1;
			add(new JLabel(""+alphabet[i]), cont);			
		}

		// Add columns 0-9
		cont.gridy = 0;
		for(int k = 0; k<10; k++) {
			cont.gridx = 1 + k;
			cont.weightx = 1;
			cont.weighty = 0.5;
			add(new JLabel(""+k), cont);
		}
	}

	/**
	 * Fills the board on the remaining slots with buttons representing misses
	 */
	public void fillBoard(Game game) {
		for(int i = 0; i<10; i++) {
			for (int j = 0; j < 10; j++) {
				contBtn.gridx = j+1;
				contBtn.gridy = i+1;
				if(board[i][j] == '-') {
					MissButton miss = new MissButton(game, ""+alphabet[i]+j);
					buttons[i][j] = miss;
					add(miss, contBtn);					
				}
			}
		}
	}

	/**
	 * Places the ship in the board. Must be called depending on noOverlap and isInBoundary
	 * in order to make an correct placement
	 * @param ship Ship to bi placed
	 */
	public void addShipToBoard(Game game, Ship ship) {
		int length = ship.getLength();
		char pos = ship.getAlignment();
		int row = ship.getRow();
		int col = ship.getCol();

		switch(pos) {
		case 'V':
			for (int i = row; i < (row + length); i++) {
				board[i][col] = ship.getShipType(); // Adds the shipType to the char-board[][]
				ShipButton shipBtn = new ShipButton(game, ship, ""+ alphabet[i] + col);
				buttons[i][col] = shipBtn;
				contBtn.gridx = col + 1;	
				contBtn.gridy = i + 1;
				add(shipBtn, contBtn);	// Adds a shipButton to the correct position connected to the ship
			}
			break;
		case 'H':
			for (int i = col; i < (col + length); i++) {
				board[row][i] = ship.getShipType();
				ShipButton shipBtn = new ShipButton(game, ship, ""+alphabet[row] + i);
				buttons[row][i] = shipBtn;
				contBtn.gridx = i + 1;
				contBtn.gridy = row + 1;
				add(shipBtn, contBtn);
			}
			break;
		default:
			System.out.println("Error, addShipToBoard.OH-SHIT");
			break;
		}
	}

	/**
	 * Checks if there are at least one square in between the requested placement of the ship 
	 * @param ship Ship to check for valid position 
	 * @return True if the position is valid
	 */
	public boolean noOverlap(Ship ship) {
		int col = ship.getCol();
		int row = ship.getRow();
		int length = ship.getLength();
		char pos = ship.getAlignment();
		
		// If the ships wants to be placed vertical
		if(pos == 'V') {
			for(int nextPos = 0; nextPos < length; nextPos++) {
				int rowStart = row-1+nextPos, rowEnd = row+1+nextPos;
				int colStart = col-1, colEnd =col+1;

				// If the position is along the edge or in a corner we don't want to control
				// the positions outside the board, so these are skipped
				if(rowStart < 0) {
					rowStart = 0;
				}
				if(rowEnd > 9) {
					rowEnd = 9;
				}
				if(colStart < 0) {
					colStart = 0;
				}
				if(colEnd > 9) {
					colEnd = 9;
				}

				// Checking one coordinate at a time in a 3x3 (not i special cases) square around the coordinate
				for(int y = rowStart; y <= rowEnd; y++) {
					for(int x = colStart; x <= colEnd; x++) {
						if(board[y][x] != '-') {
							return false;
						}
					}
				}
			}
		} 
		else {
			for(int nextPos = 0; nextPos < length; nextPos++) {
				int rowStart = row-1, rowEnd = row+1;
				int colStart = col-1+nextPos, colEnd = col+1+nextPos;

				// If the position is along the edge or in a corner we don't want to control
				// the positions outside the board, so these are skipped
				if(rowStart < 0) {
					rowStart = 0;
				}
				if(rowEnd > 9) {
					rowEnd = 9;
				}
				if(colStart < 0) {
					colStart = 0;
				}
				if(colEnd > 9) {
					colEnd = 9;
				}

				// Checking one coordinate at a time in a 3x3 (not i special cases) square around the coordinate
				for(int y = rowStart; y <= rowEnd; y++) {
					for(int x = colStart; x <= colEnd; x++) {
						if(board[y][x] != '-') {
							return false;
						}
					}
				}
			}
		}

		return true;
	}
	
	/**
	 * Returns a 2-dimensional array holding the buttons the board
	 * @return buttons
	 */
	public GameButton[][] getButtons() {
		return buttons;
	}
	
	/**
	 * Reurns a 2-dimensional char array representing the board where
	 * the ships are placed
	 * @return board
	 */
	public char[][] getBoard() {
		return board;
	}
	
	/**
	 * Disables all the buttons on the players board so you can't shoot at your own board
	 */
	public void disableAllButtons() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j].setEnabled(false);
			}
		}
	}

	/**
	 * Makes a graphic overview of the ships the player have placed on the
	 * board to help place next ship on a valid place.
	 **/
	public String makeGraphicBoard() {
		String output = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				output += board[i][j] + " ";
			}
			output += "\n";
		}
		return output;
	}
}
