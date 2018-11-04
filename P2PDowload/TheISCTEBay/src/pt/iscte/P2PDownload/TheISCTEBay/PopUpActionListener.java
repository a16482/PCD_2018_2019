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
			criaEmostraIGUtilizadores();
		break;
		case ("Configurações"):
			break;
		default: 
			break;
		}
	}
	
	private static void criaEmostraIGUtilizadores() {
		JFrame frame = new JFrame("The ISCTE Bay - Utilizadores");
		frame.setAlwaysOnTop(true);
		JComponent UtilizadoresContentPane = new IGUtilizadores();
		UtilizadoresContentPane.setOpaque(true); 
		
		frame.setContentPane(UtilizadoresContentPane);
		frame.setSize(400, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
        frame.setLocationRelativeTo(null);

	}

	@SuppressWarnings("unused")
	private static void criaEmostraIGConfig() {
		// to do
	}
}
