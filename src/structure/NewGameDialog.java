package structure;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class NewGameDialog extends JDialog {
	
	private static final long serialVersionUID = -6426771511023522638L;
	private boolean startNewGame;
	
	public NewGameDialog(JFrame parent, String title, boolean modal) {
		super(parent, title, modal);
		
		this.startNewGame = false;
		
		this.setResizable(true);
		this.setVisible(false);
		
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		this.initComponent();
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.validate();
		this.repaint();	
	}
	
	private void initComponent() {
		GridBagConstraints c = new GridBagConstraints();
		
		this.setLayout(new GridBagLayout());
		
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = GridBagConstraints.REMAINDER;
		c.gridy = 0;
		c.gridwidth = 2;
		JTextArea warningMsg = BitingDeathFrame.createTextArea();
		warningMsg.setText("Do you really want to start a new game (everything will be lost) ?");
		this.add(warningMsg, c);
		
		/* Buttons */
		c.gridwidth = 1;

		c.ipadx = 0;
		c.ipady = 0;
		
		c.gridx = 0;
		c.gridy = 1;
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onOkClick();
			}
		});
		this.add(okButton, c);
		
		c.gridx = 1;
		c.gridy = 1;
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onCancelClick();
			}
		});
		this.add(cancelButton, c);
	}
	
	/**
	 * Show the dialog window, allowing the user to custom his new game
	 * and hides it when he is finished with it.
	 * @return true if the user actually wants to start a new game
	 */
	public boolean showDialog() {
		this.setVisible(true);
		
		return this.startNewGame;
	}
	
	private void onOkClick() {
		this.startNewGame = true;
		this.setVisible(false);
	}
	
	private void onCancelClick() {
		this.startNewGame = false;
		this.setVisible(false);
	}
}
