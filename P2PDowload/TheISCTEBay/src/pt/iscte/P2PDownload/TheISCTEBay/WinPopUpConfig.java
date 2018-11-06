package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

//public class WinPopUpConfig extends JPanel implements ActionListener, PropertyChangeListener {
public class WinPopUpConfig extends JPanel  {
	/** Trata a janela PopUp de Configura��es.
	 *  Projeto: The ISCTE Bay
	 */
	private static final long serialVersionUID = 1L;

	private static final int W = 600;
	private static final int H = 600;
	JButton botaoOK;

	private ActionListener okActionListener; //aqui � que est� o bus�lis

	class Task extends SwingWorker<Void, Void> {

		@Override
		public Void doInBackground() {

			return null;
		}

		@Override
		public void done() { //Feito!!!
			Toolkit.getDefaultToolkit().beep();
			setCursor(null); //desliga o wait do cursor 
		}
	}

	public WinPopUpConfig() {
		super(new BorderLayout());
		addFrameContent();
	}

	//@Override
	public void actionPerformed(ActionEvent e) {
		String a = e.getActionCommand();
		MsgBox.info(a);
		switch(e.getActionCommand()) {
		case ("OK"): 

			MsgBox.info("OK - caught you!");

		break;
		default:
			// nada a fazer
			break;
		}

	}

/*
	botaoOK.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e) { 
			selectionButtonPressed();
			String a = e.getPropertyName();
			MsgBox.info("selectionButtonPressed " + a);
		} 
	}	);

	//selectionButtonPressed()
*/

	//@Override
	public void propertyChange(PropertyChangeEvent e) {
		String a = e.getPropertyName();
		MsgBox.info("PropertyChangeEvent " + a);
	}

