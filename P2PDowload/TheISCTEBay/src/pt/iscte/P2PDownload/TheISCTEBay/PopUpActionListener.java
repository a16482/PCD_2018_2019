package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class PopUpActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent evt) {
		System.out.println("Selected: " + evt.getActionCommand());
		//...
		switch(evt.getActionCommand()) {
		case ("Utilizadores"):
			try {
				criaEmostraIGUtilizadores();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		break;
		case ("Configurações"):
			try {
				criaEmostraIGConfig();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default: 
			MsgBox.info(evt.getActionCommand());
			break;
		}
	}
	
	private static void criaEmostraIGUtilizadores() throws InterruptedException {
		JFrame frame = new JFrame("The ISCTE Bay - Utilizadores");
		frame.setAlwaysOnTop(true);
		JComponent UtilizadoresContentPane = new IGUtilizadores();
		UtilizadoresContentPane.setOpaque(true); 
		
		frame.setContentPane(UtilizadoresContentPane);
		frame.setSize(500, 500);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.pack();
        frame.setLocationRelativeTo(null);

	}
	
	private static void criaEmostraIGConfig() throws InterruptedException {
		JFrame frameConfig = new JFrame("The ISCTE Bay - Configurações");
		frameConfig.setAlwaysOnTop(true);
		JComponent ConfigContentPane = new WinPopUpConfig();
		ConfigContentPane.setOpaque(true); 
		
		frameConfig.setContentPane(ConfigContentPane);
		frameConfig.setSize(600, 600);
		frameConfig.setVisible(true);
		frameConfig.setResizable(false);
		frameConfig.pack();
		frameConfig.setLocationRelativeTo(null);
	}
}
