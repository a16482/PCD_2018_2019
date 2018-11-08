package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class WinPopUpUtilizadores extends JPanel implements ActionListener, PropertyChangeListener  {
	
	private static final long serialVersionUID = -736174911678774149L;

	private static final int W = 500;
	private static final int H = 500;
	private static final String NEW_LINE = "\n";

//	private DefaultListModel<Utilizador> utilizadores = new DefaultListModel<Utilizador>();
	private DefaultListModel<String> utilizadores;
//	private JList<Utilizador> listaUtilizadores;
	private JList<String> listaUtilizadores;
	private JScrollPane scrollerListaUtilizadores;

	private Task tarefa;
	private JProgressBar barraDeProgresso = new JProgressBar(0, 100); 
	private JLabel lblTitulo = new JLabel();
	private JButton botaoRefrescar;
	private JPanel painelRefrescar;
	private JPanel painelUtilizadores;
	private JPanel painelBase;

	JFrame smallFrame;
	Container painelPequeno;

	class Task extends SwingWorker<Void, Void> {

		@Override
		public Void doInBackground() {
			loadListaUtilizadores();
			return null;
		}

		//---------------------------------------------------
		// Executado no evento de despacho da thread
		//---------------------------------------------------
		@Override
		public void done() { //Feito!!!
			//			mostraBarraDeProgresso(false);
			MsgBox.info("Lista de utilizadores carregada!");
//			Toolkit.getDefaultToolkit().beep();
			setCursor(null); //desliga o wait do cursor
		}
	}
	
	int getTotalUtilizadores() {
		int i =0;
		i = TheISCTEBay.devolveNumeroUtilizadores();
		return i;
	}
	
	public Void loadListaUtilizadores() {
		String u = "";
		utilizadores.removeAllElements();
		int totalElementos = getTotalUtilizadores();
		int i=0;
		try {
			for (i=0;i<totalElementos;i++) {
//				utilizadores.add(i, TheISCTEBay.devolveUtilizadorNDaLista(i));
				u= TheISCTEBay.devolveUtilizadorNDaLista(i).ipUtilizador() + ":" + 
						TheISCTEBay.devolveUtilizadorNDaLista(i).portoUtilizador(); 
				utilizadores.add(i, u);
//				MsgBox.info("i=" + String.valueOf(i) + " " + u);
//				MsgBox.info("i=" + String.valueOf(i) + 
//						TheISCTEBay.devolveUtilizadorNDaLista(i).ipUtilizador() + " " + 
//						TheISCTEBay.devolveUtilizadorNDaLista(i).portoUtilizador() );
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			MsgBox.erro("Erro na obtenção da lista de Utilizadores!" +  NEW_LINE + e.getMessage());
		} 
		//...
		scrollerListaUtilizadores.repaint();
		
//		String teste = "TESTE 1";
//		for (i=0;i<totalElementos;i++) {
//			teste = teste + "\n" + "i=" + String.valueOf(i) + " " + 
//					TheISCTEBay.devolveUtilizadorNDaLista(i).ipUtilizador() + ":" + 
//					TheISCTEBay.devolveUtilizadorNDaLista(i).portoUtilizador();
//		}
//		MsgBox.info(teste);
//		
//		String teste2 = "TESTE 2";
//		for (i=0;i<utilizadores.getSize();i++) {
//			teste2 = teste2 + "\n" + "i=" + String.valueOf(i) + " " + utilizadores.elementAt(i);
//		}
//		MsgBox.info(teste2);		 
//			
//		
		//...
		return null;
	}

	
	public void enviaEventoRefrescar() {
		this.botaoRefrescar.doClick();
	}
	
// exemplo 1	
//	scrollerListaUtilizadores = new JScrollPane();
//	//frame.add(new JScrollPane(listaUtilizadores));
//	scrollerListaUtilizadores.setViewportView(listaUtilizadores);
//	scrollerListaUtilizadores = new JScrollPane(listaUtilizadores);
//	listaUtilizadores.addListSelectionListener(new ListSelectionListener() {
//		private int previous = -1;
//  fim de exemplo	
	
	// Construtor da Lista de Utilizadores
//	public void ListaUtilizadores() {	
//		scrollerListaUtilizadores.setViewportView(listaUtilizadores);
//		listaUtilizadores.addListSelectionListener(new ListSelectionListener() {
//			private int previous = -1;
//			//@Override
//			public void valueChanged(ListSelectionEvent e) {
//				if (listaUtilizadores.getSelectedIndex() != -1 && previous != listaUtilizadores.getSelectedIndex()) {
//					System.out.println(listaUtilizadores.getSelectedValue().toString());
//				}
//				previous = listaUtilizadores.getSelectedIndex();
//			}
//		});	
//		
//		try {
//			enviaEventoRefrescar();
//		} catch (Exception e) {
//			e.printStackTrace();
//			MsgBox.erro("erro: " + e.getMessage());
//		}
//	}
	


	// Construtor da Janela
	public WinPopUpUtilizadores() {

		super(new BorderLayout());

		// elementos da IGUtilizadores
		if(getTotalUtilizadores()== 1) {
			lblTitulo.setText(getTotalUtilizadores() + " Utilizador Ligado");
		} else {
			lblTitulo.setText(getTotalUtilizadores() + " Utilizadores Ligados");
		}
		Font fontLblTexto = new Font("Lucida Sans Serif", Font.BOLD, 16);
		lblTitulo.setFont(fontLblTexto);
		lblTitulo.setForeground(Color.DARK_GRAY);
		lblTitulo.setVerticalAlignment(SwingConstants.CENTER);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		// botão Refrescar
		botaoRefrescar = new JButton("Refrescar");
		Font fontBotaoProcurar = new Font("Lucida Sans Serif", Font.BOLD, 16);
		botaoRefrescar.setFont(fontBotaoProcurar);
		botaoRefrescar.setActionCommand("Refrescar");
		botaoRefrescar.setPreferredSize(new Dimension(W / 8 * 3, H / 10));
		botaoRefrescar.addActionListener((ActionListener) this);

		// Painel base
		painelBase = new JPanel();
		painelBase.setBackground(Color.darkGray);
		painelBase.setPreferredSize(new Dimension(W, H));
		painelBase.setLayout(new BorderLayout());
		Border borderPainelBase = BorderFactory.createEmptyBorder(10, 10, 10, 10);

		// Painel só para o lado esquerdo de cima - texto sobrepõe barra de progresso
		smallFrame = new JFrame(); 
		painelPequeno = smallFrame.getContentPane();
		painelPequeno.add(barraDeProgresso);
		painelPequeno.add(lblTitulo);

		// Painel de cima
		painelRefrescar = new JPanel();
		painelRefrescar.setLayout(new GridLayout(1,3)); // 1 linha, 3 colunas.
		painelRefrescar.setOpaque(false);
		Border insideBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		painelRefrescar.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));
		painelRefrescar.add(painelPequeno, BorderLayout.WEST);
		painelRefrescar.add(botaoRefrescar, BorderLayout.EAST);

		painelUtilizadores = new JPanel();
		
//		listaUtilizadores = new JList<Utilizador>(utilizadores);
//		........................................................
//		EXEMPLO:
//		dlm = new DefaultListModel();
//        while (iter.hasNext()) {
//            dlm.addElement(iter.next().toString());
//        }
//        list = new JList(dlm);
//        scroll = new JScrollPane(list);
		
		utilizadores = new DefaultListModel<String>();
		enviaEventoRefrescar();
		listaUtilizadores = new JList<String>(utilizadores);
		scrollerListaUtilizadores = new JScrollPane(listaUtilizadores);
		listaUtilizadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaUtilizadores.setLayoutOrientation(JList.VERTICAL);
		listaUtilizadores.setVisibleRowCount(-1);
	
		
		scrollerListaUtilizadores.setPreferredSize(new Dimension (W/9*5, H));
//		scrollerListaUtilizadores.setPreferredSize(new Dimension (W, H));
		scrollerListaUtilizadores.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); //cima;esquerda;baixo;direita
		scrollerListaUtilizadores.setViewportView(listaUtilizadores);
		scrollerListaUtilizadores.repaint();
		
		painelUtilizadores.add(new JScrollPane(listaUtilizadores), BorderLayout.WEST);
		painelUtilizadores.add(scrollerListaUtilizadores, BorderLayout.WEST);
		painelUtilizadores.setPreferredSize(new Dimension(W , (H / 10 * 9) + 80));
		painelUtilizadores.setLayout(new BorderLayout());
		Border borderPainelUtilizadores = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		painelUtilizadores.setBorder(BorderFactory.createCompoundBorder(borderPainelUtilizadores , insideBorder ));

		// Criação dos elementos da GUI relacionados no contentor principal
		add(painelRefrescar, BorderLayout.NORTH);
		add(painelUtilizadores, BorderLayout.WEST);
		//---------------------------------------------------
		//Fim da definição da janela de Utilizadores
		//---------------------------------------------------
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

	// -----------------------------------------------------------
	// Invocado quando o utilizador prime o botão "Refrescar".
	// -----------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent evento) {
		switch(evento.getActionCommand()) {
			case ("Refrescar"): 
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				tarefa = new Task();
				tarefa.addPropertyChangeListener(this); 
				tarefa.execute();
				break;
			default:
				// nada a fazer
				break;
		}
	}
}
