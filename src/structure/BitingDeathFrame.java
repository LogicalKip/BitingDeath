package structure;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import listeners.NextDayListener;
import listeners.SetRaidListener;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;

/**
 * The main class of a graphical interface used to play Biting Death
 * @author LogicalKip
 *
 */
public class BitingDeathFrame extends JFrame {

	/* 
	 * TODO : preferences, new game, display survivor lists (raid+all), next day, messages/next message, see map, 
	 * Handle Game over
     */ 
	
	private static final long serialVersionUID = 4518217666862978656L;
	
	private BitingDeathGame game;
	
	private JLabel foodLabel;
	
	private JButton nextDayButton;
	
	private JButton raidButton;
	
	/**
	 * Starts the graphical interface for the game.
	 * @param newGame must not be null
	 */
	public BitingDeathFrame(BitingDeathGame newGame) {
		super();
		this.setSize(800, 600);

		this.setTitle("Biting Death");
		this.setLocationRelativeTo(null);
		this.setMenuBar(new BitingDeathMenu(game));
		this.game = newGame;
		
	    this.foodLabel = new JLabel();
	    this.foodLabel.setHorizontalAlignment(SwingConstants.LEFT);
	    this.foodLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		
		this.raidButton = new JButton("Set a raid");
		this.nextDayButton = new JButton("Next day");
		this.getContentPane().setLayout(new FlowLayout());
	    this.getContentPane().add(this.raidButton);
	    this.getContentPane().add(this.nextDayButton);
	    this.getContentPane().add(this.foodLabel);
	    

	    
	    this.raidButton.addActionListener(new SetRaidListener(this.game));
	    this.nextDayButton.addActionListener(new NextDayListener(this.game, this));
	    
	    this.updateDisplayedData();
	    
		this.setVisible(true);
		
	    while (game.thereAreMessagesToDisplay()) {
			JOptionPane.showMessageDialog(null, this.game.getNextMessageToDisplay(), "Information", JOptionPane.INFORMATION_MESSAGE);
		}
	    
	}
	
	public void updateAll() {
		this.updateDisplayedData();
		
		// Updating UI
		if (this.game.getCurrentRaidSettings() != null && 
			this.game.getCurrentRaidSettings().getTeam().size() == 0) {  //FIXME or no destination provided... ?
			this.nextDayButton.setEnabled(false);
		} else {
			this.nextDayButton.setEnabled(true);
		}
	}
	
	public void updateDisplayedData() {
		this.foodLabel.setText("Rations left : " + this.game.getRations());
	}
}
