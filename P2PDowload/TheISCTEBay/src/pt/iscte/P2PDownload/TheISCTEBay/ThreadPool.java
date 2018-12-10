package pt.iscte.P2PDownload.TheISCTEBay;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
	private ThreadPoolWorker[] workers;
	private BlockingQueue<Runnable> tasks;
	private final int nThreads;

	public ThreadPool (int threads) {
		this.nThreads = threads;
		tasks = new LinkedBlockingQueue<>();
		workers = new ThreadPoolWorker[nThreads];
		for(int i=0; i<nThreads; i++) {
			workers[i]=new ThreadPoolWorker();
			//			clearThreadPoolPosition(t);
			workers[i].setName("Tarefa "+ i);
			workers[i].start();
		}
	}


	public void execute (Runnable task) {
		synchronized(tasks) {
			tasks.add(task);
			tasks.notify();
		}
	}

	public class ThreadPoolWorker extends Thread{

		@Override
		public void run() {
			Runnable task;
			while(true) {
				synchronized(tasks) {
					while(tasks.isEmpty()) {
						try {
							tasks.wait();
						}catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
					task = tasks.poll();
				}
				try {
					System.out.println(this.getName() + " Iniciou");
					task.run();
					System.out.println(this.getName() + " Terminou");
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}	
		}
	}
}
