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
			System.err.println("São necessários 4 argumentos: <IP do Diretório> <Porto do Diretório> <Porto do Utilizador> "
					+ "<Pasta para transferências>");
			System.exit(1);
		}
		ipDiretorio = args[0];
		portoDiretorio = Integer.parseInt(args[1]);
		portoUtilizador = Integer.parseInt(args[2]);
		pastaTransferencias = args[3];

		System.out.println("IP do Diretório: " + ipDiretorio + "\nPorto do Diretorio: "
				+ portoDiretorio + "\nPorto do Utilizador: "+ portoUtilizador + "\nPasta para transferências: " +
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
			System.out.println("Conexão encerrada");
		}
		catch(Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}
}
