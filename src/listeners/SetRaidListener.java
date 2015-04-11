package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import structure.NewRaidDialog;

import com.logicalkip.bitingdeath.bitingdeath.RaidCreator;
import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;

/**
 * Describes what to do when the "Set raid" button is clicked
 * @author charles
 *
 */
public class SetRaidListener implements ActionListener {
	
	RaidCreator raidCreator;
	
	public SetRaidListener(RaidCreator rc) {
		this.raidCreator = rc;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		NewRaidDialog raidDialog = new NewRaidDialog(null, "Setting a raid", true, this.raidCreator.getPickableRaiders(), this.raidCreator.getMap(), this.raidCreator.getZonesAlreadyBeingRaided(), this.raidCreator.getMainBase());
		RaidSettings userDefinedRaid = raidDialog.showRaidDialog();
		
		if (userDefinedRaid != null) {
			this.raidCreator.addRaid(userDefinedRaid);
		}
	}
}
