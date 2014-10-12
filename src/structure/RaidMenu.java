package structure;

import java.awt.Menu;
import java.awt.MenuItem;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;

public class RaidMenu extends Menu {
	
	private static final long serialVersionUID = 7594933627709899021L;
	
	/**
	 * Click to set up a Raid.
	 */
	private MenuItem set = new MenuItem("Set");
	
	/**
	 * Click to run the raid (must be set up).
	 * Disabled at first
	 */
	private MenuItem run = new MenuItem("Run");
	
	public RaidMenu(BitingDeathGame game) {
		super("Raid");
		
		this.run.setEnabled(false);

		this.add(set);
		this.add(run);
	}
}
