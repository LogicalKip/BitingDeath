package structure;

import java.awt.Menu;
import java.awt.MenuItem;
import java.util.LinkedList;
import java.util.List;

import listeners.SetRaidListener;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.bitingdeath.RaidCreator;
import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Base;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Map;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;

public class RaidMenu extends Menu implements RaidCreator {

	private static final long serialVersionUID = 7594933627709899021L;

	private BitingDeathGame game;

	public RaidMenu(BitingDeathFrame frame) {
		super("Raid");

		this.game = frame.getGame();

		/**
		 * Create a new raid from the menu bar, quicker than going to the "Manage raids" dialog
		 */
		MenuItem newRaid = new MenuItem("Add new raid");
		newRaid.setEnabled(true);
		newRaid.addActionListener(new SetRaidListener(this));

		this.add(newRaid);		
	}

	@Override
	public List<Survivor> getPickableRaiders() {
		List<Survivor> res = new LinkedList<Survivor>();
		for (Survivor s : this.game.getSurvivors()) {
			boolean alreadyChosen = false;
			for (RaidSettings raid : this.game.getCurrentPlannedRaids()) {
				if (raid.getTeam().contains(s)) {
					alreadyChosen = true;
				}
			}
			if (!alreadyChosen) {
				res.add(s);
			}
		}
		return res;
	}

	@Override
	public Base getMainBase() {
		return this.game.getMainBase();
	}

	@Override
	public Map getMap() {
		return this.game.getMap();
	}

	@Override
	public List<Zone> getZonesAlreadyBeingRaided() {
		List<Zone> res = new LinkedList<Zone>();
		for (RaidSettings raid : this.game.getCurrentPlannedRaids()) {
			res.add(raid.getDestination());
		}
		return res;
	}

	@Override
	public void addRaid(RaidSettings raid) {
		List<RaidSettings> raids = this.game.getCurrentPlannedRaids();
		raids.add(raid);
		this.game.setCurrentRaidSettings(raids);
	}
}
