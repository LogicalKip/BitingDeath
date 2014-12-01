package structure;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import listeners.SetRaidListener;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;

/**
 * The dialog in which the user can set up new raids, (maybe edit) or remove some.
 * All currently set up raids are displayed.
 * @author charles
 * 
 */
public class RaidManagingDialog extends JDialog {
	private static final long serialVersionUID = -8667604758519407114L;

	private BitingDeathGame game;
	
	private JButton newRaidButton;
	
	private JButton okButton;
		
	private JScrollPane raidListScrollPane;
	
	/**
	 * True if the user wants his choices to be considered (~"OK" Button)
	 * False otherwise (~"Cancel", Close).
	 */
	private boolean sendData;
	
	/**
	 * A copy of the originally planned raids, that can be modified by the user and will eventually be accepted (i.e considered as the new raids) or canceled
	 */
	private List<RaidSettings> raids;
	
	//TODO edit button on each raid, that calls a specific "New"RaidDialog constructor (gives the details, they are copied and will eventually replace the old).
	
	public RaidManagingDialog(JFrame parent, String title, boolean modal, BitingDeathGame game) {
		super(parent, title, modal);
		
		this.game = game;
		this.sendData = false;
		this.raids = new LinkedList<RaidSettings>();
		for (RaidSettings raid : game.getCurrentPlannedRaids()) {
			this.raids.add(new RaidSettings(raid));
		}
		
		this.setResizable(true);
		this.setVisible(false);
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.initComponent();
		this.updateAll();
	}
	
	private void initComponent() {
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		/* New raid button */
		c.gridx = 0;
		c.gridy = 0;
		this.newRaidButton = new JButton("Add a new raid");
		this.newRaidButton.addActionListener(new SetRaidListener(this.game.getAvailableSurvivors(), this.game.getMap(), this));//FIXME it's not really the actual available survivors. You have to remove or add some, corresponding to what the user has set 
		pane.add(this.newRaidButton, c);
		
		
		/* ScrollPane for raid list */
		c.gridx = 1;
		c.gridy = 0;
		this.raidListScrollPane = new JScrollPane();
		pane.add(this.raidListScrollPane, c);
	
		
		/* OK CANCEL buttons */
		JPanel okCancelPanel = new JPanel();
		c.gridx = 0;
		c.gridy = 1;
		this.okButton = new JButton("Ok");
		this.okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendData = true;
				setVisible(false);
			}
		});
		okCancelPanel.add(this.okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		okCancelPanel.add(cancelButton);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		pane.add(okCancelPanel, c);
	}
	
	/**
	 * Show the dialog window, allowing the user to manage the raids (create, remove, etc)
	 * and hides it when he is finished with it.
	 * @return The raids to run today or null if he canceled.
	 */
	public List<RaidSettings> showDialog() {
		this.sendData = false;
		this.setVisible(true);
		
		// If he clicked on a "OK"-like button, sendData == true
		return this.sendData ? this.raids : null;
	}
	
	public void addRaid(RaidSettings raid) {
		this.raids.add(raid);
	}
	
	public void updateAll() {
		this.newRaidButton.setEnabled(this.game.getAvailableSurvivors().size() > 0);//FIXME not the actual idle survivors, need to consider those checked and unchecked for the current dialog
		
		
		JPanel scrollableRaidListPanel = new JPanel();
		scrollableRaidListPanel.setLayout(new BoxLayout(scrollableRaidListPanel, BoxLayout.Y_AXIS));
		for (RaidSettings raid : this.raids) {
			JPanel panel = new JPanel();
			String survNames = "";
			for (Survivor s : raid.getTeam()) {
				survNames += s.getName() + "\n";						
			}
			JTextArea survNamesArea = new JTextArea(survNames);
			survNamesArea.setEditable(false);
			survNamesArea.setOpaque(false);
			panel.add(survNamesArea);
			
			JTextArea destinationArea = new JTextArea(raid.getDestination().getName());
			destinationArea.setEditable(false);
			destinationArea.setOpaque(false);
			panel.add(destinationArea);
			
			panel.add(new JButton("Delete")); //TODO ActionListener
			
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			scrollableRaidListPanel.add(panel);
		}

		this.raidListScrollPane.setViewportView(scrollableRaidListPanel);
		this.validate();
		this.repaint();
	
	//	this.raidListScrollPane.setVisible(true);// FIXME useful ? Remove when all is OK if not necessary
		
		
	}	
}
