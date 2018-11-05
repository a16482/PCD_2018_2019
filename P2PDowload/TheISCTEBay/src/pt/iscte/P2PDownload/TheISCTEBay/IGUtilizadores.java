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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
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

public class IGUtilizadores extends JPanel implements ActionListener, PropertyChangeListener   {

	private static final long serialVersionUID = 1L;

	private static final int W = 500;
	private static final int H = 500;

	private JList<String> listaUtilizadores = new JList<String>();
	private JScrollPane scrollerListaUtilizadores = new JScrollPane();
	private Task tarefa;
	private JProgressBar barraDeProgresso = new JProgressBar(0, 100); 
	private JLabel lblTitulo = new JLabel();
	private JFrame smallFrame = new JFrame(); 
	private int progresso; // recurso comum
	
	public synchronized int getValorDeProgresso(int nElementos, int totalElementos) {
		try {
			progresso = (nElementos / totalElementos * 100);
		} catch (Exception e) {
			e.printStackTrace();
			MsgBox.erro(e.getMessage());
		}
		return progresso;
	}
	
	public synchronized int getTotalUtilizadores() {
		int i =0;
		i = TheISCTEBay.devolveNumeroUtilizadores();
		return i;
	}

	public synchronized void mostraBarraDeProgresso(boolean b) {
		boolean bNegado =!b;   
		try {
			barraDeProgresso.setVisible(b);
		} catch (Exception e) {
			e.printStackTrace();
			MsgBox.erro(e.getMessage());
		}
		try {
			lblTitulo.setVisible(bNegado);
		} catch (Exception e) {
			e.printStackTrace();
			MsgBox.erro(e.getMessage());
		}
	}
	
	public synchronized Void loadListaUtilizadores() {
		int totalElementos;
		int elementoAtual=0;
		progresso =0;
		ArrayList<String> pesquisa = new ArrayList<String>();
		DefaultListModel<String> model = new DefaultListModel<String>();
		try {
			pesquisa =TheISCTEBay.devolveListaUtilizadoresArrayStr();
		} catch (Exception e) {
			e.printStackTrace(); 
			MsgBox.erro(e.getMessage());
		} 
		
		totalElementos = pesquisa.size();	
		try {
			for(String s:pesquisa){
				model.addElement(s);
				elementoAtual += 1;
				progresso = getValorDeProgresso(elementoAtual,totalElementos);
//				notifyAll();
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			//MsgBox.erro(e.getMessage());
		}
		listaUtilizadores.setModel(model);
		// JList<String> listaUtilizadores = new JList<String>(model);
		return null;
	}
	
	class Task extends SwingWorker<Void, Void> {
		
		@Override
		public Void doInBackground() {
			// Carrega utilizadores
			progresso = 0;
			setProgress(0);
			barraDeProgresso.setVisible(true);
//			mostraBarraDeProgresso(true);
			loadListaUtilizadores();
//			if (smallFrame.isVisible()) {
//				smallFrame.setVisible(false);
//			}
//			try {			
//				while (progresso < 100) {
////					wait();
//					setProgress(Math.min(progresso, 100));
//				}
//			} catch (Exception e) {
//				e.printStackTrace(); 
//			}
//			mostraBarraDeProgresso(false);
			return null;
		}
		
		//---------------------------------------------------
		// Executado no evento de despacho da thread
		//---------------------------------------------------
		@Override
		public void done() { //Feito!!!
//			mostraBarraDeProgresso(false);
			Toolkit.getDefaultToolkit().beep();
			setCursor(null); //desliga o wait do cursor
		}
	}
	
	// Construtor da IG
	public IGUtilizadores() {

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

		// barra de progresso
//		JProgressBar barraDeProgresso = new JProgressBar(0, 100);
		Font fontbarraDeProgresso = new Font("Lucida Sans Serif", Font.BOLD, 18);
		barraDeProgresso.setFont(fontbarraDeProgresso);
		barraDeProgresso.setValue(0);
		barraDeProgresso.setPreferredSize(new Dimension(W / 8 * 3, H / 10));
		barraDeProgresso.setForeground(Color.GREEN);
		barraDeProgresso.setStringPainted(true);

		// bot�o Refrescar
		JButton botaoRefrescar = new JButton("Refrescar");
		Font fontBotaoProcurar = new Font("Lucida Sans Serif", Font.BOLD, 16);
		botaoRefrescar.setFont(fontBotaoProcurar);
		botaoRefrescar.setActionCommand("Refrescar");
		botaoRefrescar.setPreferredSize(new Dimension(W / 8 * 3, H / 10));
		botaoRefrescar.addActionListener((ActionListener) this);
		
		// Painel base
		JPanel painelBase = new JPanel();
		painelBase.setBackground(Color.darkGray);
		painelBase.setPreferredSize(new Dimension(W, H));
		painelBase.setLayout(new BorderLayout());
		Border borderPainelBase = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		
		// Painel s� para o lado esquerdo de cima - texto sobrep�e barra de progresso
		JFrame smallFrame = new JFrame(); 
		Container painelPequeno = smallFrame.getContentPane();
		painelPequeno.add(barraDeProgresso);
		painelPequeno.add(lblTitulo);

		// Painel de cima
		JPanel painelRefrescar = new JPanel();
		painelRefrescar.setLayout(new GridLayout(1,3)); // 1 linha, 3 colunas.
		painelRefrescar.setOpaque(false);
		Border insideBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		painelRefrescar.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));
		painelRefrescar.add(painelPequeno, BorderLayout.WEST);
		painelRefrescar.add(botaoRefrescar, BorderLayout.EAST);

