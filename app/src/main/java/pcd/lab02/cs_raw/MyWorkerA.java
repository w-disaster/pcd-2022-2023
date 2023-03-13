package pcd.lab02.cs_raw;

public class MyWorkerA extends Worker {
	
	private Object lock;
	
	public MyWorkerA(String name, Object lock){
		super(name);
		this.lock = lock;
	}
	
	public void run(){
		while (true){
		  action1();
		  /*
		  	Quando un thread é in sezione critica, nessun altro  thread puó essere in sezione critica.
		  	Possibili sequenze tra MyWorkerA e MyWorkerB:
		  		- a1, a2, a3, b1, b2, b3
		  		- a1, b1, b2, b3, a2, a3
		  		- a1, b1, b2, a2, b3, a3
		  	Esempio di sequenza impossibile: a1, a2, b1, a3, b2, b3
		   */
		  synchronized(lock){
			  action2();	
			  action3();	
		  }
		}
	}
	
	protected void action1(){
		println("a1");
		wasteRandomTime(100,500);	
	}
	
	protected void action2(){
		println("a2");
		wasteRandomTime(300,700);	
	}
	protected void action3(){
		println("a3");
		wasteRandomTime(300,700);	
	}
}

