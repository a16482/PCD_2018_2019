package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class TheISCTEBay {

	private static String enderecoDiretorio;
	private static int portoDiretorio;
	private static int portoUtilizador;
	private static String pastaTransferencias;
	private static Diretorio d;
	//private String msgErro;
	//--------------------------------------------	
	// Variaveis e Constantes da IG
	//--------------------------------------------
	private static int W = 800;
	private static int H = 400;
	//
	// ------------------------------------------------------------------------
	// Criação do GUI e colocação em funcionamento.
	// Corre no evento que despacha a thread.
	// ------------------------------------------------------------------------
	private static void criaEmostraGUI() {
		JFrame frame = new JFrame("The ISCTE Bay");
		frame.setPreferredSize(new Dimension(W, H)); // Isto não está a funcionar!
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Cria e configura o painel de conteúdos.
		JComponent newContentPane = new IGDownload();
		newContentPane.setOpaque(true); // Os painéis de conteúdos devem ser opacos !!!
		frame.setContentPane(newContentPane);

		// Mostra a janela.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Verifica se a aplicação iniciou com os 4 argumentos:<IP do Diretório> <Porto do Diretório> <Porto do Utilizador> <Pasta para transferências>
		if (args.length < 4) {
			String msgErro = "São necessários 4 argumentos: <IP do Diretório> <Porto do Diretório> <Porto do Utilizador> "
					+ "<Pasta para transferências>";
			System.err.println(msgErro);
			MsgBox.erro(msgErro);
			System.exit(1);
		}
		
		//Guarda os argumentos em variáveis da classe
		enderecoDiretorio = args[0];
		portoDiretorio = Integer.parseInt(args[1]);
		portoUtilizador = Integer.parseInt(args[2]);
		pastaTransferencias = args[3];
		
		//Mostra os arumentos recebidos na consola
		System.out.println("IP do Diretório: " + enderecoDiretorio + "\nPorto do Diretorio: " + portoDiretorio
				+ "\nPorto do Utilizador: " + portoUtilizador + "\nPasta para transferências: " + pastaTransferencias);
		
		//Instancia um diretório
		d = new Diretorio (enderecoDiretorio, portoDiretorio, portoUtilizador);
		
		//Regista-se no Diretório
		d.registoDiretorio();
		
		//Consulta a lista de utilizadores no Diretório
		d.consultaUtilizadores();
		
		List<Utilizador> listaUtilizadores = d.getListaUtilizadores();
		
		for (Utilizador u : listaUtilizadores) {
			System.out.println(u.ipUtilizador() + " " + u.portoUtilizador());
		}
		
		// Agenda um job para o evento de despachar a thread.
		// Cria e mostra o GUI desta aplicação.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				criaEmostraGUI();
			}
		});
	}
}