		// Cria��o dos elementos da lista de utilizadores
		
		// teste a partir daqui ----
		String[] searchResult = {"aaaa", "bbbbb", "ccccc", "d", "0001", "f", "g","h", "i", "j", "k", "l" +
				"aaaa", "bbbbb", "ccccc", "d", "0001", "f", "g","h", "i", "j", "k", "l"};
		scrollerListaUtilizadores = new JScrollPane();
		listaUtilizadores = new JList<String>(searchResult);
		
		// teste at� aqui ----
		listaUtilizadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaUtilizadores.setLayoutOrientation(JList.VERTICAL);
		listaUtilizadores.setSize(new Dimension ((W), (H / 10 * 9)));
		listaUtilizadores.setVisibleRowCount(-1);
		//listaUtilizadores.setFixedCellHeight(H / 10);
		listaUtilizadores.setFixedCellWidth((W / 10 * 9)+ 20);
		listaUtilizadores.setFont(new Font("Lucida Sans Serif", Font.PLAIN, 16));
		scrollerListaUtilizadores.setViewportView(listaUtilizadores);
		scrollerListaUtilizadores.setPreferredSize(new Dimension ((W), (H / 10 * 9)-+ 10));
		scrollerListaUtilizadores.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //cima;esquerda;baixo;direita

		scrollerListaUtilizadores = new JScrollPane(listaUtilizadores);
		
		JPanel painelUtilizadores = new JPanel();
		painelUtilizadores.setPreferredSize(new Dimension(W , (H / 10 * 9)+20));
		painelUtilizadores.setLayout(new BorderLayout());
		Border borderPainelUtilizadores = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		painelUtilizadores.add(scrollerListaUtilizadores, BorderLayout.WEST);
		painelUtilizadores.setBorder(BorderFactory.createCompoundBorder(borderPainelUtilizadores , insideBorder ));
		
		// Cria��o dos elementos da GUI relacionados Utilizadores
		add(painelRefrescar, BorderLayout.NORTH);
		add(painelUtilizadores, BorderLayout.WEST);
		//---------------------------------------------------
		//Fim da defini��o da janela IGUtilizadores
		//---------------------------------------------------
	}
	
	// -----------------------------------------------------------
	// Invocado quando o utilizador prime o bot�o "Refrescar".
	// -----------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent evt) {
		switch(evt.getActionCommand()) {
		case ("Refrescar"): 
			MsgBox.info("Refrescar");
			
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			// As inst�ncias do javax.swing.SwingWorker n�o s�o reutiliz�veis.
			// Por isso, cria-se uma nova, � medida do necess�rio.
			tarefa = new Task();
			tarefa.execute();
			break;
		default:
			// nada para fazer
			break;
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


}
