package structure;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;
import com.logicalkip.bitingdeath.exceptions.OutOfBoundsException;



public class RaidDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8885619271231314595L;

	/**
	 * True if the user wants his choices to be considered (~"Ok" Button)
	 * False otherwise (~"Cancel", Close).
	 */
	private boolean sendData;
	
	/**
	 * User's choices about the raid
	 */
	private RaidSettings raidSettings;
	
	
	private BitingDeathGame game;
	
	
	/**
	 * The Zone the player currently intends to raid. Until he clicks another one.
	 */
	private Zone currentChosenRaidZone;
	
	/**
	 * Checking one of those Checkbox means that you want the matching survivor to be in the Raid.
	 */
	private Checkbox []willBePartOfTheTeam;
	
	
	public RaidDialog (JFrame parent, String title, boolean modal, BitingDeathGame game) {
		super(parent, title, modal);
		
		this.game = game;
		this.raidSettings = new RaidSettings(null, null);
		this.sendData = false;
		this.currentChosenRaidZone = null;
		this.setResizable(false);
		this.setVisible(false);
		this.setSize(480, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.initComponent();
		//TODO cocher de base tout ce qui est dans raidSettings (suffit de ne pas réallouer willBePartOfTheTeam pour conserver les coches ? pb avec le nb de surv qui a pu changer ?)
	}
	
	
	private void initComponent() {
		
		this.getContentPane().setLayout(new GridBagLayout());
		
		/* 	SURVIVORS  */
		JPanel survivorsPanel = new JPanel();


		survivorsPanel.setLayout(new GridBagLayout());
		
		this.willBePartOfTheTeam = new Checkbox[this.game.getSurvivors().size()];
		Iterator<Survivor> iterator = this.game.getSurvivors().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Survivor currentSurvivor = iterator.next();
			
			willBePartOfTheTeam[i] = new Checkbox();

			GridBagConstraints constraints;


			// Current survivor description
			constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = i;
			constraints.insets= new Insets(0, 10, 0, 10);
			constraints.anchor = GridBagConstraints.PAGE_START;
			survivorsPanel.add(new JLabel(currentSurvivor.toString()), constraints);//FIXME positionner la ligne courante à la ième ligne du gridbag ?
			
			// Current checkBox
			constraints = new GridBagConstraints();
			constraints.gridx = 1;
			constraints.gridy = i;
			survivorsPanel.add(willBePartOfTheTeam[i], constraints);
			
			i++;
		}
				
		survivorsPanel.setBackground(Color.cyan);
		
		
		
		/*	MAP INFO 	*/
		JPanel zoneInfoPanel = new JPanel();
		final JLabel zoneInfoLabel = new JLabel("You haven't chosen a zone"); // final because of the ActionListener below
		zoneInfoPanel.add(zoneInfoLabel);
		zoneInfoPanel.setBackground(Color.white);

		
		/*	MAP CHOOSING */
		JPanel mapPanel = new JPanel();
		mapPanel.setLayout(new GridBagLayout());
		
		for (int x = 0 ; x < this.game.getMap().getWidth() ; x++) {
			for (int y = 0 ; y < this.game.getMap().getHeight() ; y++) {				
				Button currentZoneButton = new Button(this.getZoneFromMap(x, y).getName());
				GridBagConstraints currentButtonConstraints = new GridBagConstraints();
				currentButtonConstraints.gridx = x;
				currentButtonConstraints.gridy = y;
				currentButtonConstraints.anchor = GridBagConstraints.WEST;
				
				final int X = x, Y = y; // because the anonymous method below needs final.
				currentZoneButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						currentChosenRaidZone = getZoneFromMap(X, Y);
						zoneInfoLabel.setText("You will raid : " + currentChosenRaidZone.getName());
					}
				});
				
				mapPanel.add(currentZoneButton, currentButtonConstraints);
			}
		}
		
		/*	OK CANCEL Buttons */		
		JPanel closeButtonsPanel = new JPanel();
		
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendData = true;
				updateRaidSettings();
				setVisible(false);
			}
		});
		
		
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		closeButtonsPanel.add(okButton);
		closeButtonsPanel.add(cancelButton);
		
		
		// Adding all JPanel to Dialog.
		GridBagConstraints panelConstraint = new GridBagConstraints();
		
		panelConstraint.gridx = 0;
		panelConstraint.gridy = 0;
		this.getContentPane().add(survivorsPanel, panelConstraint);
		
		panelConstraint.gridx = 1;
		this.getContentPane().add(zoneInfoPanel, panelConstraint);
		
		panelConstraint.gridx = 0;
		panelConstraint.gridy = 1;
		panelConstraint.gridwidth = GridBagConstraints.REMAINDER;
		this.getContentPane().add(mapPanel, panelConstraint);
		
		panelConstraint.gridy = 2;
		this.getContentPane().add(closeButtonsPanel, panelConstraint);
	}
	
	/**
	 * Avoiding try/catch in the code
	 * @return the Zone located in [x,y]
	 */
	private Zone getZoneFromMap(int x, int y) {
		Zone res = null;
		try {
			res = this.game.getMap().getZone(x, y);
		} catch (OutOfBoundsException e) {
			e.printStackTrace();
		}
		return res;
	}

	
	/**
	 * Show the dialog window, allowing the user to customize the raid (which survivors, etc)
	 * and hides it when he is finished with it.
	 * @return The user choices about the Raid or null if he canceled.
	 */
	public RaidSettings showRaidDialog() {
		this.sendData = false;
		this.setVisible(true);
		
		// If he clicked on "Ok"-like button, sendData == true
		return this.sendData ? this.raidSettings : null;
	}
	
	/**
	 * Sets raidSettings to the values picked by the player on the RaidDialog
	 */
	private void updateRaidSettings() {
		LinkedList<Survivor> team = new LinkedList<Survivor>();
		for (int i = 0 ; i < this.willBePartOfTheTeam.length ; i++) {
			if (this.willBePartOfTheTeam[i].getState())
				team.add(game.getSurvivors().get(i));
		}
		this.raidSettings = new RaidSettings(this.currentChosenRaidZone, team);
	}
}
