package structure;

import java.awt.Menu;
import java.awt.MenuItem;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;

public class RaidMenu extends Menu {
	
	private static final long serialVersionUID = 7594933627709899021L;
	
	//FIXME no need for attributes (still leave Jdoc)
	
	//FIXME remove run, and set becomes "Add new raid"
	
	/**
	 * Click to set up a Raid.
	 */
	private MenuItem set;
	
	/**
	 * Click to run the raid (must be set up).
	 * Disabled at first
	 */
	private MenuItem run;
	
	public RaidMenu(BitingDeathGame game, BitingDeathFrame frame) {
		super("Raid");
		
		this.set = new MenuItem("Set");
		this.run = new MenuItem("Run");
		
		this.add(set);
		this.add(run);
		
		this.run.setEnabled(false);
		this.set.setEnabled(false);
	}
}
