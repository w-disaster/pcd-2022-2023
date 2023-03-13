package pcd.lab02.check_act;

public class WorkerA extends Thread{
	
	private final Counter counter;
	private int ntimes;
	
	public WorkerA(Counter c, int ntimes){
		counter = c;
		this.ntimes = ntimes;
	}
	
	public void run(){
		try {
			for (int i = 0; i < ntimes; i++){
				synchronized (counter) {
					// Check
					if (counter.getValue() > 0){
						// Act
						counter.dec();
					}
				}
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
