package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

public class IGMenu implements ActionListener  {

	public JMenuBar createMenuBar() {
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("A Menu");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		menuBar.add(menu);

		//a group of JMenuItems
		menuItem = new JMenuItem("A text-only menu item",KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		//a submenu
		menu.addSeparator();
		submenu = new JMenu("A submenu");
		submenu.setMnemonic(KeyEvent.VK_S);

		menuItem = new JMenuItem("An item in the submenu");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		menuItem.addActionListener(this);
		submenu.add(menuItem);

		menuItem = new JMenuItem("Another item");
		menuItem.addActionListener(this);
		submenu.add(menuItem);
		menu.add(submenu);

		//Build second menu in the menu bar.
		menu = new JMenu("Another Menu");
		menu.setMnemonic(KeyEvent.VK_N);
		menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");
		menuBar.add(menu);

		return menuBar;
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem)(e.getSource());
		String s;
		s = getClassName(source);
		IGMsgBox.info(s);
	}

	public void itemStateChanged(ItemEvent e) {
		JMenuItem source = (JMenuItem)(e.getSource());
		String s;
		s = getClassName(source);
		IGMsgBox.info(s);
	}

	//	public Container createContentPane() {
	//		//Create the content-pane-to-be.
	//		JPanel contentPane = new JPanel(new BorderLayout());
	//		contentPane.setOpaque(true);
	//
	//		//Create a scrolled text area.
	//		output = new JTextArea(5, 30);
	//		output.setEditable(false);
	//		scrollPane = new JScrollPane(output);
	//
	//		//Add the text area to the content pane.
	//		contentPane.add(scrollPane, BorderLayout.CENTER);
	//
	//		return contentPane;
	//	}
	//
	//	public void createPopupMenu() {
	//		JMenuItem menuItem;
	//
	//		//Create the popup menu.
	//		JPopupMenu popup = new JPopupMenu();
	//		menuItem = new JMenuItem("A popup menu item");
	//		menuItem.addActionListener(this);
	//		popup.add(menuItem);
	//		menuItem = new JMenuItem("Another popup menu item");
	//		menuItem.addActionListener(this);
	//		popup.add(menuItem);
	//
	//		//Add listener to the text area so the popup menu can come up.
	//		MouseListener popupListener = new PopupListener(popup);
	//		output.addMouseListener(popupListener);
	//	}


	// Returns just the class name -- no package info.
	protected String getClassName(Object o) {
		String classString = o.getClass().getName();
		int dotIndex = classString.lastIndexOf(".");
		return classString.substring(dotIndex+1);
	}


	//	private static void createAndShowGUI() {
	//		//Create and set up the window.
	//		JFrame frame = new JFrame("PopupMenuDemo");
	//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//
	//		//Create/set menu bar and content pane.
	//		PopupMenuDemo demo = new PopupMenuDemo();
	//		frame.setJMenuBar(demo.createMenuBar());
	//		frame.setContentPane(demo.createContentPane());
	//
	//		//Create and set up the popup menu.
	//		demo.createPopupMenu();
	//
	//		//Display the window.
	//		frame.setSize(450, 260);
	//		frame.setVisible(true);
	//	}
	//
	//	public static void main(String[] args) {
	//		//Schedule a job for the event-dispatching thread:
	//		//creating and showing this application's GUI.
	//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	//			public void run() {
	//				createAndShowGUI();
	//			}
	//		});
	//	}
	class PopupListener extends MouseAdapter {
		JPopupMenu popup;

		PopupListener(JPopupMenu popupMenu) {
			popup = popupMenu;
		}

		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(),e.getX(), e.getY());
			}
		}
	}

}
