package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
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
	private JList<FileDetails> listaFiles;
	private JScrollPane listaFilesScroller;
	private JPanel painelBase;
	private JPanel painelProcura;
	private JPanel painelBotaoProcurar;
	private JPanel painelSuperior;
	private JPanel painelFicheiros;
	private JPanel painelProgresso;
	private String palavraChave;

	private DefaultListModel<FileDetails> searchResult = new DefaultListModel<FileDetails>();

	private Diretorio dir;

	private static final int W = 600;
	private static final int H = 400;
	private static final String NEW_LINE = "\n";

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
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			botaoDescarregar.setEnabled(true);
			botaoProcurar.setEnabled(true);
			txtField.setEnabled(true);
			listaFiles.setEnabled(true);
			setCursor(null); //desliga o wait do cursor 
		}
	}

	public String formataMsgFimDownload(String textoMsg, String novoTexto) {
		return textoMsg + NEW_LINE + novoTexto;
	}

	public void descarregaFicheiro(FileDetails f) {
		if(verificaSeTem(f)) {
			MsgBox.info("O ficheiro " + f.nomeFicheiro() + " já existe na pasta " + TheISCTEBay.devolvePastaTransferencias());
		}else {
			Thread t = new Download(f);
			t.start();
		}
	}

	private boolean verificaSeTem(FileDetails fch) {
		File[] files = new File("./" + TheISCTEBay.devolvePastaTransferencias()).listFiles();
		for (File file : files) {
			if (file.getName().equals(fch.nomeFicheiro()) && file.length() == fch.bytesFicheiro()) return true;
		}
		return false;
	}

	public WinDownload(Diretorio d) {

		super(new BorderLayout());
		dir = d;
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

		listaFilesScroller = new JScrollPane();

		listaFiles = new JList<FileDetails>(searchResult);

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
		// ------------------------------------------------------------------------
		// Mouse Listener para a JList
		// ------------------------------------------------------------------------
		listaFiles.addMouseListener( new MouseAdapter() {
			@SuppressWarnings("rawtypes")
			public void mouseClicked(MouseEvent mouseEvent) {
				JList listaFiles = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
					int index = listaFiles.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						FileDetails f = (FileDetails) (listaFiles.getModel().getElementAt(index));
						botaoProcurar.setEnabled(false);
						botaoDescarregar.setEnabled(false);
						descarregaFicheiro(f);
					}
				}
			}
		});


		txtField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (txtField.getText().length()>0) {
					botaoProcurar.setEnabled(true);
				}
			}
		});

		botaoDescarregar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				FileDetails f=null;
				int i =  listaFiles.getSelectedIndex();
				if (i >=0) {
					f =  (FileDetails) (listaFiles.getModel().getElementAt(i));
				}

				if (f==null) {
					MsgBox.info("Selecciona um ficheiro para descarregar");
				}else {
					botaoProcurar.setEnabled(false);
					botaoDescarregar.setEnabled(false);

					descarregaFicheiro(f);
					botaoProcurar.setEnabled(true);
					botaoDescarregar.setEnabled(true);
				}
			}
		});

		botaoProcurar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				palavraChave= txtField.getText();
				WordSearchMessage w = new WordSearchMessage(palavraChave);
				mostraListaFilesEncontrados(w);
			}
		});
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


	private void mostraListaFilesEncontrados(WordSearchMessage w) {
		searchResult.removeAllElements();
		int i=0;
		DefaultListModel<FileDetails> sr = dir.procuraFicheirosPorPalavraChave(w);
		while (i<sr.size()) {
			searchResult.addElement(sr.getElementAt(i));
			i++;
		}
		listaFilesScroller.repaint();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub


	}
}