package pt.iscte.p2pdownload.theISCTEbay.domain;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Container.*;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.color.*;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



public class Ligacao {
	
	  
	  public static void main(String[] args)
	  {
		  JFrame myFrame = new JFrame("The ISCTE Bay");
		  Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		  myFrame.setPreferredSize(dimension);
		  // ou em alternativa...
		  //Container c = myFrame.getContentPane();
		  //c.setPreferredSize(new Dimension(400, 300));
		  
		  JPanel painelDeProcura= new JPanel();
		  painelDeProcura.setLayout(new BorderLayout());
		  
		  JPanel painelDeResultados= new JPanel();
		  painelDeResultados.setLayout(new BorderLayout());
		  
		  JPanel painelDeControlo= new JPanel();
		  painelDeControlo.setLayout(new GridLayout(2, 1));
		  
		  //---------------------------------------------------------
		  // PAINEL DE PROCURA: Label "Texto a Procurar"
		  //---------------------------------------------------------
		  JLabel lblTexto = new JLabel("Texto a procurar:");
		  Font fontLblTexto = new Font("LucidaSans", Font.PLAIN, 14);
		  lblTexto.setFont(fontLblTexto);
		  lblTexto.setForeground(Color.red);
		  lblTexto.setBackground(Color.gray);
		  lblTexto.setVerticalAlignment(JTextField.CENTER);
		  lblTexto.setHorizontalAlignment(JTextField.LEFT);
		  painelDeProcura.add(lblTexto, BorderLayout.EAST);
		  //---------------------------------------------------------
		  // PAINEL DE PROCURA: Caixa de texto "Texto a Procurar"
		  //---------------------------------------------------------
		  JTextField txtField = new JTextField();
		  txtField.setText("");
		  txtField.setFont(fontLblTexto);
		  txtField.setHorizontalAlignment(JTextField.LEFT);
		  txtField.setSelectedTextColor(Color.BLUE);
		  painelDeProcura.add(txtField, BorderLayout.NORTH);
		  //---------------------------------------------------------
		  // PAINEL DE PROCURA: Botão com o texto "Procurar"
		  //---------------------------------------------------------	  
		  JButton botaoProcurar = new JButton("Procurar");
		  painelDeProcura.add(botaoProcurar, BorderLayout.WEST);
		  
		  //---------------------------------------------------------
		  // PAINEL DE RESULTADOS: Botão com o texto "Procurar"
		  //---------------------------------------------------------	
		  JList<String> listaResultados = new JList<String>();
		  // o carregamento depende do servidor!
		  painelDeResultados.add(listaResultados, BorderLayout.EAST);
		  
		  //---------------------------------------------------------
		  // PAINEL DE CONTOLO: Botão com o texto "Descarregar"
		  //---------------------------------------------------------	
		  JButton botaoDescarregar = new JButton("Descarregar");
		  painelDeControlo.add(botaoDescarregar);
		  
		  //---------------------------------------------------------
		  // PAINEL DE CONTOLO: Botão com o texto "Descarregar"
		  //---------------------------------------------------------	
		  JProgressBar progressBar = new JProgressBar(0, task.getLengthOfTask());
		  progressBar.setValue(0);
		  progressBar.setStringPainted(true);
		  //---------------------------------------------------------
		  // colocar este código no local próprio para controlar a ProgressBar:
		  //
		  //---------------------------------------------------------
		  
		  
//		  myFrame.setSize(300, 200);
//		  myFrame.setLocation(200, 100);
//		  myFrame.setResizable(false);
		  
		  myFrame.setVisible(true);
		  
		  
		// painel ds ações
//			JPanel painelDasAcções= new JPanel();
//			painelDasAcções.setLayout(new GridLayout(2, 1));
//			painelDasAcções.add(new JButton("C"));
//			painelDasAcções.add(new JButton("="));
//			painel.add(painelDasAcções);
//			frame.add(painel, BorderLayout.CENTER);
			
	  }
		  //myFrame.setSize(800, 600);
		  
		//JFrame frame = new JFrame("The ISCTE Bay");
//		frame.setPreferredSize(new Dimension(800, 600));
//		//frame.setTitle("The ISCTE Bay");
//		// this maximizes the jframe:
//		frame.setSize(800, 600);
//		frame.setBackground(bgColor);
		//JFrame frame = new JFrame("The ISCTE Bay"); 
		
//		JFrame frame = new JFrame("The ISCTE Bay");
//	    Container c = frame.getContentPane();
//	    c.setPreferredSize(new Dimension(400, 300));
//	    c.setBounds(20, 20, 200, 200);
	    // this maximizes the jframe:
	    // c.setSize(800, 600);
	    //c.setBounds(x, y, width, height);
		  
//	    c.setForeground(Color.black);
//	    c.setBackground(Color.red);
//	    
//		

//		frame.add(painelDasLigacoes);
		
		//painelDasLigacoes.setLayout(new GridLayout(4, 2));
    	

	    // schedule this for the event dispatch thread (edt)
//	    SwingUtilities.invokeLater(new Runnable()
//	    {
//	      public void run()
//	      {
//
//	    	
//	        
//	    	frame.displayJFrame();
//	    	myFrame.setVisible(true);
//	      }
//	    });
//	  }

//	private static void add(JPanel painelDasLigacoes) {
//		// TODO Auto-generated method stub
//		
//	}

//	  static void displayJFrame()
//	  {
//	    // create our jframe as usual
//	    JFrame jframe = new JFrame("JFrame Size Example");
//	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//	    // set the jframe size and location, and make it visible
//	    frame.setPreferredSize(new Dimension(400, 300));
//	    frame.pack();
//	    frame.setLocationRelativeTo(null);
//	    frame.setVisible(true);
//	  }
//}

//		public static void main(String[] args) {
//			static integer prefSize;
//		
//			
//			JFrame frame= new JFrame("The ISCTE Bay");
//			frame.setPreferredSize(new Dimension(400, 300));
//			
//		
//
//			JPanel painel = new JPanel();
//
//			// painel ds ligações
//			JPanel painelDasLigacoes= new JPanel();
//			painelDasLigacoes.setLayout(new GridLayout(2, 1));
//			painelDasLigacoes.add(new JButton("C"));
//			painelDasLigacoes.add(new JButton("="));
//			painel.add(painelDasLigacoes);
//			frame.add(painel, BorderLayout.CENTER);
//			
//			// mostrador
//			JTextField mostrador = new JTextField();
//			frame.add(mostrador, BorderLayout.NORTH);
//			
//			// Make the background white
//			mostrador.setBackground(Color.WHITE);
//			// Use a specific font“Arial, regular, tamanho 14”
//			mostrador.setFont(new Font("Arial", Font.PLAIN, 14));
//			// Right-align text
//			mostrador.setHorizontalAlignment(JTextField.RIGHT);
//			// Disable editing
//			mostrador.setEditable(false);
//			
//			frame.setSize(300, 200);
//			frame.setLocation(200, 100);
//			frame.setResizable(false);
//			
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.setVisible(true);
//		}
}
