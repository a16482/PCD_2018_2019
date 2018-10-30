package pt.iscte.p2pdownload.theISCTEbay.domain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
//import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
//import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

public class TheISCTEBay extends JPanel implements ActionListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	
	private static int  W = 800 ;
	private static int H = 400 ;
	
	private JProgressBar barraDeProgresso;
    private JButton botaoDescarregar;
    private JButton botaoProcurar;
    //private JTextArea tarefaOutput;
    private Task tarefa;
    private JLabel lblTexto;
    //private JLabel lblTitulo;
    private JTextField txtField;
    private JList<String> listaFiles;
//    private JScrollPane listScroller;
 
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
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            botaoDescarregar.setEnabled(true);
            botaoProcurar.setEnabled(true);
            txtField.setEnabled(true);
            listaFiles.setEnabled(true);
            setCursor(null); //desliga o wait do cursor
//            -----------------------------------------------------         
//  	      Recuperar as linhas seguintes para a mensagem final!
//          ----------------------------------------------------- 
//            tarefaOutput.append("Feito!\n");
//          ----------------------------------------------------- 
        }
    }
 
    public TheISCTEBay() {
       	//---------------------------------------------------
        //Criação da Interface com o utilizador (GUI)
       	//---------------------------------------------------
    	
        super(new BorderLayout());
             
        lblTexto = new JLabel("Texto a procurar: ");
		Font fontLblTexto = new Font("Lucida Sans Serif", Font.BOLD, 16);
		lblTexto.setFont(fontLblTexto);
		lblTexto.setForeground(Color.DARK_GRAY);
		lblTexto.setVerticalAlignment(JTextField.CENTER);
		lblTexto.setHorizontalAlignment(JTextField.LEFT);
		
		txtField = new JTextField();
		Font fonttxtField = new Font("Lucida Sans Serif", Font.PLAIN, 16);
		txtField.setFont(fonttxtField);
		txtField.setText("");
		//txtField.setPreferredSize(new Dimension (300, 40));
		txtField.setHorizontalAlignment(JTextField.LEFT);
		txtField.setSelectedTextColor(Color.DARK_GRAY);
		
		botaoProcurar = new JButton("Procurar");
		Font fontBotaoProcurar = new Font("Lucida Sans Serif", Font.BOLD, 16);
		botaoProcurar.setFont(fontBotaoProcurar);
		botaoProcurar.setActionCommand("Procurar");
		botaoProcurar.setPreferredSize(new Dimension (W/8*3, H/10));
		botaoProcurar.addActionListener(this);
	 
		  
		JPanel painelProcura= new JPanel();
		painelProcura.setLayout(new BorderLayout());
		painelProcura.add(lblTexto,BorderLayout.WEST);
		painelProcura.add(txtField,BorderLayout.CENTER);
		painelProcura.add(botaoProcurar,BorderLayout.EAST);
		painelProcura.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		
		
		//Criação dos elementos da GUI relacionados com a lista de ficheiros
		//--------------------------------------------------------------------------------------------
		// Substituir pelo processo de carregamento....
		//--------------------------------------------------------------------------------------------
		String[] searchResult = {"aaaa", "bbbbb","aaaa", "bbbbb","aaaa", "bbbbb"}; // Array de Strings
		//--------------------------------------------------------------------------------------------
		listaFiles = new JList<String>(searchResult);
		listaFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// 3 hipóteses: a)SINGLE_SELECTION; b) SINGLE_INTERVAL_SELECTION; c) MULTIPLE_INTERVAL_SELECTION
		listaFiles.setLayoutOrientation(JList.VERTICAL);
		listaFiles.setVisibleRowCount(-1); 	
		listaFiles.setFixedCellHeight(H/10);
//		...
//		JScrollPane listScroller = new JScrollPane(listaFiles);
//		listScroller.setPreferredSize(new Dimension(250, 80));
//		...
		JPanel painelFicheiros = new JPanel();
		painelFicheiros.setPreferredSize(new Dimension(W/2, H));
		painelFicheiros.setLayout(new BorderLayout());
		painelFicheiros.add(listaFiles,BorderLayout.NORTH);
		painelFicheiros.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		
        //Criação dos elementos da GUI relacionados com a barra de progresso..
        botaoDescarregar = new JButton("Descarregar");
        Font fontBotaoDescarregar = new Font("Lucida Sans Serif", Font.BOLD, 16);
        botaoDescarregar.setFont(fontBotaoDescarregar);
        botaoDescarregar.setActionCommand("Descarregar");
        botaoDescarregar.setPreferredSize(new Dimension (W/8*3, H/4));
        botaoDescarregar.addActionListener(this);
 
        barraDeProgresso = new JProgressBar(0, 100);
        Font fontbarraDeProgresso = new Font("Lucida Sans Serif", Font.BOLD, 18);
        barraDeProgresso.setFont(fontbarraDeProgresso);
        barraDeProgresso.setValue(0);
        barraDeProgresso.setPreferredSize(new Dimension (W/8*3, H/4));
        barraDeProgresso.setForeground(Color.LIGHT_GRAY);
        barraDeProgresso.setStringPainted(true);
//      -----------------------------------------------------  
//      -----------------------------------------------------         
//		  Recuperar as linhas seguintes para a mensagem final!   
//        Formato da msg final: Fornecedor{endereço=ip, porto=p]: blocos
//        Exemplo:
//        (ícone I) Descarga completa.
//		  Fornecedor[endereço=/127.0.0.1, porto=8082]:253 
//		  Fornecedor[endereço=/127.0.0.1, porto=8081]:251 
//        -----------------------------------------------------        
//        tarefaOutput = new JTextArea(5, 20);
//        tarefaOutput.setMargin(new Insets(5,5,5,5));
//        tarefaOutput.setEditable(false);
//      -----------------------------------------------------  
//      ----------------------------------------------------- 
 
        JPanel painelProgresso = new JPanel();
        painelProgresso.setLayout(new GridLayout(2, 1));
        painelProgresso.add(botaoDescarregar);
        painelProgresso.add(barraDeProgresso);
        //add(new JScrollPane(tarefaOutput)); //aqui no GridLayout
        //add(new JScrollPane(tarefaOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
        add(painelProcura, BorderLayout.NORTH);
        add(painelFicheiros, BorderLayout.WEST);
        add(painelProgresso, BorderLayout.EAST);
        
 
    }
 
