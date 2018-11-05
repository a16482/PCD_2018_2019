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

public class IGDownload extends JPanel implements ActionListener, PropertyChangeListener  {
	private static final long serialVersionUID = 1L;

	//--------------------------------------------	
	// Variaveis e Constantes da IGDownload
	//--------------------------------------------
	private JProgressBar barraDeProgresso;
	private JButton botaoDescarregar;
	private JButton botaoProcurar;
	private JLabel lblTexto;
	private JTextField txtField;
	private JList<String> listaFiles;
	private JScrollPane listScroller;
	private Task tarefa;
	private static final String NEWLINE = "%n";
	
	class Task extends SwingWorker<Void, Void> {
	

		//private Object icon;

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
				//Tornar o progresso random.
				progresso += random.nextInt(10);
				setProgress(Math.min(progresso, 100));
			}
			return null;
		}

		//---------------------------------------------------
		// Executado no evento de despachar a thread
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
					NEWLINE + "fake fake fake: Fornecedor[endereço=/127.0.0.1, porto=8082]:253" +
					NEWLINE + "fake fake fake: Fornecedor[endereço=/127.0.0.1, porto=8082]:253" +
					NEWLINE + "fake fake fake: Fornecedor[endereço=/127.0.0.1, porto=8082]:253" +
					NEWLINE + "fake fake fake: Fornecedor[endereço=/127.0.0.1, porto=8082]:253" 
					, "Descarga completa");    
		}
	}

	public IGDownload( int W,  int H) {

		super(new BorderLayout());
		//---------------------------------------------------
		//Criação da Interface IGDownload
		//---------------------------------------------------
		//Painel superior     
		lblTexto = new JLabel("Texto a procurar: ");
		Font fontLblTexto = new Font("Lucida Sans Serif", Font.BOLD, 16);
		lblTexto.setFont(fontLblTexto);
		lblTexto.setForeground(Color.DARK_GRAY);
		lblTexto.setVerticalAlignment(SwingConstants.CENTER);
		lblTexto.setHorizontalAlignment(SwingConstants.LEFT);
		txtField = new JTextField();
		Font fonttxtField = new Font("Lucida Sans Serif", Font.PLAIN, 16);
		txtField.setFont(fonttxtField);
		txtField.setText("");
		// txtField.setPreferredSize(new Dimension (300, 40));
		txtField.setHorizontalAlignment(SwingConstants.LEFT);
		txtField.setSelectedTextColor(Color.DARK_GRAY);

		botaoProcurar = new JButton("Procurar");
		Font fontBotaoProcurar = new Font("Lucida Sans Serif", Font.BOLD, 16);
		botaoProcurar.setFont(fontBotaoProcurar);
		botaoProcurar.setActionCommand("Procurar");
		botaoProcurar.setPreferredSize(new Dimension(W / 8 * 3, H / 10));
		botaoProcurar.addActionListener((ActionListener) this);
		//botaoProcurar.addActionListener(this);

		JPanel painelProcura = new JPanel();
		painelProcura.setLayout(new BorderLayout());
		painelProcura.add(lblTexto, BorderLayout.WEST);
		painelProcura.add(txtField, BorderLayout.CENTER);
		painelProcura.add(botaoProcurar, BorderLayout.EAST);
		painelProcura.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		// -----------------------------------------------------------------------
		// Criação dos elementos da GUI relacionados com a lista de ficheiros
		// --------------------------------------------------------------------------------------------
		// ATENÇÃO: Substituir o array de string no processo de carregamento....
		//--------------------------------------------------------------------------------------------
		String[] searchResult = {"aaaa", "bbbbb", "ccccc", "d", "0001", "f", "g","h", "i", "j", "k", "l" +
				"aaaa", "bbbbb", "ccccc", "d", "0001", "f", "g","h", "i", "j", "k", "l"};
		listScroller = new JScrollPane();
		listaFiles = new JList<String>(searchResult);
		listaFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaFiles.setLayoutOrientation(JList.VERTICAL);
		listaFiles.setVisibleRowCount(-1); 	
		//listaFiles.setFixedCellHeight(H/10);
		listaFiles.setFont(new Font("Lucida Sans Serif", Font.PLAIN, 16));
		listScroller.setViewportView(listaFiles);
		listScroller.setPreferredSize(new Dimension (W/9*5, H));
		listScroller.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); //cima;esquerda;baixo;direita

		JPanel painelFicheiros = new JPanel();
		painelFicheiros.setPreferredSize(new Dimension((W / 9 * 5), (H/2)));
		painelFicheiros.setLayout(new BorderLayout());
		painelFicheiros.add(listScroller, BorderLayout.NORTH);
		painelFicheiros.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		// -----------------------------------------------------------------------
		// Criação dos elementos da GUI relacionados com a barra de progresso..
		// -----------------------------------------------------------------------
		botaoDescarregar = new JButton("Descarregar");
		Font fontBotaoDescarregar = new Font("Lucida Sans Serif", Font.BOLD, 16);
		botaoDescarregar.setFont(fontBotaoDescarregar);
		botaoDescarregar.setActionCommand("Descarregar");
		botaoDescarregar.setPreferredSize(new Dimension(W / 8 * 3, H / 4));
		botaoDescarregar.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		botaoDescarregar.addActionListener(this);

		barraDeProgresso = new JProgressBar(0, 100);
		Font fontbarraDeProgresso = new Font("Lucida Sans Serif", Font.BOLD, 18);
		barraDeProgresso.setFont(fontbarraDeProgresso);
		barraDeProgresso.setValue(0);
		barraDeProgresso.setPreferredSize(new Dimension(W / 8 * 3, H / 4));
		barraDeProgresso.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		barraDeProgresso.setForeground(Color.LIGHT_GRAY);
		barraDeProgresso.setStringPainted(true);

		JPanel painelProgresso = new JPanel();
		painelProgresso.setLayout(new GridLayout(2, 1));
		painelProgresso.add(botaoDescarregar);
		painelProgresso.add(barraDeProgresso);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		add(painelProcura, BorderLayout.NORTH);
		add(painelFicheiros, BorderLayout.WEST);
		add(painelProgresso, BorderLayout.EAST);
		// ---------------------------------------------
		//Fim da definição do corpo da janela IGDownload 
		// ---------------------------------------------
	}
	public static JPopupMenu setPopUpMenu(){

		JPopupMenu pmenu = new JPopupMenu("PopUpMenu");
		ActionListener actionListener = new PopUpActionListener();
		
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
				MsgBox.info("ui");
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