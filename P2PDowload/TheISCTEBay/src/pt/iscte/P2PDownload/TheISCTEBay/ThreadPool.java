package pt.iscte.P2PDownload.TheISCTEBay;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {

	ThreadPoolWorker[] workers;
	BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();

	public class ThreadPoolWorker extends Thread{
		private int posicao;
		
		public ThreadPoolWorker(int t ) {
			posicao=t;
		}
		
		@Override
		public void run() {
			try {
				Runnable r;
				r = getTask();
				r.run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  finally {
				ClearThreadPoolPosition(posicao);
			}
		}	

	}

	public ThreadPool (int i) {
		workers = new ThreadPoolWorker[i];
		for(int t=0; t<i; t++) {
			ClearThreadPoolPosition(t);
		}
	}
	
	public void ClearThreadPoolPosition (int pos) {
		workers[pos] = new ThreadPoolWorker(pos);
		workers[pos].start();
	}

	private Runnable getTask() throws InterruptedException {
		return tasks.take();
	}

	public synchronized void submit (Runnable primeCheckTask) {
		tasks.add(primeCheckTask);
	}
}
