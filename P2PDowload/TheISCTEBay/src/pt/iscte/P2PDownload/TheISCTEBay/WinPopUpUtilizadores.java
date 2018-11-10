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

public class WinPopUpUtilizadores extends JPanel implements ActionListener, PropertyChangeListener  {
	
	private static final long serialVersionUID = -736174911678774149L;

	private static final int W = 400;
	private static final int H = 400;
	private static final String NEW_LINE = "\n";

	private DefaultListModel<String> utilizadores;
	private JList<String> listaUtilizadores;
	private JScrollPane scrollerListaUtilizadores;

	private Task tarefa;
	private JProgressBar barraDeProgresso = new JProgressBar(0, 100); 
	private JLabel lblTitulo = new JLabel();
	private JButton botaoRefrescar;
	private JPanel painelRefrescar;
	private JPanel painelUtilizadores;
	private JPanel painelBase;

	JFrame upperLeftFrame;
	Container painelPequeno;
	Diretorio d = TheISCTEBay.devolveDiretorio();
	
	class Task extends SwingWorker<Void, Void> {
		@Override
		public Void doInBackground() { 
			ReloadListaUtilizadores reloadLista = new ReloadListaUtilizadores();
			reloadLista.start();
			return null;
		}

		//---------------------------------------------------
		// Executado no evento de despacho da thread
		//---------------------------------------------------
		@Override
		public void done() { 		
//			Toolkit.getDefaultToolkit().beep();
			setCursor(null); //desliga o wait do cursor
		}
	}
	
	public class FlushListaUtilizadores extends Thread implements Runnable {
		public void run() {
			synchronized(this) {
				try {
					flushListaUtilizadores();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					notify();
				}
			}
		}
		private synchronized void flushListaUtilizadores() throws InterruptedException {
			utilizadores.removeAllElements();
		}
		
		public void init() {
			new Thread(this, "FlushListaUtilizadores").start();
		}
	}
	
	public class ReloadListaUtilizadores extends Thread implements Runnable {
		public void run() {
			synchronized(this) {
				FlushListaUtilizadores listaLimpa = new FlushListaUtilizadores();
				listaLimpa.start();			
				try {
					listaLimpa.join(); //(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
				LoadListaUtilizadores novaLista = new LoadListaUtilizadores();
				novaLista.start();
				try {
					listaLimpa.join();
					novaLista.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
				while (this.isAlive()) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			repintaScroller();
			
		}
		
		public synchronized void repintaScroller() {
			scrollerListaUtilizadores.revalidate();
			scrollerListaUtilizadores.repaint();
			MsgBox.info("Lista de utilizadores recarregada!");
		}
		
		public void init() {
			new Thread(this, "ReloadListaUtilizadores").start();
		}
		
	}

	public class LoadListaUtilizadores extends Thread implements Runnable{
		
			@Override
			public void run() {
				d.consultaUtilizadores();
				synchronized(this) {
					try {
						loadListaUtilizadores(d);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						notify();
					}
				}
			}
			
			private synchronized int loadListaUtilizadores(Diretorio d)throws InterruptedException {
				String u = "";
				int totalElementos = d.getTotalUtilizadores();
				int i=0;
				try {
					for (i=0;i<totalElementos;i++) {
						u= d.getUtilizadorNDaLista(i).ipUtilizador() + ":" + 
								d.getUtilizadorNDaLista(i).portoUtilizador(); 
						utilizadores.add(i, u);
					}
				} catch (Exception e) {
					e.printStackTrace(); 
					MsgBox.erro("Erro na obtenção da lista de Utilizadores!" +  NEW_LINE + e.getMessage());
				} finally {
					notifyAll();
				}
				return totalElementos;
			}
			
			public int getTotalUtilizadores() {
				return d.getTotalUtilizadores();
			}
			
			public void init() {
				new Thread(this, "LoadListaUtilizadores").start();
			}
	}

	// ------------------------------------------------------------------------
	// Construtor da Janela
	// ------------------------------------------------------------------------
	public WinPopUpUtilizadores() {
		super(new BorderLayout());
		
		//Fonts
		Font fontTitulos = new Font("Lucida Sans Serif", Font.BOLD, 12);
		
		// elementos da IG que mostra os Utilizadores
		int totalUtilizadores = d.getTotalUtilizadores();
		if(totalUtilizadores== 1) {
			lblTitulo.setText(totalUtilizadores + " Utilizador Ligado");
		} else {
			lblTitulo.setText(totalUtilizadores + " Utilizadores Ligados");
		}
		lblTitulo.setFont(fontTitulos);
		lblTitulo.setForeground(Color.DARK_GRAY);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		// botão Refrescar
		botaoRefrescar = new JButton("Refrescar");
		botaoRefrescar.setFont(fontTitulos);
		botaoRefrescar.setActionCommand("Refrescar");
		botaoRefrescar.setPreferredSize(new Dimension(W / 8 * 3, H / 10));
		botaoRefrescar.addActionListener((ActionListener) this);

		// Painel base
		painelBase = new JPanel();
		painelBase.setBackground(Color.darkGray);
		painelBase.setPreferredSize(new Dimension(W, H));
		painelBase.setLayout(new BorderLayout());
		Border borderPainelBase = BorderFactory.createEmptyBorder(5, 5, 5, 5);

		// Painel só para o lado esquerdo de cima - texto sobrepõe barra de progresso
		upperLeftFrame = new JFrame(); 
		painelPequeno = upperLeftFrame.getContentPane();
		painelPequeno.add(barraDeProgresso);
		painelPequeno.add(lblTitulo);

		// Composição do painel de cima
	
		painelRefrescar = new JPanel();
		painelRefrescar.setLayout(new GridLayout(1,2)); // 1 linha, 2 colunas.
		painelRefrescar.setOpaque(false);
		Border insideBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		painelRefrescar.setBorder(BorderFactory.createCompoundBorder(borderPainelBase , insideBorder ));
		painelRefrescar.add(painelPequeno, BorderLayout.WEST);
		painelRefrescar.add(botaoRefrescar, BorderLayout.EAST);
		
		// Painel de baixo
		utilizadores = new DefaultListModel<String>();
	
		LoadListaUtilizadores loadLista = new LoadListaUtilizadores();
		loadLista.start();
		

		
		listaUtilizadores = new JList<String>(utilizadores);
		listaUtilizadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaUtilizadores.setLayoutOrientation(JList.VERTICAL);
		listaUtilizadores.setVisibleRowCount(-1);
		scrollerListaUtilizadores=new JScrollPane(listaUtilizadores);

		painelUtilizadores = new JPanel();
		painelUtilizadores.setLayout(new GridLayout(1,0)); 
		painelUtilizadores.setOpaque(false);
		painelUtilizadores.setPreferredSize(new Dimension(W , (H / 10 * 9) + 80));
		Border borderPainelUtilizadores = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		painelUtilizadores.setBorder(BorderFactory.createCompoundBorder(borderPainelUtilizadores , insideBorder ));
		painelUtilizadores.add(scrollerListaUtilizadores, BorderLayout.WEST);

		// Criação dos elementos da GUI relacionados no contentor principal
		add(painelRefrescar, BorderLayout.NORTH);
		add(painelUtilizadores, BorderLayout.WEST);
	}

	// ------------------------------------------------------------------------
	// Invocado sempre que a propriedade de progresso da tarefa é alterada
	// ------------------------------------------------------------------------
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			@SuppressWarnings("unused")
			int progress = (Integer) evt.getNewValue();
			//barraDeProgresso.setValue(progress);
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
