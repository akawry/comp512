package LockManager;

class LockManagerTest {
    public static void main (String[] args) {
        MyThread t1, t2, t3, t4;
	LockManager lm = new LockManager ();
	
	int test = 2;
	
	if (test == 1){
		t1 = new MyThread (lm, 1);
		t2 = new MyThread (lm, 2);
		t1.start ();
		t2.start ();
	} else if (test == 2){
		t3 = new MyThread(lm, 3);
		t3.start();
	} else if (test == 3){
		t3 = new MyThread(lm, 3);
		t4 = new MyThread(lm, 4);
		t3.start();
		t4.start();
	}
    }
}

class MyThread extends Thread {
    LockManager lm;
    int threadId;

    public MyThread (LockManager lm, int threadId) {
        this.lm = lm;
	this.threadId = threadId;
    }

    public void run () {
        if (threadId == 1) {
	    try {
		lm.Lock (1, "a", LockManager.READ);
	    }
	    catch (DeadlockException e) {
	        System.out.println ("Deadlock.... ");
	    }

	    try {
	        this.sleep (4000);
	    }
	    catch (InterruptedException e) { }

	    try {
		lm.Lock (1, "b", LockManager.WRITE);
	    }
	    catch (DeadlockException e) {
	        System.out.println ("Deadlock.... ");
	    }
	    
	    lm.UnlockAll (1);
	}
	else if (threadId == 2) {
	    try {
		lm.Lock (2, "b", LockManager.READ);
	    }
	    catch (DeadlockException e) { 
	        System.out.println ("Deadlock.... ");
	    }

	    try {
	        this.sleep (1000);
	    }
	    catch (InterruptedException e) { }

	    try {
		lm.Lock (2, "a", LockManager.WRITE);
	    }
	    catch (DeadlockException e) { 
	        System.out.println ("Deadlock.... ");
	    }
	    
	    lm.UnlockAll (2);
	} else if (threadId == 3){
		try {
			System.out.println("T3 is attempting to read c...");
			lm.Lock(3, "c", LockManager.READ);
		} catch (DeadlockException e){
			System.out.println("Deadlock ....");
		}
		
		try {
			this.sleep(4000);
		} catch (InterruptedException e){}
		
		try {
			System.out.println("T3 is attempting to write c... ");
			lm.Lock(3, "c", LockManager.WRITE);
		} catch (DeadlockException e){
			System.out.println("Deadlock ....");
		}
		
		try {
			System.out.println("T3 is attempting to read c... ");
			lm.Lock(3, "c", LockManager.READ);
		} catch (DeadlockException e){
			
		}
		
	} else if (threadId == 4){
		try {
			System.out.println("T4 is attempting to read c... ");
			lm.Lock(4, "c", LockManager.READ);
		} catch (DeadlockException e){
			System.out.println("Deadlock ....");
		}
	}
    }
}