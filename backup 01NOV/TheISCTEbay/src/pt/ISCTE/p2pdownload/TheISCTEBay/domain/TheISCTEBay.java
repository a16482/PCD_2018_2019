package pt.ISCTE.p2pdownload.TheISCTEBay.domain;

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
import java.util.*;
import java.util.Random;


//import javax.swing.*;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

public class TheISCTEBay extends JPanel implements ActionListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	private static int W = 800;
	private static int H = 400;
	
	private static String enderecoDiretorio;
	private static int portoDiretorio;
	private static int portoUtilizador;
	private static String pastaTransferencias;
    private static Diretorio d;

	private JProgressBar barraDeProgresso;
	private JButton botaoDescarregar;
	private JButton botaoProcurar;
	// private JTextArea tarefaOutput;
	private Task tarefa;
	private JLabel lblTexto;
	private JTextField txtField;
	private JList<String> listaFiles;
	private JScrollPane listScroller;
	
	//-------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------
//	class Task extends SwingWorker<Void, Void> {
//		@Override
//		public Void doInBackground() {
//			// --------------------------------------
//			Random random = new Random(); // --> ALTERAR!
//			// --------------------------------------
//
//			int progresso = 0;
//			// Inicializa a propriedade "progress".
//			setProgress(0);
//			while (progresso < 100) {
//				// Sleep at� 1 segundo.
//				try {
//					Thread.sleep(random.nextInt(1000));
//				} catch (InterruptedException ignore) {
//				}
//				// --> ALTERAR ISTO!
//				// Tornar o progresso random.
//				progresso += random.nextInt(10);
//				setProgress(Math.min(progresso, 100));
//			}
//			return null;
//		}
//
//		// ---------------------------------------------------
//		// Executado no evento de despachar a thread
//		// ---------------------------------------------------
//		@Override
//		public void done() {
//			Toolkit.getDefaultToolkit().beep();
//			botaoDescarregar.setEnabled(true);
//			botaoProcurar.setEnabled(true);
//			txtField.setEnabled(true);
//			listaFiles.setEnabled(true);
//			setCursor(null); // desliga o wait do cursor
//		}
//		
		
//	    private JButton botaoDescarregar;
//	    private JButton botaoProcurar;
//	    //private JTextArea tarefaOutput;
//	    private Task tarefa;
//	    private JLabel lblTexto;
//	    //private JLabel lblTitulo;
//	    private JTextField txtField;
//	    private JList<String> listaFiles;
//	    private JScrollPane listScroller;
//		-------------------------------------------------------------------------------------------------------
//		-------------------------------------------------------------------------------------------------------
//		-------------------------------------------------------------------------------------------------------
	 
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
                //Sleep at� 1 segundo.
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
            // Mostra o que foi feito ao Cliente:
            //ImageIcon icon = createImageIcon("images/middle.gif","this is a caption");
            ImageIcon iconeInfo = createImageIcon("../Icon/iconInfo.png", "Informa��o");
            MsgBox.show("YOUR INFORMATION HERE", "TITLE BAR MESSAGE", 0);    
		}
	}

//	public TheISCTEBay() {
//		// ---------------------------------------------------
//		// Cria��o da Interface com o utilizador (GUI)
//		// ---------------------------------------------------
//
//		super(new BorderLayout());

//	lblTexto = new JLabel("Texto a procurar: ");
//
//        MsgBox.info("YOUR INFORMATION HERE", "TITLE BAR MESSAGE");
//
//        
//    }

