package structure;

import java.awt.Menu;
import java.awt.MenuBar;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;

public class BitingDeathMenuBar extends MenuBar {

	private static final long serialVersionUID = 4170679580271387715L;
	
	public BitingDeathMenuBar(BitingDeathGame game, BitingDeathFrame frame) {
		super();
		
		Menu menu1 = new Menu("UNDEFINED");
		menu1.setEnabled(false);

		
		this.add(menu1);
		this.add(new RaidMenu(game, frame));
		
	}

}