	private void addFrameContent() {
		String endIPSrv = TheISCTEBay.devolveEnderecoDirectorio();
		String portSrv = String.valueOf(TheISCTEBay.devolvePortoDiretorio());
		String endIPCli = "IP do cliente (localhost)";
		try {
			endIPCli = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String portCli = String.valueOf(TheISCTEBay.devolvePortoUtilizador());
		String transferCli = "..\\Transfer";
		// ***************************************************
		// Defini��o de Font(s)
		Font fontTitulos = new Font("Lucida Sans Serif", Font.BOLD, 24);
		Font fonteLabels = new Font("Lucida Sans Serif", Font.BOLD, 16);
		Font fonteDados = new Font("Lucida Sans Serif", Font.BOLD, 16);
		// ***************************************************
		// Defini��o de Border(s)
		Border borderPainelPequeno = BorderFactory.createEmptyBorder(10, 10, 10, 10); //cima, esquerda, baixo, direita
		//Border borderPainelPequeno = BorderFactory.createBevelBorder(BevelBorder.RAISED); 
		Border borderPainelBase = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border insideBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		// ***************************************************
		//Defini��o de Dimension(s)
		Dimension dimensionPequeno = new Dimension((W -120), (H/10 + 20));
		// ***************************************************
		// -------- SERVIDOR --------
		// T�tulo servidor
		JLabel lblServer = new JLabel();
		lblServer.setText("Servidor");
		lblServer.setHorizontalAlignment(SwingConstants.CENTER);
		lblServer.setFont(fontTitulos);
		lblServer.setHorizontalAlignment(SwingConstants.CENTER);

		// T�tulo do Endere�o IP do Servidor 
		JLabel lblendIPSrv = new JLabel();
		lblendIPSrv.setText("Endere�o IP: ");
		lblendIPSrv.setFont(fonteLabels);
		lblendIPSrv.setBorder(borderPainelPequeno);
		lblendIPSrv.setHorizontalAlignment(SwingConstants.LEFT);
		//lblendIPSrv.setForeground(getForeground());

		// Dados do Endere�o IP do Servidor 
		JLabel txtFieldIPSrv = new JLabel();
		txtFieldIPSrv.setFont(fonteDados);
		txtFieldIPSrv.setText(endIPSrv);
		txtFieldIPSrv.setBorder(borderPainelPequeno);
		txtFieldIPSrv.setHorizontalAlignment(SwingConstants.LEFT);

		// T�tulo do Porto do Servidor 
		JLabel lblportSrv = new JLabel();
		lblportSrv.setText("Porto: ");
		lblportSrv.setFont(fonteLabels);
		lblportSrv.setBorder(borderPainelPequeno);
		lblportSrv.setHorizontalAlignment(SwingConstants.LEFT);

		// Dados do Porto do Servidor 
		JLabel txtFieldPortoSrv = new JLabel();
		txtFieldPortoSrv.setFont(fonteDados);
		txtFieldPortoSrv.setText(portSrv);
		txtFieldPortoSrv.setBorder(borderPainelPequeno);
		txtFieldPortoSrv.setHorizontalAlignment(SwingConstants.LEFT);

		//------------------ PAIN�IS --------------------------------
		//painel pequeno - T�tulo Servidor
		JPanel painelT�tuloServidor = new JPanel();
		painelT�tuloServidor.setLayout(new GridLayout(1,1));
		painelT�tuloServidor.setPreferredSize(dimensionPequeno);
		painelT�tuloServidor.setForeground(Color.GRAY);
		painelT�tuloServidor.setBorder(insideBorder);
		//painelT�tuloServidor.setBackground(Color.lightGray);
		painelT�tuloServidor.add(lblServer);

		//painel pequeno - IP Servidor
		JPanel painelIPServidor = new JPanel();
		painelIPServidor.setLayout(new GridLayout(1,2));
		painelIPServidor.setPreferredSize(dimensionPequeno);
		painelIPServidor.setBorder(insideBorder); 
		painelIPServidor.add(lblendIPSrv);
		painelIPServidor.add(txtFieldIPSrv);

		//painel pequeno - Porto Servidor
		JPanel painelPortoServidor = new JPanel();
		painelPortoServidor.setLayout(new GridLayout(1,2));
		//painelPortoServidor.setAlignmentY(LEFT_ALIGNMENT);
		//painelPortoServidor.setLayout(new FlowLayout());
		painelPortoServidor.setPreferredSize(dimensionPequeno);
		painelPortoServidor.setBorder(insideBorder); 
		painelPortoServidor.add(lblportSrv);
		painelPortoServidor.add(txtFieldPortoSrv);

		// Painel base
		JPanel painelBase = new JPanel();
		painelBase.setBackground(Color.GRAY);
		painelBase.setPreferredSize(new Dimension(W, H));
		painelBase.setBorder(borderPainelPequeno); 

		// Painel de cima - SERVIDOR
		JPanel painelServidor = new JPanel();
		painelServidor.setLayout(new GridLayout(3,1)); 
		painelServidor.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));
		painelServidor.add(painelT�tuloServidor);
		painelServidor.add(painelIPServidor);
		painelServidor.add(painelPortoServidor);

		// COLOCAR AQUI UM SEPARADOR

		// -------- CLIENTE --------
		// T�tulo Cliente
		JLabel lblCliente = new JLabel();
		lblCliente.setText("Cliente");
		lblCliente.setFont(fontTitulos);
		lblCliente.setHorizontalAlignment(SwingConstants.CENTER);

		// T�tulo do Endere�o IP do Cliente 
		JLabel lblendIPCli = new JLabel();
		lblendIPCli.setText("Endere�o IP: ");
		lblendIPCli.setFont(fonteLabels);
		lblendIPCli.setHorizontalAlignment(SwingConstants.LEFT);
		lblendIPCli.setBorder(borderPainelPequeno);

		// Dados do Endere�o IP do Cliente 
		JLabel txtFieldIPCli = new JLabel();
		txtFieldIPCli.setFont(fonteDados);
		txtFieldIPCli.setText(endIPCli);
		txtFieldIPCli.setHorizontalAlignment(SwingConstants.LEFT);

