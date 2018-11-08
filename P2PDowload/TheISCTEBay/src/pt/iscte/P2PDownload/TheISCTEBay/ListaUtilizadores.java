package pt.iscte.P2PDownload.TheISCTEBay;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListaUtilizadores  {

	/**
	 * 
	 */
	private JFrame frame = new JFrame();
	private DefaultListModel<Utilizador> utilizadores = new DefaultListModel<Utilizador>();
	private JList<Utilizador> listaUtilizadores = new JList<Utilizador>(utilizadores);
	private JScrollPane scrollerListaUtilizadores = new JScrollPane();
	
	public ListaUtilizadores() {
		scrollerListaUtilizadores = new JScrollPane();
		frame.add(new JScrollPane(listaUtilizadores));
		scrollerListaUtilizadores.setViewportView(listaUtilizadores);
//	I	scrollerListaUtilizadores = new JScrollPane(listaUtilizadores);
		listaUtilizadores.addListSelectionListener(new ListSelectionListener() {
			private int previous = -1;
	
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (listaUtilizadores.getSelectedIndex() != -1 && previous != listaUtilizadores.getSelectedIndex()) {
					System.out.println(listaUtilizadores.getSelectedValue().toString());
				}
				previous = listaUtilizadores.getSelectedIndex();
			}
		});	
}

}
