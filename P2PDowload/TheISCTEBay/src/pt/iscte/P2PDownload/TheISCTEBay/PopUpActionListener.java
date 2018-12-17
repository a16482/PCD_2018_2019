package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class PopUpActionListener implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent evt) {
	
		switch(evt.getActionCommand()) {
			case ("Utilizadores"):
				try {
					criaEmostraIGUtilizadores();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				break;
			case ("Configurações"):
				try {
					criaEmostraIGConfig();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			default: 
				MsgBox.info(evt.getActionCommand());
				break;
		}
	}
	
	private static void criaEmostraIGUtilizadores() throws InterruptedException {
		String endIPCli = TheISCTEBay.devolveIPUtilizador();
		String portCli = String.valueOf(TheISCTEBay.devolvePortoUtilizador());
		JFrame frameUtilizadores = new JFrame();
		frameUtilizadores.setTitle("The ISCTE Bay" + " (" + endIPCli + ":" + portCli  + ")");
		frameUtilizadores.setAlwaysOnTop(false);
		JComponent UtilizadoresContentPane = new WinPopUpUtilizadores();
		UtilizadoresContentPane.setOpaque(true);
		frameUtilizadores.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameUtilizadores.setContentPane(UtilizadoresContentPane);;
		frameUtilizadores.setResizable(false);
		frameUtilizadores.pack();
        frameUtilizadores.setLocationRelativeTo(null);
		frameUtilizadores.setVisible(true);
	}
	
	private static void criaEmostraIGConfig() throws InterruptedException {
		JFrame frameConfig = new JFrame("The ISCTE Bay - Configurações");
		frameConfig.setAlwaysOnTop(false);
		JComponent ConfigContentPane = new WinPopUpConfig();
		ConfigContentPane.setOpaque(true);
		frameConfig.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		frameConfig.setContentPane(ConfigContentPane);
		frameConfig.setResizable(false);
		frameConfig.pack();
		frameConfig.setLocationRelativeTo(null);
		frameConfig.setVisible(true);
	}
}
