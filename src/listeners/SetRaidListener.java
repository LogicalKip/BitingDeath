package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import structure.BitingDeathFrame;
import structure.RaidDialog;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;

/**
 * Describes what to do when the "Set raid" button is clicked
 * @author charles
 *
 */
public class SetRaidListener implements ActionListener {
	
	private BitingDeathGame game;
	
	private BitingDeathFrame frame;
	
	public SetRaidListener(BitingDeathGame g, BitingDeathFrame f) {
		this.game = g;
		this.frame = f;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		RaidDialog raidDialog = new RaidDialog(null, "Setting a raid", true, this.game);
		RaidSettings raidSettings = raidDialog.showRaidDialog();
		
		if (raidSettings != null) {
			this.game.setCurrentRaidSettings(raidSettings);
		}
		this.frame.updateAll();
	}

}
