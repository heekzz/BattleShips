package BattleShips;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

/**
 * Handles the left part of the game where the statistics are shown with graphical 
 * presentation and will be updated when actions are performed
 */
@SuppressWarnings("serial")
public class Stats extends JPanel {

	// Number of misses, shots and hits
	private int playerMiss = 0;	
	private int playerHits = 0;
	private int playerShots = 0;
	private int compMiss = 0;
	private int compHits = 0;
	private int compShots = 0;

	private String userName;
	private String highScoreName;
	private double highScorePercent;
	private int highScoreShots;

	private double playerHitPercent;
	private double compHitPercent;

	// JLabels that shows the stats
	private JLabel playerMissCounter;
	private JLabel playerMissLabel;
	private JLabel playerHitCounter;
	private JLabel playerHitLabel;
	private JLabel playerHitPercentCounter;
	private JLabel playerHitPercentLabel;

	private JLabel playerName;
	private JLabel compName;

	private JLabel compMissCounter;
	private JLabel compMissLabel;
	private JLabel compHitCounter;
	private JLabel compHitLabel;
	private JLabel compHitPercentCounter;
	private JLabel compHitPercentLabel;

	private JLabel highScoreText;
	private JLabel highScoreHitPercent;
	private JLabel highScoreTotalShots;

	private JLabel[] textLabels;
	private JLabel[] counterLabels;


	public Stats() {
		// Hard coded size, oops
		Dimension size = getMaximumSize();
		size.height = 120;
		setPreferredSize(size);

		textLabels = new JLabel[11];
		counterLabels = new JLabel[11];

		// Makes a cool border with text in the left corner, fancy!	
		setBorder(BorderFactory.createTitledBorder("Statistic"));

		// Components which shows the different statistics text
		textLabels [0] = playerName = new JLabel("Player: " + userName);
		textLabels [1] = playerMissLabel = new JLabel("Misses:");
		textLabels [2] = playerHitLabel = new JLabel("Hits:");
		textLabels [3] = playerHitPercentLabel = new JLabel("Hit accuracy:");

		textLabels [4] = compName = new JLabel("Computer");
		textLabels [5] = compMissLabel = new JLabel("Misses:");
		textLabels [6] = compHitLabel = new JLabel("Hits:");
		textLabels [7] = compHitPercentLabel = new JLabel("Hit accuracy:");

		textLabels [8] = highScoreText = new JLabel("Highscore by:");
		textLabels [9] = highScoreHitPercent = new JLabel("Hitpercent:");
		textLabels [10] = highScoreTotalShots = new JLabel("Total shots:");

		// Dynamically labels which changes when actions are performed 
		counterLabels[0] = new JLabel("");
		counterLabels[1] = playerMissCounter = new JLabel(" " + playerMiss);
		counterLabels[2] = playerHitCounter = new JLabel(" " + playerHits);
		counterLabels[3] = playerHitPercentCounter = new JLabel(" " + (int)playerHitPercent + " %");

		counterLabels[4] = new JLabel(""); 
		counterLabels[5] = compMissCounter = new JLabel(" " + compMiss);
		counterLabels[6] = compHitCounter = new JLabel(" " + compHits);
		counterLabels[7] = compHitPercentCounter = new JLabel(" " + (int)compHitPercent + " %");

		counterLabels[8] = new JLabel(" ");
		counterLabels[9] = new JLabel(" ");
		counterLabels[10] = new JLabel(" ");

		// Change fontsize of the text
		for (int i = 0; i < counterLabels.length; i++) {
			counterLabels[i].setFont(new Font("Serif", Font.PLAIN, 20));
		}
		for (int i = 0; i < textLabels.length; i++) {
			textLabels[i].setFont(new Font("Serif", Font.PLAIN, 20));
		}
		playerName.setFont(new Font("Serif", Font.BOLD, 20));
		compName.setFont(new Font("Serif", Font.BOLD, 20));

		// Set Layout for panel
		// GridbagLayout arranges the items in a grid
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();

		// First column (Playerstats) ----------------------
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.05;
		c.weighty = 0.05;
		c.gridx = 0;
		c.gridy = 0;
		add(playerName, c);
		c.gridy = 1;
		add(playerHitLabel, c);
		c.gridy = 2;
		add(playerMissLabel, c);
		c.gridy = 3;
		add(playerHitPercentLabel, c);

		// Second Column (Playerstats) ---------------------
		c.gridx = 1;
		c.gridy = 0;
		add(new JLabel(""), c);
		c.gridy = 1;
		add(playerHitCounter, c);
		c.gridy = 2;
		add(playerMissCounter, c);
		c.gridy = 3;
		add(playerHitPercentCounter, c);

		// Third column (Highscore) ----------------------
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.gridx = 2;
		c.gridy = 0;
		add(highScoreText, c);
		c.gridy = 1;
		add(highScoreTotalShots, c);
		c.gridy = 2;
		add(highScoreHitPercent, c);
		c.gridy = 3;
		add(new JLabel(""), c);

		// Fourth column (Comp) ------------------------	
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.05;
		c.weighty = 0.05;
		c.gridx = 3;
		c.gridy = 0;
		add(compName, c);
		c.gridy = 1;
		add(compHitLabel, c);
		c.gridy = 2;
		add(compMissLabel, c);
		c.gridy = 3;
		add(compHitPercentLabel,c);

		// Fifth column (Comp) --------------------------
		c.gridx = 4;
		c.gridy = 0;
		add(new JLabel(""), c);
		c.gridy = 1;
		add(compHitCounter, c);
		c.gridy = 2;
		add(compMissCounter, c);
		c.gridy = 3;
		add(compHitPercentCounter, c);
	}

