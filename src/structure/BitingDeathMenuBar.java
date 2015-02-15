package structure;

import java.awt.MenuBar;

public class BitingDeathMenuBar extends MenuBar {

	private static final long serialVersionUID = 4170679580271387715L;
	
	public BitingDeathMenuBar(BitingDeathFrame frame) {
		super();
		
		this.add(new GameMenu(frame));
		this.add(new RaidMenu(frame));
	}

}