//    public void valueChanged(ListSelectionEvent e) {
//        if (e.getValueIsAdjusting() == false) {
//
//            if (listaFiles.getSelectedIndex() == -1) {
//            //Nada selecionado, disable botão de fire
//                fireButton.setEnabled(false);
//
//            } else {
//            // Selecionado, enable do botao fire.
//                fireButton.setEnabled(true);
//            }
//        }
//    }
    
    //------------------------------------------------------------------------
    // Invocado quando o utilizador prime o botão "Descarregar" ou "Procurar".
    //------------------------------------------------------------------------
    public void actionPerformed(ActionEvent evt) {
    	if (evt.getActionCommand() == "Descarregar") {
	        botaoDescarregar.setEnabled(false);
	        botaoProcurar.setEnabled(false);
	        txtField.setEnabled(false);
	        listaFiles.setEnabled(false);
	        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	        //As instâncias do javax.swing.SwingWorker não são reutilizáveis.
	        //Por isso, cria-se uma nova, à medida do necessário.
	        tarefa = new Task();
	        tarefa.addPropertyChangeListener(this);  //sentinela
	        tarefa.execute();
    	} else {
    		// botão "Procurar"
    		
    	}
    }
    
    //------------------------------------------------------------------------
    // Invocado sempre que a propriedade de progresso da tarefa é alterada
    //------------------------------------------------------------------------
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            barraDeProgresso.setValue(progress);
        } 
    }
 
    //------------------------------------------------------------------------
    // Criação do GUI e colocação em funcionamento.
    // Corre no evento que despacha a thread.
    //------------------------------------------------------------------------
    private static void criaEmostraGUI() {
        JFrame frame = new JFrame("The ISCTE Bay");
        frame.setPreferredSize( new Dimension ( W ,  H ) ) ;  // Isto não está a funcionar!
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Cria e configura o painel de conteúdos.
        JComponent newContentPane = new TheISCTEBay();
        newContentPane.setOpaque(true); //Os painéis de conteúdos devem ser opacos !!!
        frame.setContentPane(newContentPane);
 
        //Mostra a janela.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
       	//Agenda um job para o evento de despachar a thread.
        //Cria e mostra o GUI desta aplicação.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                criaEmostraGUI();
            }
        });
    }
}