	/**
	 * Adds a player miss and updates the scores
	 */
	public void addPlayerMiss() { 
		playerMiss++;
		playerShots++;
		playerHitPercent = (double)playerHits / (double)playerShots;
		playerHitPercent = (double)Math.round(playerHitPercent * 100);
		playerHitPercentCounter.setText("" + (int)playerHitPercent + " %");
		playerMissCounter.setText("" + playerMiss);
	}

	/**
	 * Adds a player hit and updates the scores
	 */
	public void addPlayerHit() {
		playerHits++;
		playerShots++;
		playerHitPercent = (double)playerHits / (double)playerShots;
		playerHitPercent = (double)Math.round(playerHitPercent * 100);
		playerHitCounter.setText("" + playerHits);
		playerHitPercentCounter.setText("" + (int)playerHitPercent + " %");
	}

	/**
	 * Adds a computer miss and updates the scores
	 */
	public void addCompMiss() {
		compMiss++;
		compShots++;
		compHitPercent = (double)compHits / (double)compShots;
		compHitPercent = (double)Math.round(compHitPercent * 100);
		compMissCounter.setText("" + compMiss);
		compHitPercentCounter.setText("" + (int)compHitPercent + " %");
	}

	/**
	 * Adds a computer hit and updates the scores
	 */
	public void addCompHit() {
		compHits++;
		compShots++;
		compHitPercent = (double)compHits / (double)compShots;
		compHitPercent = (double)Math.round(compHitPercent * 100);
		compHitCounter.setText("" + compHits);
		compHitPercentCounter.setText("" + (int)compHitPercent + " %");
	}
	
	public void setUsername(String _userName) {
		userName = _userName;
		playerName.setText("Player: " + userName);
	}

	/**
	 * Loads highscore from a textfile.
	 */
	public void loadHighScore() {
		String fileName ="highscore.txt";

		String line = null;

		try {
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bf = new BufferedReader(fileReader);

			while((line = bf.readLine()) != null) {
				String[] parts = line.split(":");

				switch(parts[0]) {
				case "Username":
					highScoreName = parts[1];
					highScoreText.setText("Highscore by: " + parts[1]);
					break;
				case "Hitpercent":
					highScorePercent = Double.parseDouble(parts[1]);
					highScoreHitPercent.setText("Hit percent: " + parts[1]);
					break;
				case "Shots":
					highScoreShots = Integer.parseInt(parts[1]);
					highScoreTotalShots.setText("Total shots to win: " + parts[1]);
					break;
				}

				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Highscore file not found!");
		} catch(IOException io) {
			System.out.println("Error reading file " + fileName);
		}
	}

	/**
	 * Save new highscore if there is a new record. 
	 * @param userName Name fo the user
	 */
	public boolean saveHighScore(String userName) {
		if(playerShots < highScoreShots || highScoreShots == 0) {
			// The name of the file to open.
			String fileName = "highscore.txt";

			try {
				// Assume default encoding.
				FileWriter fileWriter = new FileWriter(fileName);

				// Always wrap FileWriter in BufferedWriter.
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

				bufferedWriter.write("Username:" + userName);
				bufferedWriter.newLine();
				bufferedWriter.write("Hitpercent:" + playerHitPercent);
				bufferedWriter.newLine();
				bufferedWriter.write("Shots:" + playerShots);

				// Always close files.
				bufferedWriter.close();
			}
			catch(IOException ex) {
				System.out.println("Error writing to file '" + fileName + "'");
			}
			return true;
		} else {
			return false;
		}
	}

}



