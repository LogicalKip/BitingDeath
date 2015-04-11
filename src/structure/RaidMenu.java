package structure;

import java.awt.Menu;
import java.awt.MenuItem;
import java.util.LinkedList;
import java.util.List;

import listeners.SetRaidListener;

import com.logicalkip.bitingdeath.bitingdeath.RaidCreator;
import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Base;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Map;
import com.logicalkip.bitingdeath.bitingdeath.mapping.Zone;
import com.logicalkip.bitingdeath.bitingdeath.survivor.Survivor;

public class RaidMenu extends Menu implements RaidCreator {

	private static final long serialVersionUID = 7594933627709899021L;

	private BitingDeathFrame frame;

	public RaidMenu(BitingDeathFrame frame) {
		super("Raid");

		this.frame = frame;

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
		for (Survivor s : this.frame.getGame().getSurvivors()) {
			boolean alreadyChosen = false;
			for (RaidSettings raid : this.frame.getGame().getCurrentPlannedRaids()) {
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
		return this.frame.getGame().getMainBase();
	}

	@Override
	public Map getMap() {
		return this.frame.getGame().getMap();
	}

	@Override
	public List<Zone> getZonesAlreadyBeingRaided() {
		List<Zone> res = new LinkedList<Zone>();
		for (RaidSettings raid : this.frame.getGame().getCurrentPlannedRaids()) {
			res.add(raid.getDestination());
		}
		return res;
	}

	@Override
	public void addRaid(RaidSettings raid) {
		List<RaidSettings> raids = this.frame.getGame().getCurrentPlannedRaids();
		raids.add(raid);
		this.frame.getGame().setCurrentRaidSettings(raids);
		this.frame.updateAll();
	}
}
