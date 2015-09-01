package BattleShips;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * Shipbutton extends JButton but we want to
 * add more stuff to the JButton so we create a
 * customize class instead
 */
@SuppressWarnings("serial")
public class ShipButton extends GameButton {

	private boolean botShot = false;

	// Constructor takes the name shown on the button and position on the board
	public ShipButton(Game game, Ship ship, String title) {
		super(title);

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDisabledIcon(hitIcon);	// Set icon of a disabled button
				setIcon(hitIcon);			// Change Icon
				setText(null);				// Remove text
				setEnabled(false);			// Disabels the button
				ship.shoot();				// Shoot at the ship, BAAAM!
				
				// If the player shoots we want to add to player stats and
				// we also want to chech if the ship we shot at is sunk.
				if(!botShot) {
					game.getStats().addPlayerHit();
					if(ship.isSunk()) {
						JOptionPane.showMessageDialog(null, "You sank the " + ship.getName() + "!!", 
								"You sank the " + ship.getName() + "!!",
								JOptionPane.PLAIN_MESSAGE); // Notify that you sank a ship
						game.playerShipHasBeenSunk();
						game.checkGameStatus();	// Check if someone won
						game.getBot().shoot();
					}
				} else {
					botShot = false;
					game.getStats().addCompHit();
					if(ship.isSunk()) {
						JOptionPane.showMessageDialog(null, "The Bot sank the " + ship.getName() + "!!",
								"The Bot sank the " + ship.getName() + "!!",
								JOptionPane.PLAIN_MESSAGE);	// Notify that the bot sank a ship of yours
						game.compShipHasBeenSunk();
						game.checkGameStatus();
						
						// If the bot sank the ship we reset the stack in order to 
						// shoot a a new random pos instead of close to the ship we just sunk
						game.getBot().clearStack();	
					}
				}
				
			}
		});
	}

	/**
	 * Call for this function if there is a bot that makes the shot instead of a physical click
	 */
	public void botShot() {
		botShot = true;
		setEnabled(true);	// Enabling the button right before the bot makes a click
		this.doClick();		// Does a click and then the button is disabled again
	}

}
