package pt.iscte.p2pdownload.theISCTEbay.domain;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.Random;

public class InterfaceGrafica extends JPanel implements ActionListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	
	private JProgressBar barraDeProgresso;
    private JButton botaoDescarregar;
    private JTextArea outputTarefa;
    private Task tarefa;
    private JLabel lblTexto;
    private JTextField txtField;
 
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
        
 
        /*
         * Executado no evento de despachar a thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            botaoDescarregar.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            outputTarefa.append("Feito!\n");
        }
    }
 
    public InterfaceGrafica() {
       	//---------------------------------------------------
       	//Experiência:
       	//---------------------------------------------------
    	
        //Criação da Interface com o utilizador (GUI).
        super(new BorderLayout());
             
        lblTexto = new JLabel("Texto a procurar:");
		Font fontLblTexto = new Font("LucidaSans", Font.PLAIN, 14);
		lblTexto.setFont(fontLblTexto);
		lblTexto.setForeground(Color.red);
		lblTexto.setBackground(Color.gray);
		lblTexto.setVerticalAlignment(JTextField.CENTER);
		lblTexto.setHorizontalAlignment(JTextField.LEFT);
		
		txtField = new JTextField();
		txtField.setText("");
		txtField.setFont(fontLblTexto);
		txtField.setHorizontalAlignment(JTextField.LEFT);
		txtField.setSelectedTextColor(Color.BLUE);
		  
		JPanel painelProcura= new JPanel();
		painelProcura.setLayout(new GridLayout(1, 2));
		painelProcura.add(lblTexto);
		painelProcura.add(txtField);
  
        //Criação dos elementos da GUI relacionados com a barra de progresso..
        botaoDescarregar = new JButton("Descarregar");
        botaoDescarregar.setActionCommand("Descarregar");
        botaoDescarregar.addActionListener(this);
 
        barraDeProgresso = new JProgressBar(0, 100);
        barraDeProgresso.setValue(0);
        barraDeProgresso.setStringPainted(true);
 
        outputTarefa = new JTextArea(5, 20);
        outputTarefa.setMargin(new Insets(5,5,5,5));
        outputTarefa.setEditable(false);
       
 
        JPanel painelProgresso = new JPanel();
        painelProgresso.setLayout(new GridLayout(2, 2));
        painelProgresso.add(botaoDescarregar);
        painelProgresso.add(barraDeProgresso);
        add(new JScrollPane(outputTarefa), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
        add(painelProcura, BorderLayout.NORTH);
        add(painelProgresso, BorderLayout.SOUTH);
        
        //add(painel1, BorderLayout.PAGE_START);
//        add(new JScrollPane(outputTarefa), BorderLayout.CENTER);
//        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
    }
 
     //----------------------------------------------------------
     // Invocado quando o utilizador carrega no botão "Descarregar".
     //---------------------------------------------------------------------
    public void actionPerformed(ActionEvent evt) {
        botaoDescarregar.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //As instâncias do javax.swing.SwingWorker não são reutilizáveis.
        //Por isso, cria-se uma nova, à medida do necessário.
        tarefa = new Task();
        tarefa.addPropertyChangeListener(this);
        tarefa.execute();
    }
 
    /**
     * Invocado quando a propriedade de progresso da tarefa é alterada.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            barraDeProgresso.setValue(progress);
            // VER ISTO!
            outputTarefa.append(String.format("%d%% da tarefa completa.\n", tarefa.getProgress()));
        } 
    }
 
 
    /**
     * Ciação do GUI e colocação em funcionamento.
     * Corre no evento que despacha a thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("The ISCTE Bay");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Cria e configura o painael de conteúdos.
        JComponent newContentPane = new InterfaceGrafica();
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
                createAndShowGUI();
            }
        });
    }
}
