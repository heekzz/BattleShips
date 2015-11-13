package BattleShips;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Abstract class that extends a JButton and act as a 
 * template for the other buttons (Shipbutton and Missbutton)
 * It defines the properties for the buttons used in the game.
 */
@SuppressWarnings("serial")
public abstract class GameButton extends JButton{
	protected ImageIcon missIcon, hitIcon;
	private URL missUrl, hitUrl;
	
	public GameButton(String title) {
		super(title);
		missUrl = GameButton.class.getResource("/waves.png");
		hitUrl = GameButton.class.getResource("/explosion.png");
		
		// Image icon of ocean waves to represent a miss
		missIcon = new ImageIcon(missUrl);
		
		// Image icon of explosion to represent a hit
		hitIcon = new ImageIcon(hitUrl);
		
		// Makes the button doesn't change size when clicked
		Dimension size = this.getPreferredSize();
		this.setMaximumSize(size);
		this.setMinimumSize(size);		
	}
	
	public abstract void botShot();

}
