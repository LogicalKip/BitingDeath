package structure;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import listeners.ManageRaidListener;
import listeners.NextDayListener;

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
	 * TODO /!\ List concerning more or less the Swing implementation : 
	 * 
	 * Wouldn't creating new Dialogs every time (instead of reusing the same) accumulate and get slower ? Or garbage collector will get rid of them because they are of no use ?
	 * 
	 * Start-alone-mode
	 * 
	 * Pressing Esc <=> clicking cancel or |x|
	 * 
	 * Save
	 * 
	 * Use pictures (for skills, map, ?)
	 * 
	 * Remember where user put windows and display them same place next time
	 */ 

	private static final long serialVersionUID = 4518217666862978656L;
	
	private BitingDeathGame game;
	
	private JLabel foodLabel;
	
	private JTextArea currentRaidsSettingsText;
	
	private JTextArea survivorInfoText;
	
	private JButton nextDayButton;
	
	private JButton raidButton;
	
	/**
	 * Starts the graphical interface for the game.
	 * @param newGame must not be null
	 */
	public BitingDeathFrame(BitingDeathGame newGame) {
		super();
		
		this.game = newGame;

		this.setTitle("Biting Death");
		this.setMenuBar(new BitingDeathMenuBar(this));

		Container pane = this.getContentPane();

		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		// Natural height, maximum width
		c.fill = GridBagConstraints.HORIZONTAL;

		this.foodLabel = new JLabel();
		c.weightx = 0.5;
		c.weighty = 1;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;
		pane.add(this.foodLabel, c);
		
		
		this.raidButton = new JButton("Manage scavenging raids");
		c.gridx = 1;
		pane.add(this.raidButton, c);

		this.nextDayButton = new JButton("Next day");
		c.gridx = 2;
		pane.add(this.nextDayButton, c);

	    this.currentRaidsSettingsText = createTextArea();
	    c.gridy = 1;
	    c.gridx = 1;
	    c.gridwidth = 2;
	    c.weighty = 1;
	    c.weightx = 1;
	    pane.add(this.currentRaidsSettingsText, c);
	    
	    this.survivorInfoText = createTextArea();
	    c.gridy = 1;
	    c.gridx = 0;
	    c.gridwidth = 1;
	    c.weighty = 1;
	    pane.add(this.survivorInfoText, c);

	    this.raidButton.addActionListener(new ManageRaidListener(this));
	    this.nextDayButton.addActionListener(new NextDayListener(this));

	    this.updateAll();
		this.setVisible(true);

		
		
		this.showAllMessages();
	}
	
	public static JTextArea createTextArea() {
		JTextArea text = new JTextArea();
		text.setEditable(false);
		text.setCursor(null);  
		text.setOpaque(false);  
		text.setFocusable(false);

		return text;
	}
	
	public void updateAll() {
		this.updateDisplayedData();
		
		// Updating UI
		if (this.game.getCurrentPlannedRaids() == null) { // Should not happen
			this.nextDayButton.setEnabled(false);
		} else {
			this.nextDayButton.setEnabled(true);
		}
		
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public void updateDisplayedData() {
		this.foodLabel.setText("Rations left : " + this.game.getRations());
		
		// Survivor info
		String survText = "Survivors (" + this.game.getSurvivors().size() + "):\n";
		for (Survivor s : this.game.getSurvivors()) {
			survText += "   " + s.toString() + "\n";
		}
		this.survivorInfoText.setText(survText);
		
		this.currentRaidsSettingsText.setText(getRaidInfo());
	}
	
	/**
	 * Builds and return a string featuring the number of raid(s) and its runner(s), without using "(s)"
	 */
	private String getRaidInfo() {
		String raidText;
		List<RaidSettings> raids = this.game.getCurrentPlannedRaids();
		int nbRaids = raids.size();
		
		if (nbRaids == 0) {
			raidText = "You have not set any raid for today";
		} else {
			raidText = "Survivor";
			
			if (! (nbRaids == 1 && raids.get(0).getTeam().size() == 1)) {
				raidText += "s";
			}
			
			raidText += " ready for scavenging (" + nbRaids + " raid" + (nbRaids > 1 ? "s" : "") + ")";
		}		
		return raidText;
	}
	
	public void showAllMessages() {
	    while (game.thereAreMessagesToDisplay()) {
			JOptionPane.showMessageDialog(null, this.game.getNextMessageToDisplay(), "Information", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void startNewGame() {
		this.game = new BitingDeathGame();
		this.updateAll();
		this.showAllMessages();
	}

	public BitingDeathGame getGame() {
		return game;
	}
}
