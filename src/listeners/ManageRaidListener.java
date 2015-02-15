package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import structure.BitingDeathFrame;
import structure.RaidManagingDialog;

import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;

public class ManageRaidListener implements ActionListener {
		
	private BitingDeathFrame frame;
	
	public ManageRaidListener(BitingDeathFrame f) {
		this.frame = f;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		RaidManagingDialog dialog = new RaidManagingDialog(null, "Scavenging menu", true, this.frame.getGame());
		List<RaidSettings> userDefinedRaids = dialog.showDialog();
		
		if (userDefinedRaids != null) {
			this.frame.getGame().setCurrentRaidSettings(userDefinedRaids);
		}
		
		this.frame.updateAll();
	}

}
