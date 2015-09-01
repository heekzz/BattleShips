package BattleShips;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class MissButton extends GameButton {
	
	private boolean botShot = false;
	
	public MissButton(Game game, String title) {
		super(title);
		
		// Handles what happens then you click a button.
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDisabledIcon(missIcon); 	// Set icon of a disabled button
				setIcon(missIcon);			// Change Icon
				setText(null);				// Remove text
				setEnabled(false);			// Disabels the button
				game.checkGameStatus();		// Check if someone won
				
				// If the player shoots we want to add to player stats and
				// then make the bot shoot
				if(!botShot) {
					game.getStats().addPlayerMiss();
					game.getBot().shoot();
				}
				else {
					botShot = false;
					game.getStats().addCompMiss();
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
