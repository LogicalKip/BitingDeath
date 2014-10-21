package structure;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import listeners.NextDayListener;
import listeners.SetRaidListener;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;

/**
 * The main class of a graphical interface used to play Biting Death
 * @author LogicalKip
 *
 */
public class BitingDeathFrame extends JFrame {

	/* 
	 * TODO : preferences, new game, display survivor lists (raid+all), messages/next message, see map, 
	 * Handle Game over
     */ 
	
	private static final long serialVersionUID = 4518217666862978656L;
	
	private BitingDeathGame game;
	
	private JLabel foodLabel;
	
	private JTextArea currentRaidSettingsText;
	
	private JTextArea survivorInfoText;
	
	private JButton nextDayButton;
	
	private JButton raidButton;
	
	/**
	 * Starts the graphical interface for the game.
	 * @param newGame must not be null
	 */
	public BitingDeathFrame(BitingDeathGame newGame) {
		super();
		this.setSize(500, 300);
		
		this.game = newGame;

		this.setTitle("Biting Death");
		this.setLocationRelativeTo(null);
		this.setMenuBar(new BitingDeathMenuBar(game, this));


		Container pane = this.getContentPane();

		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		//natural height, maximum width
		c.fill = GridBagConstraints.HORIZONTAL;

		this.foodLabel = new JLabel();
		c.weightx = 0.5;
		c.weighty = 1;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;
		pane.add(this.foodLabel, c);
		
		
		this.raidButton = new JButton("Set a raid");
		c.gridx = 1;
		pane.add(this.raidButton, c);

		this.nextDayButton = new JButton("Next day");
		c.gridx = 2;
		pane.add(this.nextDayButton, c);

	    this.currentRaidSettingsText = new JTextArea();
	    this.currentRaidSettingsText.setEditable(false);
	    this.currentRaidSettingsText.setCursor(null);  
	    this.currentRaidSettingsText.setOpaque(false);  
	    this.currentRaidSettingsText.setFocusable(false);
	    c.gridy = 1;
	    c.gridx = 1;
	    c.gridwidth = 2;
	    c.weighty = 1;
	    c.weightx = 1;
	    pane.add(this.currentRaidSettingsText, c);
	    
	    this.survivorInfoText = new JTextArea();
	    this.survivorInfoText.setEditable(false);
	    this.survivorInfoText.setCursor(null);  
	    this.survivorInfoText.setOpaque(false);  
	    this.survivorInfoText.setFocusable(false);
	    c.gridy = 1;
	    c.gridx = 0;
	    c.gridwidth = 1;
	    c.weighty = 1;
	    pane.add(this.survivorInfoText, c);

	    this.raidButton.addActionListener(new SetRaidListener(this.game, this));
	    this.nextDayButton.addActionListener(new NextDayListener(this.game, this));

	    
	    this.updateAll();
		
		
		this.setVisible(true);
		
		this.showAllMessages();
	    
	}
	
	public void updateAll() {
		this.updateDisplayedData();
		
		// Updating UI
		if (this.game.getCurrentRaidSettings() == null || 
			this.game.getCurrentRaidSettings().getTeam().size() == 0 ||
			this.game.getCurrentRaidSettings().getDestination() == null) {
			this.nextDayButton.setEnabled(false);
		} else {
			this.nextDayButton.setEnabled(true);
		}
	}
	
	public void updateDisplayedData() {
		this.foodLabel.setText("Rations left : " + this.game.getRations());
		
		// Survivor info
		String survText = "Survivors :\n";;
		for (Survivor s : this.game.getSurvivors()) {
			survText += "   " + s.toString() + "\n";
		}
		this.survivorInfoText.setText(survText);
		
		// Raid info
		RaidSettings raidSettings = this.game.getCurrentRaidSettings();
		String raidText;
		if (raidSettings == null) {
			raidText = "You have not set any raid for today";
		} else if (raidSettings.getTeam().size() == 0) {
			raidText = "No survivors selected to run the raid in " + raidSettings.getDestination();
		} else {
			raidText = "You will raid : " + raidSettings.getDestination() + "\nwith :";
			for (Survivor s : raidSettings.getTeam()) {
				raidText += "\t- " + s.getName() + "\n";
			}
		}
		this.currentRaidSettingsText.setText(raidText);
	}
	
	public void showAllMessages() {
	    while (game.thereAreMessagesToDisplay()) {
			JOptionPane.showMessageDialog(null, this.game.getNextMessageToDisplay(), "Information", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
