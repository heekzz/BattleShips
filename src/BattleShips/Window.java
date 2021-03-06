	package BattleShips;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;

/**
 * Window class is the graphic base we are using and is 
 * extending JFrame from the Swing library. It contains some pretty standard
 * stuff for creating a JFrame and also where to place our boards
 * and stats panels. 
 */
@SuppressWarnings("serial")
public class Window extends JFrame {
	private Container cont; 

	public Window(int width, int height) {
		super("BattleShips");	// Creates JFrame with title BattleShips
		cont = getContentPane();

		// Set Layout
		setLayout(new BorderLayout());

		// Standard stuff
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(width, height);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	/**
	 * Add PlayerBoard to the window
	 */
	public void addPlayerBoard(Board board) {
		cont.add(board, BorderLayout.LINE_START);
	}
	
	/**
	 * Add CompBoard to the window
	 */
	public void addCompBoard(Board board) {
		cont.add(board, BorderLayout.LINE_END);
	}
	
	/**
	 * Add stats section to the window
	 */
	public void addStats(Stats stats) {
		cont.add(stats, BorderLayout.PAGE_START);
	}
}  
