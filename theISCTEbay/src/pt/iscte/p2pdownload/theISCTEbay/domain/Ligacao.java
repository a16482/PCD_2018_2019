package pt.iscte.p2pdownload.theISCTEbay.domain;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Container.*;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.color.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



public class Ligacao {
	
	  
	  public static void main(String[] args)
	  {
		//JFrame frame = new JFrame("The ISCTE Bay");
//		frame.setPreferredSize(new Dimension(800, 600));
//		//frame.setTitle("The ISCTE Bay");
//		// this maximizes the jframe:
//		frame.setSize(800, 600);
//		frame.setBackground(bgColor);
		//JFrame frame = new JFrame("The ISCTE Bay"); 
		
		JFrame frame = new JFrame("The ISCTE Bay");
	    Container c = frame.getContentPane();
	    c.setPreferredSize(new Dimension(800, 600));
	    c.setBounds(20, 20, 200, 200);
	    //c.setBounds(x, y, width, height);
	    c.setForeground(Color.black);
	    c.setBackground(Color.red);
	    
		JPanel painelDasLigacoes= new JPanel();
		painelDasLigacoes.setLayout(new BorderLayout());
		painelDasLigacoes.add(new Button("Okay"), BorderLayout.SOUTH);

		frame.add(painelDasLigacoes);
		
    	//painelDasLigacoes.setLayout(new GridLayout(4, 2));
    	
    	
	    // schedule this for the event dispatch thread (edt)
	    SwingUtilities.invokeLater(new Runnable()
	    {
	      public void run()
	      {

	    	
	        
	    	//frame.displayJFrame();
	    	frame.setVisible(true);
	      }
	    });
	  }

	private static void add(JPanel painelDasLigacoes) {
		// TODO Auto-generated method stub
		
	}
}

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