		// T�tulo do Porto do Cliente 
		JLabel lblportCli = new JLabel();
		lblportCli.setText("Porto: ");
		lblportCli.setFont(fonteLabels);
		lblportCli.setHorizontalAlignment(SwingConstants.LEFT);
		lblportCli.setBorder(borderPainelPequeno);

		// Dados do Porto do Cliente 
		JTextField txtFieldPortCli = new JTextField();
		txtFieldPortCli.setFont(fonteDados);
		txtFieldPortCli.setText(portCli);
		txtFieldPortCli.setHorizontalAlignment(SwingConstants.LEFT);

		// T�tulo da Localiza��o das transfer�ncias no Cliente
		JLabel lblTransfer = new JLabel();
		lblTransfer.setText("Transfer�ncias: ");
		lblTransfer.setFont(fonteLabels);
		lblTransfer.setHorizontalAlignment(SwingConstants.LEFT);
		lblTransfer.setBorder(borderPainelPequeno);

		// Dados da Localiza��o das transfer�ncias no Cliente 
		JTextField txtFieldTransfer = new JTextField();
		txtFieldTransfer.setFont(fonteDados);
		txtFieldTransfer.setText(transferCli);
		txtFieldTransfer.setHorizontalAlignment(SwingConstants.LEFT);

		//painel pequeno - T�tulo Cliente
		JPanel painelT�tuloCliente = new JPanel();
		painelT�tuloCliente.setLayout(new GridLayout(1,1)); 
		painelT�tuloCliente.setForeground(Color.GRAY);
		painelT�tuloCliente.setPreferredSize(dimensionPequeno);
		painelT�tuloCliente.setBorder(insideBorder);
		painelT�tuloCliente.add(lblCliente);

		//painel pequeno - IP Cliente
		JPanel painelIPCliente = new JPanel();
		painelIPCliente.setLayout(new GridLayout(1,3)); // 1 linha, 2 colunas
		painelIPCliente.setPreferredSize(dimensionPequeno);
		painelIPCliente.setBorder(insideBorder);
		painelIPCliente.add(lblendIPCli);
		painelIPCliente.add(txtFieldIPCli);

		//painel pequeno - Porto Cliente
		JPanel painelPortoCliente = new JPanel();
		painelPortoCliente.setLayout(new GridLayout(1,3)); 
		painelPortoCliente.setPreferredSize(dimensionPequeno);
		painelPortoCliente.setBorder(insideBorder);
		painelPortoCliente.add(lblportCli);
		painelPortoCliente.add(txtFieldPortCli);

		//painel pequeno - Transfer Cliente
		JPanel painelTransfer = new JPanel();
		painelTransfer.setLayout(new GridLayout(1,3));
		painelTransfer.setPreferredSize(dimensionPequeno);
		painelTransfer.setBorder(insideBorder);
		painelTransfer.add(lblTransfer);
		painelTransfer.add(txtFieldTransfer);

		// Painel de baixo - CLIENTE
		JPanel painelCliente = new JPanel();
		painelCliente.setLayout(new GridLayout(4,1));
		painelCliente.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));
		painelCliente.add(painelT�tuloCliente);
		painelCliente.add(painelIPCliente);
		painelCliente.add(painelPortoCliente);
		painelCliente.add(painelTransfer);

		// Bot�o "OK"
		botaoOK = new JButton("OK");
		botaoOK.setFont(fontTitulos);
		botaoOK.setActionCommand("OK");
		botaoOK.setPreferredSize(new Dimension(W / 8 * 3, H / 10));
		//botaoOK.addActionListener((ActionListener) this);
		botaoOK.setHorizontalAlignment(SwingConstants.CENTER);

		botaoOK.addActionListener(okActionListener);
		JPanel painelOK = new JPanel();
		painelOK.setLayout(new GridLayout(1, 1));
		painelOK.add(botaoOK);
		painelOK.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));

		// Carregamento final
		add(painelServidor, BorderLayout.NORTH);
		add(painelCliente, BorderLayout.WEST);
		add(painelOK, BorderLayout.SOUTH);
	}

}