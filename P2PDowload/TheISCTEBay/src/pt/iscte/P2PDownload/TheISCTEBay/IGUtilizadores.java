package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class IGUtilizadores extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static int W = 800;
	private static int H = 400;

	public IGUtilizadores() {
		
		super(new BorderLayout());
		//---------------------------------------------------
		//Criação da Interface IGUtilizadores
		//---------------------------------------------------    
		JLabel lblTitulo = new JLabel("Utiizadores Ligados");
		Font fontLblTexto = new Font("Lucida Sans Serif", Font.BOLD, 16);
		lblTitulo.setFont(fontLblTexto);
		lblTitulo.setForeground(Color.DARK_GRAY);
		lblTitulo.setVerticalAlignment(SwingConstants.CENTER);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton botaoRefrescar = new JButton("Refrescar");
		Font fontBotaoProcurar = new Font("Lucida Sans Serif", Font.BOLD, 16);
		botaoRefrescar.setFont(fontBotaoProcurar);
		botaoRefrescar.setActionCommand("Refrescar");
		botaoRefrescar.setPreferredSize(new Dimension(W / 8 * 3, H / 10));
		botaoRefrescar.addActionListener((ActionListener) this);
		
		JPanel painelRefrescar = new JPanel();
		painelRefrescar.setLayout(new BorderLayout());
		painelRefrescar.add(lblTitulo, BorderLayout.WEST);
		painelRefrescar.add(botaoRefrescar, BorderLayout.EAST);
		painelRefrescar.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		// ---------------------------------------------------------------------------------
		// Criação dos elementos da IGUtilizadores relacionados com a lista de utilizadores
		// ---------------------------------------------------------------------------------
		// ATENÇÃO: Substituir o array de string no processo de carregamento....
		//----------------------------------------------------------------------------------
		String[] searchResult = {};

//		String[] searchResult = getListaUtilizString();
		JScrollPane lUtilizadoresScroller = new JScrollPane();
		JList<String> listaUtilizadores = new JList<String>(searchResult);
		
		listaUtilizadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaUtilizadores.setLayoutOrientation(JList.VERTICAL);
		listaUtilizadores.setVisibleRowCount(-1);
		listaUtilizadores.setFixedCellHeight(H / 10);
		listaUtilizadores.setFont(new Font("Lucida Sans Serif", Font.PLAIN, 16));
		lUtilizadoresScroller.setViewportView(listaUtilizadores);
		lUtilizadoresScroller.setPreferredSize(new Dimension (W/8*3, H/3*2-10));
		lUtilizadoresScroller.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); //cima;esquerda;baixo;direita

		JPanel painelUtilizadores = new JPanel();
		painelUtilizadores.setPreferredSize(new Dimension(W / 2+60, H));
		painelUtilizadores.setLayout(new BorderLayout());
		painelUtilizadores.add(lUtilizadoresScroller, BorderLayout.NORTH);
		painelUtilizadores.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		// -----------------------------------------------------------------------
		// Criação dos elementos da GUI relacionados com a barra de progresso..
		// -----------------------------------------------------------------------

		add(painelRefrescar, BorderLayout.NORTH);
		add(painelUtilizadores, BorderLayout.WEST);
		//---------------------------------------------------
		//Fim da definição da janela IGUtilizadores
		//---------------------------------------------------
		
	}
		
}
