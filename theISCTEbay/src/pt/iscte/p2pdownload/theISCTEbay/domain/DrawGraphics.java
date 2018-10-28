package pt.iscte.p2pdownload.theISCTEbay.domain;

import javax.swing.*;
import java.awt.*;


public class DrawGraphics extends JFrame{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DrawGraphics(String title) throws HeadlessException {
      super(title);
      InitialElements();
    }

    private void InitialElements(){
      setSize(300, 250);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setVisible(true);
    }

    public void paint(Graphics draw){
      //Here you can perform any drawing like an oval...
      draw.fillOval(40, 40, 60, 50);

      getContentPane().setBackground(new Color(70,80,70));
    }
}