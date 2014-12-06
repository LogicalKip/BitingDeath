package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import structure.NewRaidDialog;
import structure.RaidManagingDialog;

import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Map;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;

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
		NewRaidDialog raidDialog = new NewRaidDialog(null, "Setting a raid", true, this.raidManagingDialog.getSurvivorsNotPicked(), this.map, this.getZonesAlreadyBeingRaided());
		RaidSettings userDefinedRaid = raidDialog.showRaidDialog();
		
		if (userDefinedRaid != null) {
			this.raidManagingDialog.addRaid(userDefinedRaid);
		}
		this.raidManagingDialog.updateAll();
	}
	
	private List<Zone> getZonesAlreadyBeingRaided() {
		List<Zone> res = new LinkedList<Zone>();
		
		for (RaidSettings raid : this.raidManagingDialog.getRaids()) {
			res.add(raid.getDestination());
		}
		
		return res;
	}
}