//		private ImageIcon createImageIcon(String string) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//    }
 
    public TheISCTEBay() {
       	//---------------------------------------------------
        //Cria��o da Interface com o utilizador (GUI)
       	//---------------------------------------------------
        super(new BorderLayout());
        //Painel suoerior     
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
		botaoProcurar.addActionListener(this);
 
		JPanel painelProcura = new JPanel();
		painelProcura.setLayout(new BorderLayout());
		painelProcura.add(lblTexto, BorderLayout.WEST);
		painelProcura.add(txtField, BorderLayout.CENTER);
		painelProcura.add(botaoProcurar, BorderLayout.EAST);
		painelProcura.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		// Cria��o dos elementos da GUI relacionados com a lista de ficheiros
		// --------------------------------------------------------------------------------------------
		// Substituir pelo processo de carregamento....
		//String[] searchResult = {"aaaa", "bbbbb","ccccc", "ddddddddddd","eeee", "f", "gggggggggggggg", "hhhhh"}; // Array de Strings
		//--------------------------------------------------------------------------------------------
		String[] searchResult = {};
		listScroller = new JScrollPane();
		listaFiles = new JList<String>(searchResult);
		listaFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// 3 hip�teses: a)SINGLE_SELECTION; b) SINGLE_INTERVAL_SELECTION; c)
		// MULTIPLE_INTERVAL_SELECTION
		listaFiles.setLayoutOrientation(JList.VERTICAL);
		listaFiles.setVisibleRowCount(-1);
		listaFiles.setFixedCellHeight(H / 10);

		JPanel painelFicheiros = new JPanel();
		painelFicheiros.setPreferredSize(new Dimension(W / 2+60, H));
		painelFicheiros.setLayout(new BorderLayout());
		painelFicheiros.add(listScroller, BorderLayout.NORTH);
		painelFicheiros.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		//...
		// Cria��o dos elementos da GUI relacionados com a barra de progresso..
		botaoDescarregar = new JButton("Descarregar");
		Font fontBotaoDescarregar = new Font("Lucida Sans Serif", Font.BOLD, 16);
		botaoDescarregar.setFont(fontBotaoDescarregar);
		botaoDescarregar.setActionCommand("Descarregar");
		botaoDescarregar.setPreferredSize(new Dimension(W / 8 * 3, H / 4));
		botaoDescarregar.addActionListener(this);

		barraDeProgresso = new JProgressBar(0, 100);
		Font fontbarraDeProgresso = new Font("Lucida Sans Serif", Font.BOLD, 18);
		barraDeProgresso.setFont(fontbarraDeProgresso);
		barraDeProgresso.setValue(0);
		barraDeProgresso.setPreferredSize(new Dimension(W / 8 * 3, H / 4));
		barraDeProgresso.setForeground(Color.LIGHT_GRAY);
		barraDeProgresso.setStringPainted(true);

		listaFiles.setVisibleRowCount(-1); 	
		listaFiles.setFixedCellHeight(H/10);
		listaFiles.setFont(new Font("Lucida Sans Serif", Font.PLAIN, 14));
		listScroller.setViewportView(listaFiles);
		listScroller.setPreferredSize(new Dimension (W/8*3, H/3*2-10));
    	//...
        //-----------------------------------------------------  
        //-----------------------------------------------------         
		  //Recuperar as linhas seguintes para a mensagem final!   
        //Formato da msg final: Fornecedor{endere�o=ip, porto=p]: blocos
        //Exemplo:
        //(�cone I) Descarga completa.
		  //Fornecedor[endere�o=/127.0.0.1, porto=8082]:253 
		  //Fornecedor[endere�o=/127.0.0.1, porto=8081]:251 
        //-----------------------------------------------------        
        //tarefaOutput = new JTextArea(5, 20);
        //tarefaOutput.setMargin(new Insets(5,5,5,5));
        //tarefaOutput.setEditable(false);
        //-----------------------------------------------------  
        //----------------------------------------------------- 

		JPanel painelProgresso = new JPanel();
		painelProgresso.setLayout(new GridLayout(2, 1));
		painelProgresso.add(botaoDescarregar);
		painelProgresso.add(barraDeProgresso);
		// add(new JScrollPane(tarefaOutput)); //aqui no GridLayout
		// add(new JScrollPane(tarefaOutput), BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		add(painelProcura, BorderLayout.NORTH);
		add(painelFicheiros, BorderLayout.WEST);
		add(painelProgresso, BorderLayout.EAST);
       	//---------------------------------------------------
        //Fim da cria��o da Interface com o utilizador (GUI)
       	//---------------------------------------------------
	}
    

//    public void valueChanged(ListSelectionEvent e) {
//        if (e.getValueIsAdjusting() == false) {
//
//            if (listaFiles.getSelectedIndex() == -1) {
//            //Nada selecionado, disable bot�o de fire
//                fireButton.setEnabled(false);
//
//            } else {
//            // Selecionado, enable do botao fire.
//                fireButton.setEnabled(true);
//            }
//        }
//    }

	// ------------------------------------------------------------------------
	// Invocado quando o utilizador prime o bot�o "Descarregar" ou "Procurar".
	// ------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand() == "Descarregar") {
			botaoDescarregar.setEnabled(false);
			botaoProcurar.setEnabled(false);
			txtField.setEnabled(false);
			listaFiles.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			// As inst�ncias do javax.swing.SwingWorker n�o s�o reutiliz�veis.
			// Por isso, cria-se uma nova, � medida do necess�rio.
			tarefa = new Task();
			tarefa.addPropertyChangeListener(this); // sentinela
			tarefa.execute();
		} else {
			// bot�o "Procurar"

		}
	}

	// ------------------------------------------------------------------------
	// Invocado sempre que a propriedade de progresso da tarefa � alterada
	// ------------------------------------------------------------------------
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			barraDeProgresso.setValue(progress);
		}
	}

	// ------------------------------------------------------------------------
	// Cria��o do GUI e coloca��o em funcionamento.
	// Corre no evento que despacha a thread.
	// ------------------------------------------------------------------------
	private static void criaEmostraGUI() {
		JFrame frame = new JFrame("The ISCTE Bay");
		frame.setPreferredSize(new Dimension(W, H)); // Isto n�o est� a funcionar!
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Cria e configura o painel de conte�dos.
		JComponent newContentPane = new TheISCTEBay();
		newContentPane.setOpaque(true); // Os pain�is de conte�dos devem ser opacos !!!
		frame.setContentPane(newContentPane);

		// Mostra a janela.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Verifica se a aplica��o iniciou com os 4 argumentos:<IP do Diret�rio> <Porto do Diret�rio> <Porto do Utilizador> <Pasta para transfer�ncias>
		if (args.length < 4) {
			System.err.println(
					"S�o necess�rios 4 argumentos: <IP do Diret�rio> <Porto do Diret�rio> <Porto do Utilizador> "
							+ "<Pasta para transfer�ncias>");
			System.exit(1);
		}
		
		//Guarda os argumentos em vari�veis da classe
		enderecoDiretorio = args[0];
		portoDiretorio = Integer.parseInt(args[1]);
		portoUtilizador = Integer.parseInt(args[2]);
		pastaTransferencias = args[3];
		
		//Mostra os arumentos recebidos na consola
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
		
		// Agenda um job para o evento de despachar a thread.
		// Cria e mostra o GUI desta aplica��o.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				criaEmostraGUI();
			}
		});
	}
}
