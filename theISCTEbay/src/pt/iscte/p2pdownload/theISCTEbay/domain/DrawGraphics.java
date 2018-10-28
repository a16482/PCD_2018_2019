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

    //you can override the paint method of JFrame and then fill that by your favorite color like this:
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
    
//    public nameOfTheClass()  {
//
//    	final Container c = this.getContentPane();
//
//    	  public void actionPerformed(ActionEvent e) {
//    	    c.setBackground(Color.white); 
//    	  }
//    	}
}