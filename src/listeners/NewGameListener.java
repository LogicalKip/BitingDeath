package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import structure.BitingDeathFrame;
import structure.NewGameDialog;

public class NewGameListener implements ActionListener {

	private BitingDeathFrame mainFrame;
	
	public NewGameListener(BitingDeathFrame frame) {
		this.mainFrame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (new NewGameDialog(null, "New Game", true).showDialog()) {
			mainFrame.startNewGame();
		}
	}
}
