package structure;

import java.awt.Menu;
import java.awt.MenuItem;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;

public class RaidMenu extends Menu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7594933627709899021L;

	private BitingDeathGame game;
	
	
	
	/**
	 * Click to set up a Raid.
	 */
	private MenuItem set = new MenuItem("Set");
	
	/**
	 * Clic to run the raid (must be set up).
	 * Unabled at first
	 */
	private MenuItem run = new MenuItem("Run");
	
	public RaidMenu(BitingDeathGame game) {
		super("Raid");
		
		
		this.game = game;
		this.run.setEnabled(false);

		this.add(set);
		this.add(run);
	}
}
