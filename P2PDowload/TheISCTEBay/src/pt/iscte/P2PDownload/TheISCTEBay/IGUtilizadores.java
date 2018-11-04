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
	
	public synchronized int getValorDeProgresso(int nElementos, int totalElementos) {
		int progresso = 0;
		try {
			progresso = (nElementos / totalElementos * 100);
		} catch (Exception e) {
			e.printStackTrace();
			MsgBox.erro(e.getMessage());
		}
		return progresso;
	}
	
	public synchronized int loadListaUtilizadores() {
		int totalElementos;
		int elementoAtual=0;
		int progressoAtual =0;
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
				progressoAtual = getValorDeProgresso(elementoAtual,totalElementos);
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			MsgBox.erro(e.getMessage());
		}
//		JList<String> listaUtilizadores = new JList<String>(model);
		return progressoAtual;
	}
	
	public void mostraBarraDeProgresso(boolean b) {
		boolean bNegado =!b;   
		barraDeProgresso.setVisible(b);
		lblTitulo.setVisible(bNegado);
	}
	
	class Task extends SwingWorker<Void, Void> {

		@Override
		public Void doInBackground() {
			// Carrega utilizadores
			mostraBarraDeProgresso(true);
			
			try {
				int progresso = loadListaUtilizadores();
				this.wait();
				setProgress(Math.min(progresso, 100));
				try {
					Thread.sleep(3000);
				} catch (InterruptedException ignore) {}
			} catch (InterruptedException ignore) {}

			mostraBarraDeProgresso(false);

			return null;
		}
		
		//---------------------------------------------------
		// Executado no evento de despacho da thread
		//---------------------------------------------------
		@Override
		public void done() { //Feito!!!
			mostraBarraDeProgresso(false);
			Toolkit.getDefaultToolkit().beep();
			setCursor(null); //desliga o wait do cursor
		}
	}
	public int getTotalUtilizadores() {
		int i =0;
		i = TheISCTEBay.devolveNumeroUtilizadores();
		return i;
	}
	
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

		// botão Refrescar
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
		Border borderPainelBase = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		
		// Painel só para o lado esquerdo de cima - texto sobrepõe barra de progresso
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

		// Criação dos elementos da lista de utilizadores

		listaUtilizadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaUtilizadores.setLayoutOrientation(JList.VERTICAL);
		listaUtilizadores.setVisibleRowCount(-1);
		//listaUtilizadores.setFixedCellHeight(H / 10);
		listaUtilizadores.setFont(new Font("Lucida Sans Serif", Font.PLAIN, 16));
		scrollerListaUtilizadores.setViewportView(listaUtilizadores);
		scrollerListaUtilizadores.setPreferredSize(new Dimension ((W), (H / 10 * 9)));
		scrollerListaUtilizadores.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); //cima;esquerda;baixo;direita

		scrollerListaUtilizadores = new JScrollPane(listaUtilizadores);
		
		JPanel painelUtilizadores = new JPanel();
		painelUtilizadores.setPreferredSize(new Dimension(W , H / 10 * 9));
		painelUtilizadores.setLayout(new BorderLayout());
		painelUtilizadores.add(scrollerListaUtilizadores, BorderLayout.NORTH);
		painelUtilizadores.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		painelUtilizadores.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));
		
		// Criação dos elementos da GUI relacionados Utilizadores
		add(painelRefrescar, BorderLayout.NORTH);
		add(painelUtilizadores, BorderLayout.WEST);
		//---------------------------------------------------
		//Fim da definição da janela IGUtilizadores
		//---------------------------------------------------
	}
	
	// -----------------------------------------------------------
	// Invocado quando o utilizador prime o botão "Refrescar".
	// -----------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent evt) {
		switch(evt.getActionCommand()) {
		case ("Refrescar"): 
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// As instâncias do javax.swing.SwingWorker não são reutilizáveis.
		// Por isso, cria-se uma nova, à medida do necessário.
		tarefa = new Task();
		tarefa.execute();
		break;
		default:
			// nada a fazer
			break;
		}
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


}
