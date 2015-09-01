package BattleShips;

public class Ship {
	private char shipType;
	private char shipAlignment, shipRow, shipCol;
	private int shipDamage;

	public final int ROW_CONST = 65;
	public final int COL_CONST = 48;

	public Ship(char shipType){
		this.shipType = shipType;
		shipDamage = 0;
		shipAlignment = 'V';
		shipRow = 'A';
		shipCol = 0;
	}

	/**
	 * Returns name of the ship. Uses in the beginning when we add the ships
	 * @return
	 */
	public String getName() {
		switch (shipType) {
		case 'A':
			return "Aircraft Carrier";
		case 'B':
			return "Battleship";
		case 'D':
			return "Destroyer";
		case 'S':
			return "Submarines";
		case 'P':
			return "Patrol Boat";
		case '-': 
			return "Miss";
		default:
			System.out.println("Error, ship.getname!");
			return "shipType error";
		}
	}
	
	/**
	 * Set postion parameters of the ship
	 * @param row row in board
	 * @param col column in board
	 * @param pos alignment of ship
	 */
	public void setPosition(char row, char col, char pos) {
		shipRow = row;
		shipCol = col;
		shipAlignment = pos;
	}

	/**
	 * 
	 * @return alignment of ship
	 */
	public char getAlignment() {
		return shipAlignment;
	}
	
	/**
	 * @return starting row of ship
	 */
	public int getRow() {
		return shipRow - ROW_CONST;
	}
	
	/**
	 * @return starting column of ship
	 */
	public int getCol() {
		return shipCol - COL_CONST;
	}
	
	/**
	 * Every ship has a specific shiptype which then has a specific length and name
	 * @return type of the ship
	 */
	public char getShipType() {
		return shipType;
	}

	/**
	 * Returns an int of the length of the Ship
	 * @return
	 */
	public int getLength() {
		switch(shipType) {
		case 'A':
			return 5;
		case 'B':
			return 4;
		case 'D':
			return 3;
		case 'S':
			return 3;
		case 'P':
			return 2;
		case '-': 
			return 0;
		default:
			System.out.println("Error, ship.isInBoundary.length");
			return 99;
		}
	}

	/**
	 * In order to place the ship we need it to be in the boards boundary which is a 10x10 field
	 * @return true if the ship is the boundary of the board
	 */
	public boolean isInBoundary() {
		int length = getLength();
		int row = shipRow - ROW_CONST;
		int col = shipCol - COL_CONST;

		switch(shipAlignment) {
		case 'V':
			row += length;
			break;
		case 'H':
			col += length;
			break;
		default:
			length = 99;
			break;
		}
		if(row <= 9 && row >= 0 && col <= 9 && col >= 0) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Shoots at the boat and decreases the "life" by 1
	 */
	public void shoot() {
		shipDamage++;
	}

	/**
	 * Returns true if the Ships damagde is equal to it's size
	 * @return true if the ship have sunk
	 */
	public boolean isSunk() {
		if(shipDamage == this.getLength()) {
			return true;
		}
		else {
			return false;
		}
	}

}
