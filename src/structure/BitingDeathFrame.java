package structure;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.logicalkip.bitingdeath.bitingdeath.BitingDeathGame;
import com.logicalkip.bitingdeath.bitingdeath.RaidSettings;

/**
 * @author LogicalKip
 *
 */
public class BitingDeathFrame extends JFrame {

	/* 
	 * TODO : preferences, new game, survivor list, next day, messages/next message, see map, 
	 * g�rer Game over,
     */ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4518217666862978656L;
	
	private BitingDeathGame game;
	
	public BitingDeathFrame(BitingDeathGame newGame) {
		super();
		this.setSize(800, 600);
		this.setVisible(true);
		this.setTitle("Biting Death");
		this.setLocationRelativeTo(null);
		this.setMenuBar(new BitingDeathMenu(game));
		this.game = newGame;
		
		Button button = new Button("Set a Raid");
		this.getContentPane().setLayout(new FlowLayout());
	    this.getContentPane().add(button);
	    button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				RaidDialog raidDialog = new RaidDialog(null, "Setting a raid", true, game);
				RaidSettings raidSettings = raidDialog.showRaidDialog();
				
				JOptionPane.showMessageDialog(null, raidSettings.getDestination().getName() + "\n" + raidSettings.getTeam().toString(), "Composition de la team", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

}
