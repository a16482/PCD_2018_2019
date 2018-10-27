package pt.iscte.p2pdownload.theDirectory.domain;

public class Servidor {

	public final static int port = 8090;
	private ServerSocket server;

	public void init() throws IOException {
		server = new ServerSocket(port);
	}

	// private List<Turno>turnos=new ArrayList<>();

	public void serve() throws IOException{
		while(true){
		Socket s=server.accept();
		new TrataCliente(s.getInputStream()).start();
	}
}
