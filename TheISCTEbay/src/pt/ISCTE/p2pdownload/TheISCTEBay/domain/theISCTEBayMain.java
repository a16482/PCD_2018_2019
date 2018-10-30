package pt.ISCTE.p2pdownload.TheISCTEBay.domain;

import java.io.*;
import java.net.*;

public class theISCTEBayMain {
	private String ipUtilizador = "127.0.0.1";
	private static String ipDiretorio;
	private static int portoDiretorio;
	private static int portoUtilizador;
	private static String pastaTransferencias;

	public static void main(String[] args) {
		if (args.length < 4) {
			System.err.println("S�o necess�rios 4 argumentos: <IP do Diret�rio> <Porto do Diret�rio> <Porto do Utilizador> "
					+ "<Pasta para transfer�ncias>");
			System.exit(1);
		}
		ipDiretorio = args[0];
		portoDiretorio = Integer.parseInt(args[1]);
		portoUtilizador = Integer.parseInt(args[2]);
		pastaTransferencias = args[3];

		System.out.println("IP do Diret�rio: " + ipDiretorio + "\nPorto do Diretorio: "
				+ portoDiretorio + "\nPorto do Utilizador: "+ portoUtilizador + "\nPasta para transfer�ncias: " +
				pastaTransferencias);

		new theISCTEBayMain().runClient();

	}
//Registo no diretorio
	public void runClient() {
		try {
			Socket cliente = new Socket(ipDiretorio,portoDiretorio);
			ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
			saida.flush();
			saida.writeObject(new String("INSC " + ipUtilizador + " " + portoUtilizador));
			saida.close();
			cliente.close();
			System.out.println("Conex�o encerrada");
		}
		catch(Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}
}
