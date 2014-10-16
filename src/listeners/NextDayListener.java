/**
 * 
 */
package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import structure.BitingDeathFrame;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.exceptions.CantRunRaidException;

/**
 * Describes what to do when the "Next day" button is clicked 
 * @author charles
 *
 */
public class NextDayListener implements ActionListener {
	
	private BitingDeathGame game;
	
	private BitingDeathFrame frame;
	
	public NextDayListener(BitingDeathGame g, BitingDeathFrame f) {
		this.game = g;
		this.frame = f;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.game.nextDay();
			this.frame.updateAll();
			this.frame.showAllMessages();
		} catch (CantRunRaidException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		
	}

}
