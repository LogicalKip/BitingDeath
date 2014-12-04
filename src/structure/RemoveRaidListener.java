package structure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.logicalkip.bitingdeath.exceptions.IncoherentNumberException;

/** 
 * ActionListener for buttons in {@link RaidManagingDialog} that allow to delete a raid.
 * @author charles
 */
public class RemoveRaidListener implements ActionListener {

	/**
	 * 
	 */
	private int pos;
	
	private RaidManagingDialog rmd;
	
	public RemoveRaidListener(int pos, RaidManagingDialog rmd) {
		this.pos = pos;
		this.rmd = rmd;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.rmd.removeRaid(this.pos);
			this.rmd.updateAll();
		} catch (IncoherentNumberException e1) {
			System.err.println("UNEXPECTED ERROR : There is no " + this.pos + "th raid");
			e1.printStackTrace();
		}
	}
}
