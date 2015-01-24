package structure;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Map;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;
import com.logicalkip.bitingdeath.exceptions.OutOfBoundsException;


public class NewRaidDialog extends JDialog {

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
	
	
	/**
	 * List of the displayed survivors that may be selected to run the raid being set up
	 */
	private List<Survivor> availableSurvivors;

	/**
	 * The map from which will selected a Zone to raid
	 */
	private Map map;
	
	/**
	 * The Zone the player currently intends to raid. Until he clicks another one.
	 */
	private Zone currentChosenRaidZone;
	
	/**
	 * A list of Zones from the map that the user won't be able to choose.
	 */
	private List<Zone> zonesUnraidable;
	
	/**
	 * Checking one of those checkboxes means that you want the matching survivor to be in the Raid.
	 */
	private JCheckBox []willBePartOfTheTeam;
	
	private JButton okButton;
	
	/**
	 * 
	 * @param survivors List of the survivors the user will choose from to set on the raid.
	 * @param m The map from which will be picked a destination to raid
	 * @param zonesUnraidable A list of Zones from the map that the user won't be able to choose.
	 */
	public NewRaidDialog (JFrame parent, String title, boolean modal, List<Survivor> survivors, Map m, List<Zone> zonesUnraidable) {
		super(parent, title, modal);

		this.availableSurvivors = survivors;
		this.map = m;
		this.zonesUnraidable = zonesUnraidable;
		this.raidSettings = new RaidSettings(null, null);
		this.sendData = false;
		this.currentChosenRaidZone = null;
		this.setResizable(true);
		this.setVisible(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.initComponent();
	}
	
	
	private void initComponent() {
		
		this.getContentPane().setLayout(new GridBagLayout());
		
		/* 	SURVIVORS  */
		JPanel survivorsPanel = new JPanel();


		survivorsPanel.setLayout(new GridBagLayout());
		
		this.willBePartOfTheTeam = new JCheckBox[this.availableSurvivors.size()];
		Iterator<Survivor> iterator = this.availableSurvivors.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Survivor currentSurvivor = iterator.next();
			
			willBePartOfTheTeam[i] = new JCheckBox();

			GridBagConstraints constraints;


			// Current survivor description
			constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = i;
			constraints.insets= new Insets(0, 10, 0, 10);
			constraints.anchor = GridBagConstraints.PAGE_START;
			survivorsPanel.add(new JLabel(currentSurvivor.toString()), constraints);
			
			// Current checkBox
			constraints = new GridBagConstraints();
			constraints.gridx = 1;
			constraints.gridy = i;
			survivorsPanel.add(willBePartOfTheTeam[i], constraints);
			
			this.willBePartOfTheTeam[i].addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					updateOkButton();
				}
			});
			
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
		
		for (int x = 0 ; x < this.map.getWidth() ; x++) {
			for (int y = 0 ; y < this.map.getHeight() ; y++) {				
				JButton currentZoneButton = new JButton(this.getZoneFromMap(x, y).getName());
				GridBagConstraints currentButtonConstraints = new GridBagConstraints();
				currentButtonConstraints.gridx = x;
				currentButtonConstraints.gridy = y;
				currentButtonConstraints.anchor = GridBagConstraints.WEST;
				
				final int X = x, Y = y; // Because the anonymous method below needs final.
				currentZoneButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateOkButton();
					}
				});
				currentZoneButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						currentChosenRaidZone = getZoneFromMap(X, Y);
						zoneInfoLabel.setText("You will raid : " + currentChosenRaidZone.getName());
					}
				});
				currentZoneButton.setEnabled(! this.zonesUnraidable.contains(this.getZoneFromMap(x, y)));

				
				mapPanel.add(currentZoneButton, currentButtonConstraints);
			}
		}
		
		/*	OK CANCEL Buttons */		
		JPanel closeButtonsPanel = new JPanel();
		
		this.okButton = new JButton("Ok");
		this.okButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendData = true;
				updateRaidSettings();
				setVisible(false);
			}
		});
		this.updateOkButton();
		
		
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		closeButtonsPanel.add(this.okButton);
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
		
		this.pack();
	}
	
	/**
	 * Avoiding try/catch in the code
	 * @return the Zone located in [x,y]
	 */
	private Zone getZoneFromMap(int x, int y) {
		Zone res = null;
		try {
			res = this.map.getZone(x, y);
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
		
		// If he clicked on a "Ok"-like button, sendData == true
		return this.sendData ? this.raidSettings : null;
	}
	
	/**
	 * Sets raidSettings to the values picked by the player on the RaidDialog
	 */
	private void updateRaidSettings() {
		this.raidSettings = new RaidSettings(this.currentChosenRaidZone, this.getCurrentTeam());
	}
	
	private List<Survivor> getCurrentTeam() {
		List<Survivor> res = new LinkedList<Survivor>();
		for (int i = 0 ; i < this.willBePartOfTheTeam.length ; i++) {
			if (this.willBePartOfTheTeam[i].isSelected())
				res.add(this.availableSurvivors.get(i));
		}
		return res;
	}
	
	private boolean correctSettings() {
		return this.getCurrentTeam().size() > 0 && this.currentChosenRaidZone != null;
	}
	
	private void updateOkButton() {
		if (correctSettings()) {
			this.okButton.setEnabled(true);
		} else {
			this.okButton.setEnabled(false);
		}
	}
}
