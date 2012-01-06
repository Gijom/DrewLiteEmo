package Drew.Client.TextBoard;

/**
 * Timer class used for send data to network every DELAY period (and not each time
 * a user enter a chararcter in the window)
 */
class SimpleTimer extends Thread {
	SimpleNotifiable t = null;
	boolean end = false;
	int count = 0;
	int delay = 1000;

	SimpleTimer(int delay, SimpleNotifiable t) {
		this.t = t;
		this.delay=delay;
		setDaemon(true);
	}

	public void run() {
		try {
			do {
				sleep( delay );
				//count++;
				//t.textArea1.setText(""+count);
				//if (count > 20) {
				//count=0;
				//System.out.println("trying text change");
				t.textChange();
				//}
				//System.err.println( "timer : " + System.currentTimeMillis() );
			} while( end == false);
			System.out.println("end is TRUE");
		}
		catch( InterruptedException e ) {
			e.printStackTrace();
		}
		System.out.println("timer is dead");
	}

	void end() {
		System.out.println("timer ended");
		end = true;
	}
}

