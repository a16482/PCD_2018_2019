package pt.iscte.P2PDownload.TheISCTEBay;


import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import java.util.List;


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
	private static final int W = 800;
	private static final int H = 400;
	
	
	// ------------------------------------------------------------------------
	// Cria��o do GUI e coloca��o em funcionamento.
	// Corre no evento que despacha a thread.
	// ------------------------------------------------------------------------
	private static void criaEmostraGUI() {
		// ---------------- Painel de Download -----------------------------
		JFrame frame = new JFrame("The ISCTE Bay");
		
		// Cria e configura o painel de conte�dos.
		JComponent DownloadContentPane = new IGDownload(W,H);
		DownloadContentPane.setOpaque(true); // Os pain�is de conte�dos devem ser opacos !!!
		frame.setContentPane(DownloadContentPane);
		
		JPopupMenu pmenu = new JPopupMenu("PopUpMenu");
		pmenu = IGDownload.setPopUpMenu();
	   

		DownloadContentPane.setComponentPopupMenu(pmenu);

		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
        frame.setLocationRelativeTo(null);
	}
	
	public static String devolveEnderecoDirectorio () {
		return enderecoDiretorio;
	}
	
	public static int devolvePortoDiretorio() {
		return portoDiretorio;
	}
	
	public static int devolvePortoUtilizador() {
		return portoUtilizador;
	}
	
	public static int devolveNumeroUtilizadores() {
		return d.getTotalUtilizadores();
	}
	
	public static List<Utilizador> devolveListaUtilizadores() {
		return d.getListaUtilizadores();
	}
	
	public static Utilizador devolveUtilizadorNDaLista(int n) {
		return d.getUtilizadorNDaLista(n);
	}
	
//	public static List<String> devolveListaUtilizadoresStr(){
//		return d.getListaUtilizadoresStr();
//	}
	
	
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
		pastaTransferencias = args[3];
		
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
