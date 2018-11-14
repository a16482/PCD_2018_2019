package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class WinDownload extends JPanel implements ActionListener, PropertyChangeListener  {
	private static final long serialVersionUID = 1L;

	//--------------------------------------------	
	// Variaveis e Constantes da IGDownload
	//--------------------------------------------
	private JProgressBar barraDeProgresso;
	private JButton botaoDescarregar;
	private JButton botaoProcurar;
	private JLabel lblTexto;
	private JTextField txtField;
	
	//private DefaultListModel<String> files;
	private JList<String> listaFiles;
	private JScrollPane listaFilesScroller;
	
	private Task tarefa;
	private JPanel painelBase;
	private JPanel painelProcura;
	private JPanel painelBotaoProcurar;
	private JPanel painelSuperior;
	private JPanel painelFicheiros;
	private JPanel painelProgresso;
	private String palavraChave;
	
	private static final int W = 600;
	private static final int H = 400;
	private static final String NEW_LINE = "\n";
	private Diretorio d;

	class Task extends SwingWorker<Void, Void> {

		@Override
		public Void doInBackground() {
			//--------------------------------------
			Random random = new Random();  //--> ALTERAR!
			//--------------------------------------

			int progresso = 0;
			//Inicializa a propriedade "progress".
			setProgress(0);
			while (progresso < 100) {
				//Sleep até 1 segundo.
				try {
					Thread.sleep(random.nextInt(1000));
				} catch (InterruptedException ignore) {}
				//--> ALTERAR ISTO!
				//Torna o progresso random.
				progresso += random.nextInt(10);
				setProgress(Math.min(progresso, 100));
			}
			return null;
		}

		//---------------------------------------------------
		// Executado na finalização da thread
		//---------------------------------------------------
		@Override
		public void done() { //Feito!!!
			Toolkit.getDefaultToolkit().beep();
			botaoDescarregar.setEnabled(true);
			botaoProcurar.setEnabled(true);
			txtField.setEnabled(true);
			listaFiles.setEnabled(true);
			setCursor(null); //desliga o wait do cursor

			MsgBox.info(
					"Descarga completa." + 
							NEW_LINE + "fake fake fake: Fornecedor[endereço=/127.0.0.1, porto=8082]:253" +
							NEW_LINE + "fake fake fake: Fornecedor[endereço=/127.0.0.1, porto=8082]:253" +
							NEW_LINE + "fake fake fake: Fornecedor[endereço=/127.0.0.1, porto=8082]:253" +
							NEW_LINE + "fake fake fake: Fornecedor[endereço=/127.0.0.1, porto=8082]:253" 
							, "Descarga completa");    
		}
	}

	public WinDownload(Diretorio diretorio) {
		super(new BorderLayout());
		d = diretorio;
		//---------------------------------------------------
		//Criação da Interface IGDownload
		//---------------------------------------------------
		// Definição de Font(s)
		Font fontTitulos = new Font("Lucida Sans Serif", Font.BOLD, 14);
		Font fontDados = new Font("Lucida Sans Serif", Font.PLAIN, 14);

		// Definição de medidas de acerto dos elementos da IG
		int larguraPainelNWest= W / 9 * 6;
		int larguraPainelNEast= W / 8 * 3;
		int larguraPainelSWest= W / 9 * 6;
		int larguraPainelSEast= W / 8 * 3;

		int alturaPainelNWest= H / 10;
		int alturaPainelNEast= H / 10;
		int alturaPainelSWest= H / 5 * 4;
		int alturaPainelSEast= H / 4;

		//Elementos do Painel superior esquerdo     
		lblTexto = new JLabel("Texto a procurar: ");
		lblTexto.setFont(fontTitulos);
		lblTexto.setForeground(Color.DARK_GRAY);
		lblTexto.setVerticalAlignment(SwingConstants.CENTER);
		lblTexto.setHorizontalAlignment(SwingConstants.LEFT);
		txtField = new JTextField();
		txtField.setFont(fontDados);
		txtField.setText("");
		txtField.setHorizontalAlignment(SwingConstants.LEFT);
		txtField.setSelectedTextColor(Color.DARK_GRAY);

		//Elementos do Painel superior direito 
		botaoProcurar = new JButton("Procurar");;
		botaoProcurar.setFont(fontTitulos);
		botaoProcurar.setActionCommand("Procurar");
		botaoProcurar.setPreferredSize(new Dimension((larguraPainelNEast + 0), alturaPainelNEast + 0));
		botaoProcurar.addActionListener((ActionListener) this);

		//Painel base
		painelBase = new JPanel();
		painelBase.setBackground(Color.darkGray);
		painelBase.setPreferredSize(new Dimension(W, H));
		painelBase.setLayout(new BorderLayout());
		Border borderPainelBase = BorderFactory.createEmptyBorder(5, 5, 5, 5);

		//Painel de Procura - superior esquerdo (NWest)
		painelProcura = new JPanel();
		painelProcura.setPreferredSize(new Dimension((larguraPainelNWest + 90), (alturaPainelNWest + 10)));
		painelProcura.setLayout(new BorderLayout());
		painelProcura.add(lblTexto, BorderLayout.WEST);
		painelProcura.add(txtField, BorderLayout.CENTER);
		Border insideBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		painelProcura.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));

		//Painel do Botão de Procura - superior direito (NEast)
		painelBotaoProcurar = new JPanel();
		painelBotaoProcurar.setPreferredSize(new Dimension((larguraPainelNEast + 15), (alturaPainelNEast + 40)));
		painelBotaoProcurar.setLayout(new BorderLayout());
		painelBotaoProcurar.add(botaoProcurar, BorderLayout.WEST);
		painelBotaoProcurar.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));

		//Painel superior
		painelSuperior = new JPanel();
		painelSuperior.setBackground(Color.darkGray);
		painelSuperior.setPreferredSize(new Dimension(W, (alturaPainelNEast + 10)));
		painelSuperior.setLayout(new BorderLayout());
		//Border borderPainelBase = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		painelSuperior.add(painelProcura, BorderLayout.WEST);
		painelSuperior.add(painelBotaoProcurar, BorderLayout.EAST);
		// -----------------------------------------------------------------------
		// Elementos da GUI relacionados com a lista de ficheiros
		// --------------------------------------------------------------------------------------------
		// ATENÇÃO: Substituir o array de string no processo de carregamento....
		//--------------------------------------------------------------------------------------------
		String[] searchResult = {"ficheiro A", "ficheiroB.txt", "imagemC", "imagemD", "imagemE" , 
				"oMeuFiceiro", "aMinhaFolha de cálculo toda certinha", "f", "g",
				"h", "i", "j", "k", "l"
						+ "aaaa", "bbbbb", "ccccc", "d", "0001", "f", "g","h", "i", "j", "k", "l"};
		listaFilesScroller = new JScrollPane();
		listaFiles = new JList<String>(searchResult);
		listaFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaFiles.setLayoutOrientation(JList.VERTICAL);
		listaFiles.setVisibleRowCount(-1); 	
		listaFiles.setFont(fontDados);

		listaFilesScroller.setViewportView(listaFiles);
		listaFilesScroller.setPreferredSize(new Dimension ((larguraPainelSWest + 90), (alturaPainelSWest - 10))); //H/4*3-60

		painelFicheiros = new JPanel();
		painelFicheiros.setPreferredSize(new Dimension((larguraPainelSWest + 90), (alturaPainelSWest + 0)));
		painelFicheiros.setLayout(new BorderLayout());
		painelFicheiros.add(listaFilesScroller, BorderLayout.NORTH);
		painelFicheiros.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));

		// ------------------------------------------------------------------------------------------
		// Definição dos elementos da GUI principal relacionados com a barra de progresso..
		// ------------------------------------------------------------------------------------------
		// Botão "Descarregar"
		botaoDescarregar = new JButton("Descarregar");
		botaoDescarregar.setFont(fontTitulos);
		botaoDescarregar.setActionCommand("Descarregar");
		botaoDescarregar.setPreferredSize(new Dimension((larguraPainelSEast + 0), alturaPainelSEast));
		botaoDescarregar.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		botaoDescarregar.addActionListener(this);

		// Barra de progresso
		barraDeProgresso = new JProgressBar(0, 100);
		barraDeProgresso.setFont(fontTitulos);
		barraDeProgresso.setValue(0);
		barraDeProgresso.setPreferredSize(new Dimension((larguraPainelSEast + 0), alturaPainelSEast));
		barraDeProgresso.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		barraDeProgresso.setForeground(Color.LIGHT_GRAY);
		barraDeProgresso.setStringPainted(true);

		// Painel de progresso
		painelProgresso = new JPanel();
		painelProgresso.setLayout(new GridLayout(2, 1));
		painelProgresso.add(botaoDescarregar);
		painelProgresso.add(barraDeProgresso);
		painelProgresso.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));

		// frame
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(painelSuperior, BorderLayout.NORTH);
		add(painelFicheiros, BorderLayout.WEST);
		add(painelProgresso, BorderLayout.EAST);
	}

	public static JPopupMenu setPopUpMenu(){

		JPopupMenu pmenu = new JPopupMenu("PopUpMenu");
		ActionListener actionListener = new PopUpActionListener();
		pmenu.setBorder( BorderFactory.createLineBorder(Color.black, 1));

		JMenuItem menuItem1 = new JMenuItem("Utilizadores");
		menuItem1.setActionCommand("Utilizadores");
		menuItem1.addActionListener(actionListener);
		pmenu.add(menuItem1);
		pmenu.addSeparator();
		JMenuItem menuItem2 = new JMenuItem("Configurações");
		menuItem2.setActionCommand("Configurações");
		menuItem2.addActionListener(actionListener);
		pmenu.add(menuItem2);

		return pmenu;
	}
	
	public class ProcuraFicheiros<palavraChave> implements Runnable{
		private Diretorio dir;
		private ArrayList<Utilizador> listaUtilizadores = new ArrayList<Utilizador>();
		
		public ProcuraFicheiros (palavraChave parameter){
		}
		
		public void run() {
			dir.consultaUtilizadores();  //recarrega a lista de utilizadores no Diretor
			synchronized(this) {
				try {
					percorreListaUtilizadores(dir);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					notifyAll();
				}
			}
			
		}
		private synchronized void percorreListaUtilizadores(Diretorio d) throws InterruptedException {
			Utilizador utilizadorLista;
			String endIPUtilizadorLista="";
			String portoUtilizadorLista="";
	
			Iterator<Utilizador> iListaUtilizadores = listaUtilizadores.iterator();
			while (iListaUtilizadores.hasNext()) {
				utilizadorLista = iListaUtilizadores.next();
				endIPUtilizadorLista=utilizadorLista.ipUtilizador();
				portoUtilizadorLista=utilizadorLista.portoUtilizador();
				lookUpForFiles(endIPUtilizadorLista, portoUtilizadorLista, palavraChave);
			}
		}
		
		public void lookUpForFiles(String endIPUtilizador, String portoUtilizador, String palavraChave) {
			
		}
	}	

	// ------------------------------------------------------------------------
	// Invocado quando o utilizador prime o botão "Descarregar" ou "Procurar".
	// ------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent evt) {
		switch(evt.getActionCommand()) {
		case ("Descarregar"): 
			botaoDescarregar.setEnabled(false);
			botaoProcurar.setEnabled(false);
			txtField.setEnabled(false);
			listaFiles.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			// As instâncias do javax.swing.SwingWorker não são reutilizáveis.
			// Por isso, cria-se uma nova, à medida do necessário.
			tarefa = new Task();
			tarefa.addPropertyChangeListener(this); // sentinela
			tarefa.execute();
			break;
		case ("Procurar"):
			palavraChave= txtField.getText();
			System.out.println(palavraChave);
			WordSearchMessage wsm = new WordSearchMessage(palavraChave);
			d.pesquisaFicheiroNoutrosUtilizadores(wsm);
//			Thread t = new Thread(new ProcuraFicheiros<String>(palavraChave));
//		    t.start();
			MsgBox.info("Aqui vai funcionar a procura dos ficheiros - em construção...");
			break;
		default:
			// nada a fazer
			break;
		}
	}

	// ------------------------------------------------------------------------
	// Invocado sempre que a propriedade de progresso da tarefa é alterada
	// ------------------------------------------------------------------------
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			barraDeProgresso.setValue(progress);
		}

	}

}