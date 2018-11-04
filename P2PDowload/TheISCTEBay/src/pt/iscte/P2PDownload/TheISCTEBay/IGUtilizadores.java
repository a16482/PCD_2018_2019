package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.BorderLayout;
import java.awt.Color;
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

	private static final int W = 400;
	private static final int H = 400;

//	private JList listaUtilizadores = new JList();
	JList<String> listaUtilizadores = new JList<String>();
	private JScrollPane scrollListaUtilizadores = new JScrollPane();

	private Task tarefa;
//	private int asynchCounter = 0;
	private JProgressBar barraDeProgresso = new JProgressBar(0, 100); 
	
//	private void updateCounter(int delta){
//	    asynchCounter+=delta;
//	    if(asynchCounter<=0){
//	    	barraDeProgresso.setVisible(false);
//	    }else{
//	    	barraDeProgresso.setVisible(true);
//	    }
//	}
	
//	class CountedBitmapWorkerTask extends BitmapWorkerTask {
//		protected void onPreExecute() {
//			super.onPreExecute();
//			updateCounter(1);
//		}
//		protected void onPostExecute(String msg) {
//			super.onPostExecute();
//			updateCounter(-1);
//		}
//	}
//	
	
	public synchronized int setValorBarraDeProgresso(int nElementos, int totalElementos) {
		int progresso;
		progresso = (nElementos / totalElementos * 100);
//		setProgress(Math.min(progresso,100));


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
		for(String s:pesquisa){
			model.addElement(s);
			elementoAtual += 1;
			progressoAtual = setValorBarraDeProgresso(elementoAtual,totalElementos);
		}
		return progressoAtual;
	}
	
	class Task extends SwingWorker<Void, Void> {

		@Override
		public Void doInBackground() {
			// Carrega utilizadores
			barraDeProgresso.setVisible(true);
			int progresso = loadListaUtilizadores();
			setProgress(Math.min(progresso, 100));
			try {
				Thread.sleep(100);
			} catch (InterruptedException ignore) {}
			barraDeProgresso.setVisible(false);
//			JList<String> listaUtilizadores = new JList<String>(model);
//			scrollerListaUtilizadores = new JScrollPane(listaUtilizadores);
			return null;
		}
		
		//---------------------------------------------------
		// Executado no evento de despacho da thread
		//---------------------------------------------------
		@Override
		public void done() { //Feito!!!
			barraDeProgresso.setVisible(false);
			Toolkit.getDefaultToolkit().beep();
			setCursor(null); //desliga o wait do cursor
		}
	}

	public IGUtilizadores() {

		super(new BorderLayout());

		// elementos da IGUtilizadore
		int i = 0;
		i = TheISCTEBay.devolveNumeroUtilizadores();


		JLabel lblTitulo = new JLabel(i + " Utiizadores Ligados");
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

//---- barra de progresso

		JButton botaoRefrescar = new JButton("Refrescar");
		Font fontBotaoProcurar = new Font("Lucida Sans Serif", Font.BOLD, 16);
		botaoRefrescar.setFont(fontBotaoProcurar);
		botaoRefrescar.setActionCommand("Refrescar");
		botaoRefrescar.setPreferredSize(new Dimension(W / 8 * 3, H / 10));
		botaoRefrescar.addActionListener((ActionListener) this);

		JPanel painelBase = new JPanel();
		painelBase.setBackground(Color.darkGray);
		painelBase.setPreferredSize(new Dimension(W, H));
		painelBase.setLayout(new BorderLayout());
		Border borderPainelBase = BorderFactory.createEmptyBorder(5, 5, 5, 5);

		JPanel painelRefrescar = new JPanel();
		painelRefrescar.setLayout(new GridLayout(1,3)); // 1 linha, 3 colunas.
		painelRefrescar.setOpaque(false);

		painelRefrescar.add(lblTitulo, BorderLayout.WEST);
		//painelRefrescar.add(barraDeProgresso, BorderLayout.WEST);
		painelRefrescar.add(botaoRefrescar, BorderLayout.EAST);

		Border insideBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		painelRefrescar.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));

		// ---------------------------------------------------------------------------------
		// Criação dos elementos da IGUtilizadores relacionados com a lista de utilizadores
		// ---------------------------------------------------------------------------------
		// ATENÇÃO: Substituir o array de string no processo de carregamento....
		//----------------------------------------------------------------------------------
//		ArrayList<String> searchResult = TheISCTEBay.devolveListaUtilizadoresArrayStr();
//		JScrollPane lUtilizadoresScroller = new JScrollPane();
//		ListModel<String> searchResult2 = (ListModel<String>) searchResult;
//		JList<String> listaUtilizadores = new JList<String>(searchResult2);

		listaUtilizadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaUtilizadores.setLayoutOrientation(JList.VERTICAL);
		listaUtilizadores.setVisibleRowCount(-1);
		listaUtilizadores.setFixedCellHeight(H / 10);
		listaUtilizadores.setFont(new Font("Lucida Sans Serif", Font.PLAIN, 16));
		scrollListaUtilizadores.setViewportView(listaUtilizadores);
		scrollListaUtilizadores.setPreferredSize(new Dimension (W/8*3, H/3*2-10));

		scrollListaUtilizadores.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //cima;esquerda;baixo;direita

		JPanel painelUtilizadores = new JPanel();
		painelUtilizadores.setPreferredSize(new Dimension(W / 2+60, H));
		painelUtilizadores.setLayout(new BorderLayout());
		painelUtilizadores.add(scrollListaUtilizadores, BorderLayout.NORTH);
		painelUtilizadores.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		//		
		//		  JPanel outerPanel = new JPanel();
		//		  outerPanel.setBackground(Color.black);
		//		  outerPanel.setPreferredSize(new Dimension(400, 400));
		//		  outerPanel.setLayout(new BorderLayout());
		//		  Border outsideBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		//		  Border insideBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		//		  Border innerPanelBorder = BorderFactory.createCompoundBorder(outsideBorder , insideBorder );
		//		  JPanel innerPanel = new JPanel();
		//		  innerPanel.setBorder(innerPanelBorder);
		//		  innerPanel.setOpaque(false);
		//		  outerPanel.add(innerPanel);

		// -----------------------------------------------------------
		// Criação dos elementos da GUI relacionados Utilizadores
		// -----------------------------------------------------------

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
