package structure;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.bitingdeath.Weapon;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;
import com.logicalkip.bitingdeath.exceptions.CantEquipWeaponException;
import com.logicalkip.bitingdeath.exceptions.NoSurvivorSelectedException;
import com.logicalkip.bitingdeath.exceptions.NoWeaponException;

/**
 * A dialog where the user can see and manage the survivors
 * @author charles
 *
 */
public class SurvivorsManagingDialog extends JDialog {
	
	private static final long serialVersionUID = 5774289164670804838L;
	private BitingDeathGame game;
	private List<JRadioButton> survSelectButtons;
	private JLabel currentWeaponLabel;
	private JLabel fightLevel;
	private JLabel scavengeLevel;
	private JPanel availableWeaponsPanel;
	
	public SurvivorsManagingDialog(JFrame parent, String title, boolean modal, BitingDeathGame game) {
		super(parent, title, modal);
		
		this.game = game;
		
		this.initComponent();
		this.updateAll();
		
		this.setLocationRelativeTo(null);
	}
	
	private void initComponent() {
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		//TODO borders
		
		/* Survivors names + radio buttons */
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		
		ButtonGroup buttonGroup = new ButtonGroup();
		this.survSelectButtons = new LinkedList<JRadioButton>();
		JPanel survSelectPanel = new JPanel();
		survSelectPanel.setLayout(new BoxLayout(survSelectPanel, BoxLayout.Y_AXIS));
		
		for (Survivor s : this.game.getSurvivors()) {
			JRadioButton radioButton = new JRadioButton(s.getName());
			radioButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					updateAll();					
				}
			});
			
			buttonGroup.add(radioButton);
			this.survSelectButtons.add(radioButton);
			survSelectPanel.add(radioButton);
		}		
		this.survSelectButtons.get(0).setSelected(true);
		pane.add(survSelectPanel, c);
		
		
		/* Current weapon */
		c.gridx = 1;
		c.gridy = 0;
		this.currentWeaponLabel = new JLabel();
		pane.add(this.currentWeaponLabel, c);
		
		/* Take-off-weapon button */
		c.gridx = 2;
		c.gridy = 0;
		JButton unequipButton = new JButton("Unequip");
		unequipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				unequipWeapon();
				updateAll();
			}
		});
		pane.add(unequipButton, c);
		
		/* Available weapons */
		c.gridx = 1;
		c.gridy = 1; 
		this.availableWeaponsPanel = new JPanel();
		this.availableWeaponsPanel.setLayout(new GridBagLayout());
		pane.add(this.availableWeaponsPanel, c);
		
		/* Skills */
		c.gridx = 1;
		c.gridy = 2;
		pane.add(new JLabel("Fighting : "), c);
		c.gridy = 3;
		pane.add(new JLabel("Scavenging : "), c);
		
		c.gridx = 2;
		c.gridy = 2;
		this.fightLevel = new JLabel();
		pane.add(this.fightLevel, c);
		c.gridy = 3;
		this.scavengeLevel = new JLabel();
		pane.add(this.scavengeLevel, c);
		
		
		/* Ok button */
		c.gridx = 2;
		c.gridy = 4;
		c.gridheight = 1;
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		pane.add(okButton, c);
	}
	
	private void updateAll() {
		this.updateCurrentWeaponLabel();
		this.updateSkillsLevel();
		this.updateAvailableWeapons();		
		
		this.pack();
	}
	
	private void updateAvailableWeapons() {
		this.availableWeaponsPanel.removeAll();
		
		int line = 0;
		
		for (Weapon w : this.game.getMainBase().getAvailableWeapons()) {
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridy = line; 
			
			c.gridx = 0;
			c.anchor = GridBagConstraints.WEST;
			this.availableWeaponsPanel.add(new JLabel(w.getName()), c);
			
			c.gridx = 1;
			c.anchor = GridBagConstraints.EAST;
			JButton equipButton = new JButton("Equip");
			final int LINE = line;
			equipButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						equipWeapon(LINE);
					} catch (CantEquipWeaponException
							| NoSurvivorSelectedException e) {
						debugError(e);
					}
					updateAll();
				}
			});
			this.availableWeaponsPanel.add(equipButton, c);
			
			line++;
		}
	}
	
	private void updateSkillsLevel() {
		try {
			this.fightLevel.setText(   String.valueOf(this.getSelectedSurvivor().getSkills().getFightingSkill()));
			this.scavengeLevel.setText(String.valueOf(this.getSelectedSurvivor().getSkills().getScavengingSkill()));
		} catch (NoSurvivorSelectedException e) {
			String nothingToDisplay = "-";
			this.fightLevel.setText(nothingToDisplay);
			this.scavengeLevel.setText(nothingToDisplay);
		}
	}
	
	private void updateCurrentWeaponLabel() {
		Survivor selectedSurv;
		String newLabel;
		try {
			selectedSurv = this.getSelectedSurvivor();
			newLabel = "Weapon : ";
			if (selectedSurv.getWeapon() == null) {
				newLabel += "-";
			} else {
				newLabel += selectedSurv.getWeapon().getName();
			}
			
		} catch (NoSurvivorSelectedException e) {
			newLabel = "No survivor selected !";
		}

		this.currentWeaponLabel.setText(newLabel);
	}
	
	private Survivor getSelectedSurvivor() throws NoSurvivorSelectedException {
		for (int i = 0 ; i < this.survSelectButtons.size() ; i++) {
			if (this.survSelectButtons.get(i).isSelected()) {
				return this.game.getSurvivors().get(i);
			}
		}
		throw new NoSurvivorSelectedException();
	}
	
	/**
	 * Makes the currently selected survivor equip a weapon in the displayed list.
	 * Removes the weapon from the logical list and stores the previously equipped one (if any)
	 * @param idWeapon the index of the weapon in the displayed list
	 * @throws NoSurvivorSelectedException 
	 * @throws CantEquipWeaponException 
	 */
	private void equipWeapon(int idWeapon) throws CantEquipWeaponException, NoSurvivorSelectedException {
		this.game.equipWeapon(getSelectedSurvivor(), this.game.getMainBase().getAvailableWeapons().get(idWeapon));
	}
	
	/**
	 * Makes the currently selected survivor take off his/her weapon if he/she has one.
	 * Does nothing otherwise
	 * @throws NoSurvivorSelectedException 
	 */
	private void unequipWeapon() {		
		try {
			Survivor s = this.getSelectedSurvivor();
			this.game.unequipWeapon(s);
		} catch (CantEquipWeaponException | NoSurvivorSelectedException e) {
			debugError(e);
		} catch (NoWeaponException e) {
			// Do nothing
		}
	}
	
	/**
	 * What to do when something that should not be possible happens (debug exceptions).
	 * @param e the exception you probably caught
	 */
	private void debugError(Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "DEBUG ERROR", JOptionPane.WARNING_MESSAGE);
		e.printStackTrace();
	}
}
