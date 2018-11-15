package pt.iscte.P2PDownload.TheISCTEBay;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadMonitora implements Runnable{
	private ThreadPoolExecutor executor;
	private int segundos;
	private boolean run=true;

	public ThreadMonitora(ThreadPoolExecutor executor, int atraso)
	{
		this.executor = executor;
		this.segundos=atraso;
	}
	public void shutdown(){
		this.run=false;
	}
	@Override
	public void run()
	{
		while(run){
			System.out.println(
					String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
							this.executor.getPoolSize(),
							this.executor.getCorePoolSize(),
							this.executor.getActiveCount(),
							this.executor.getCompletedTaskCount(),
							this.executor.getTaskCount(),
							this.executor.isShutdown(),
							this.executor.isTerminated()));
			try {
				Thread.sleep(segundos*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
