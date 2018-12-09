package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class WinPopUpConfig extends JPanel {
	
	private static final long serialVersionUID = 1L;
	/** Trata a janela PopUp de Configurações.
	 *  Projeto: The ISCTE Bay
	 */
	
	private static final int W = 400;
	private static final int H = 400;
	
	public WinPopUpConfig() {
		super(new BorderLayout());
		
		String endIPSrv = TheISCTEBay.devolveEnderecoDirectorio();
		String portSrv = String.valueOf(TheISCTEBay.devolvePortoDiretorio());
		String endIPCli="";
		try {
			endIPCli = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String portCli = String.valueOf(TheISCTEBay.devolvePortoUtilizador());
		String transferCli = TheISCTEBay.pastaTransferencias;
		
		// ***************************************************
		// Definição de Font(s)
		Font fontTitulos = new Font("Lucida Sans Serif", Font.TYPE1_FONT, 16);
		Font fonteLabels = new Font("Lucida Sans Serif", Font.PLAIN, 12);
		Font fonteDados = new Font("Lucida Sans Serif", Font.BOLD, 12);
		
		// ***************************************************
		// Definição de Border(s)
		Border borderPainelPequeno = BorderFactory.createEmptyBorder(10, 10, 10, 10); //cima, esquerda, baixo, direita
		Border borderPainelBase = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border insideBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		
		// ***************************************************
		//Definição de Dimension(s)
		Dimension dimensionPequeno = new Dimension((W -120), (H/10 + 20));
		
		// ***************************************************
		// -------- DIRETÓRIO --------
		// Título diretório
		JLabel lblServer = new JLabel();
		lblServer.setText("Diretório");
		lblServer.setHorizontalAlignment(SwingConstants.LEFT);
		lblServer.setFont(fontTitulos);
		lblServer.setHorizontalAlignment(SwingConstants.LEFT);

		// Título do Endereço IP do Servidor 
		JLabel lblendIPSrv = new JLabel();
		lblendIPSrv.setText("Endereço IP: ");
		lblendIPSrv.setFont(fonteLabels);
		lblendIPSrv.setBorder(borderPainelPequeno);
		lblendIPSrv.setHorizontalAlignment(SwingConstants.LEFT);

		// Dados do Endereço IP do Servidor 
		JLabel txtFieldIPSrv = new JLabel();
		txtFieldIPSrv.setFont(fonteDados);
		txtFieldIPSrv.setText(endIPSrv);
		txtFieldIPSrv.setBorder(borderPainelPequeno);
		txtFieldIPSrv.setHorizontalAlignment(SwingConstants.LEFT);

		// Título do Porto do Servidor 
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

		//------------------ PAINÉIS --------------------------------
		//painel pequeno - Título Servidor
		JPanel painelTítuloServidor = new JPanel();
		painelTítuloServidor.setLayout(new GridLayout(1,1));
		painelTítuloServidor.setPreferredSize(dimensionPequeno);
		painelTítuloServidor.setForeground(Color.GRAY);
		painelTítuloServidor.setBorder(insideBorder);
		painelTítuloServidor.add(lblServer);

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
		painelServidor.add(painelTítuloServidor);
		painelServidor.add(painelIPServidor);
		painelServidor.add(painelPortoServidor);

		// -------- CLIENTE --------
		// Título Cliente
		JLabel lblCliente = new JLabel();
		lblCliente.setText("Cliente");
		lblCliente.setFont(fontTitulos);
		lblCliente.setHorizontalAlignment(SwingConstants.LEFT);

		// Título do Endereço IP do Cliente 
		JLabel lblendIPCli = new JLabel();
		lblendIPCli.setText("Endereço IP: ");
		lblendIPCli.setFont(fonteLabels);
		lblendIPCli.setHorizontalAlignment(SwingConstants.LEFT);
		lblendIPCli.setBorder(borderPainelPequeno);

		// Dados do Endereço IP do Cliente 
		JLabel txtFieldIPCli = new JLabel();
		txtFieldIPCli.setFont(fonteDados);
		txtFieldIPCli.setText(endIPCli);
		txtFieldIPCli.setHorizontalAlignment(SwingConstants.LEFT);

		// Título do Porto do Cliente 
		JLabel lblportCli = new JLabel();
		lblportCli.setText("Porto: ");
		lblportCli.setFont(fonteLabels);
		lblportCli.setHorizontalAlignment(SwingConstants.LEFT);
		lblportCli.setBorder(borderPainelPequeno);

		// Dados do Porto do Cliente 
		JLabel txtFieldPortCli = new JLabel();
		txtFieldPortCli.setFont(fonteDados);
		txtFieldPortCli.setText(portCli);
		txtFieldPortCli.setHorizontalAlignment(SwingConstants.LEFT);

		// Título da Localização das transferências no Cliente
		JLabel lblTransfer = new JLabel();
		lblTransfer.setText("Transferências: ");
		lblTransfer.setFont(fonteLabels);
		lblTransfer.setHorizontalAlignment(SwingConstants.LEFT);
		lblTransfer.setBorder(borderPainelPequeno);

		// Dados da Localização das transferências no Cliente 
		JLabel txtFieldTransfer = new JLabel();
		//JTextField txtFieldTransfer = new JTextField();
		txtFieldTransfer.setFont(fonteDados);
		txtFieldTransfer.setText(transferCli);
		txtFieldTransfer.setHorizontalAlignment(SwingConstants.LEFT);

		//painel pequeno - Título Cliente
		JPanel painelTítuloCliente = new JPanel();
		painelTítuloCliente.setLayout(new GridLayout(1,1)); 
		painelTítuloCliente.setForeground(Color.GRAY);
		painelTítuloCliente.setPreferredSize(dimensionPequeno);
		painelTítuloCliente.setBorder(insideBorder);
		painelTítuloCliente.add(lblCliente);

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
		painelCliente.add(painelTítuloCliente);
		painelCliente.add(painelIPCliente);
		painelCliente.add(painelPortoCliente);
		painelCliente.add(painelTransfer);

		// Carregamento final
		add(painelServidor, BorderLayout.NORTH);
		add(painelCliente, BorderLayout.WEST);
	}

}
