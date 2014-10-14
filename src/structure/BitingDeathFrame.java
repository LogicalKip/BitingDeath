package structure;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

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
	
	private JButton nextDayButton;
	
	private JButton raidButton;
	
	/**
	 * Starts the graphical interface for the game.
	 * @param newGame must not be null
	 */
	public BitingDeathFrame(BitingDeathGame newGame) {
		super();
		this.setSize(800, 600);
		
		this.game = newGame;

		this.setTitle("Biting Death");
		this.setLocationRelativeTo(null);
		this.setMenuBar(new BitingDeathMenuBar(game, this));
		
		
	    this.foodLabel = new JLabel();
	    this.foodLabel.setHorizontalAlignment(SwingConstants.LEFT);
	    this.foodLabel.setVerticalAlignment(SwingConstants.BOTTOM);
	    
	    this.currentRaidSettingsText = new JTextArea();
	    this.currentRaidSettingsText.setEditable(false);
	    this.currentRaidSettingsText.setCursor(null);  
	    this.currentRaidSettingsText.setOpaque(false);  
	    this.currentRaidSettingsText.setFocusable(false);
	    //FIXME Horizontal/vertical alignment
		
		this.raidButton = new JButton("Set a raid");
		this.nextDayButton = new JButton("Next day");
		this.getContentPane().setLayout(new FlowLayout());
	    this.getContentPane().add(this.raidButton);
	    this.getContentPane().add(this.nextDayButton);
	    this.getContentPane().add(this.foodLabel);
	    this.getContentPane().add(this.currentRaidSettingsText);
	    

	    
	    this.raidButton.addActionListener(new SetRaidListener(this.game, this));
	    this.nextDayButton.addActionListener(new NextDayListener(this.game, this));
	    
	    this.updateDisplayedData();
	    
		this.setVisible(true);
		
		this.showAllMessages();
	    
	}
	
	public void updateAll() {
		this.updateDisplayedData();
		
		// Updating UI
		if (this.game.getCurrentRaidSettings() == null || 
			this.game.getCurrentRaidSettings().getTeam().size() == 0) {  //FIXME or no destination provided... ?
			this.nextDayButton.setEnabled(false);
		} else {
			this.nextDayButton.setEnabled(true);
		}
	}
	
	public void updateDisplayedData() {
		this.foodLabel.setText("Rations left : " + this.game.getRations());
		
		// Raid info
		RaidSettings raidSettings = this.game.getCurrentRaidSettings();
		String raidText;
		if (raidSettings == null) {
			raidText = "You have not set any raid for today";
		} else if (raidSettings.getTeam().size() == 0) {
			raidText = "No survivors selected to run the raid";
		}  else {
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
