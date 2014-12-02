package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import structure.NewRaidDialog;
import structure.RaidManagingDialog;

import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Map;

/**
 * Describes what to do when the "Set raid" button is clicked
 * @author charles
 *
 */
public class SetRaidListener implements ActionListener {
	
	private Map map;
		
	private RaidManagingDialog raidManagingDialog;
	
	
	public SetRaidListener(Map m, RaidManagingDialog dialog) {
		this.map = m;
		this.raidManagingDialog = dialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		NewRaidDialog raidDialog = new NewRaidDialog(null, "Setting a raid", true, this.raidManagingDialog.getSurvivorsNotPicked(), this.map);
		RaidSettings userDefinedRaid = raidDialog.showRaidDialog();
		
		if (userDefinedRaid != null) {
			this.raidManagingDialog.addRaid(userDefinedRaid);
		}
		this.raidManagingDialog.updateAll();
	}
}
