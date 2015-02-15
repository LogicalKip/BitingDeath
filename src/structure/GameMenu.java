package structure;

import java.awt.Menu;
import java.awt.MenuItem;

import listeners.NewGameListener;

public class GameMenu extends Menu {
	private static final long serialVersionUID = 8293532843234454615L;

	
	public GameMenu(BitingDeathFrame frame) {
		super("Game");
		
		
		this.add(createNewGameMenuItem(frame));
	}
	
	private MenuItem createNewGameMenuItem(BitingDeathFrame frame) {
		MenuItem res = new MenuItem("New");
		res.addActionListener(new NewGameListener(frame));
		res.setEnabled(true);
		
		return res;
	}
}