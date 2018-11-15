package pt.iscte.P2PDownload.TheISCTEBay;


import java.io.File;
import java.net.InetAddress;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;


public class TheISCTEBay {

	private static String enderecoDiretorio;
	private static int portoDiretorio;
	private static int portoUtilizador;
	private static String pastaTransferencias;
	private static Diretorio d;
	private static  ServidorFicheiros fileServer;
	
	private static void fecharServidor() {
		// TODO: fechar o servidor
	}
	// ------------------------------------------------------------------------
	// Cria��o do GUI e coloca��o em funcionamento.
	// Corre no evento que despacha a thread.
	// ------------------------------------------------------------------------
		
	private static void criaEmostraGUI() {
		// ---------------- Painel de Download -----------------------------
		String idUtilizador =  devolveIPUtilizador() + ":" + String.valueOf(devolvePortoUtilizador());
				
		JFrame frame = new JFrame();
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {	
				//Remove um utilizador do Diret�rio
		    	try {
		    		fecharServidor();
					d.removeUtilizador();
					System.out.println("O Utlizador " + idUtilizador + " desligou-se e foi removido do Diret�rio.");
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} finally {
			        frame.dispose();
			        System.exit(0);
				}
		    }
		});
		
		// Cria e configura o painel de conte�dos.
		JComponent DownloadContentPane = new WinDownload(d);
		DownloadContentPane.setOpaque(true); // Os pain�is de conte�dos devem ser opacos !!!
		frame.setContentPane(DownloadContentPane);
		
		JPopupMenu pmenu = new JPopupMenu("PopUpMenu");
		pmenu = WinDownload.setPopUpMenu();
	   
		DownloadContentPane.setComponentPopupMenu(pmenu);
		
		frame.setTitle("The ISCTE Bay" + " (" + idUtilizador  + ")");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	public static String devolveEnderecoDirectorio () {
		return enderecoDiretorio;
	}
	
	public static int devolvePortoDiretorio() {
		return portoDiretorio;
	}

	public static String devolveIPUtilizador() {
		String enderecoUtilizador="";
		try {
			enderecoUtilizador = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enderecoUtilizador;
	}
	
	public static int devolvePortoUtilizador() {
		return portoUtilizador;
	}
	
	public static Diretorio devolveDiretorio() {
		return d;
	}
	
	public static String verificaPastaDeTransferencias(String dirTransfer) {
		final String pastaDefault = "Transfer"; 
		String pasta = dirTransfer;
		if ((pasta == null) || (pasta.length() ==0)) {
			pasta = pastaDefault;
		}
		
		File f = new File("./" + pasta);
		if (!f.isDirectory()) {
			MsgBox.info("A pasta de transfer�ncias indicada n�o existe atualmente." + "\n" 
					+ " A aplica��o TheISCTEBay vai criar a pasta " + pasta +  " automaticamente.");
			pasta = pastaDefault;
			f.mkdir();
		} 
		
		return pasta;
	}
	
	public static void main(String[] args) {

		//Verifica se a aplica��o iniciou com os 4 argumentos:<IP do Diret�rio> <Porto do Diret�rio> <Porto do Utilizador> <Pasta para transfer�ncias>
		if (args.length < 4) {
			String msgErro = "S�o necess�rios 4 argumentos: <IP do Diret�rio> <Porto do Diret�rio> <Porto do Utilizador> "
					+ "<Pasta para transfer�ncias>";
			System.err.println(msgErro);
			MsgBox.erro(msgErro);
			System.exit(1);
		}
		
		//Guarda os argumentos em vari�veis da classe
		enderecoDiretorio = args[0];
		portoDiretorio = Integer.parseInt(args[1]);
		portoUtilizador = Integer.parseInt(args[2]);
		pastaTransferencias = verificaPastaDeTransferencias(args[3]);
		
		//Mostra os argumentos recebidos na consola
		System.out.println("IP do Diret�rio: " + enderecoDiretorio + "\nPorto do Diretorio: " + portoDiretorio
				+ "\nPorto do Utilizador: " + portoUtilizador + "\nPasta para transfer�ncias: " + pastaTransferencias);
		
		//Instancia um diret�rio
		d = new Diretorio (enderecoDiretorio, portoDiretorio, portoUtilizador);
		
		//Regista-se no Diret�rio
		d.registoDiretorio();
		
		//Consulta a lista de utilizadores no Diret�rio
		d.consultaUtilizadores();
		
		List<Utilizador> listaUtilizadores = d.getListaUtilizadores();
		
		for (Utilizador u : listaUtilizadores) {
			System.out.println(u.ipUtilizador() + " " + u.portoUtilizador());
		}
		
		// inicia o servi�o pr�prio de servidor de ficheiros
		fileServer = new ServidorFicheiros();
		new Thread(fileServer, "ServidorFicheiros").start();
		
		
		// Agenda um job para o evento de despacho da thread.
		// Cria e mostra a GUI principal desta aplica��o.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				criaEmostraGUI();
			}
		});
	}
}
