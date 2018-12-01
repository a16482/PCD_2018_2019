package pt.iscte.P2PDownload.TheISCTEBay;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {

	ThreadPoolWorker[] workers;
	BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();

	public class ThreadPoolWorker extends Thread{
		@Override
		public void run() {
			try {
				Runnable r;
				r = getTask();
				r.run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	

	}

	public ThreadPool (int i) {
		workers = new ThreadPoolWorker[i];
		for(ThreadPoolWorker t:workers) {
			t = new ThreadPoolWorker();
			t.start();
		}
	}

	public Runnable getTask() throws InterruptedException {
		return tasks.take();
	}

	public synchronized void submit (Runnable primeCheckTask) {
		tasks.add(primeCheckTask);
	}
}
