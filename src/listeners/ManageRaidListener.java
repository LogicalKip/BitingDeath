package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import structure.BitingDeathFrame;
import structure.RaidManagingDialog;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;

public class ManageRaidListener implements ActionListener {
	
	private BitingDeathGame game;
	
	private BitingDeathFrame frame;
	
	public ManageRaidListener(BitingDeathGame g, BitingDeathFrame f) {
		this.game = g;
		this.frame = f;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		RaidManagingDialog dialog = new RaidManagingDialog(null, "Scavenging menu", true, this.game);
		List<RaidSettings> userDefinedRaids = dialog.showDialog();
		
		if (userDefinedRaids != null) {
			this.game.setCurrentRaidSettings(userDefinedRaids);
		}
		
		this.frame.updateAll();
	}

}
