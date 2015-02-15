/**
 * 
 */
package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import structure.BitingDeathFrame;

import com.logicalkip.bitingdeath.exceptions.CantRunRaidException;

/**
 * Describes what to do when the "Next day" button is clicked 
 * @author charles
 *
 */
public class NextDayListener implements ActionListener {
	
	private BitingDeathFrame frame;
	
	public NextDayListener(BitingDeathFrame f) {
		this.frame = f;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.frame.getGame().nextDay();
			this.frame.updateAll();
			this.frame.showAllMessages();
			if (this.frame.getGame().getGameOver()) {
				JOptionPane.showMessageDialog(null, this.frame.getGame().getGameOverMessage(), "GAME OVER", JOptionPane.WARNING_MESSAGE);
				System.exit(0); // TODO any cleaner way to do this ?
			}
		} catch (CantRunRaidException e1) {
			e1.printStackTrace();
		}
	
		
	}

}
