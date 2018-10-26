public class Corredor extends Thread {
	private int dist;
	private Corrida corrida;

	public Corredor(int dist, Corrida corrida) {
		this.dist = dist;
		this.corrida = corrida;
	}

	@Override
	public void run() {
		try {
			while(dist > 0) {
				dist--;
				Thread.sleep((int) (Math.random()*1000));
			}
			int lugar = corrida.cheguei();
			System.out.println(getName() + " : " + lugar + "o lugar");
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}