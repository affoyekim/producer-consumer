package project2;

import java.util.concurrent.Semaphore;

public class BoundedBuffer {
	
	private String[] buffer;
	private Semaphore mutex;
	private Semaphore empty;
	private Semaphore full;
	private int indexFirst;
	private int indexNext;
	
	//Initialize everything in the constructor
	public BoundedBuffer(int size) {
		mutex = new Semaphore(1);
		full = new Semaphore(0);
		empty = new Semaphore(size);
		buffer = new String[size];
		indexFirst = 0;
		indexNext = 0;
	}
	
	//Add in item to tail of the circular array
	public void addItem(String item) {
		buffer[indexNext] = item;
		indexNext++;
		if(indexNext == buffer.length) {
			indexNext = 0;
		}
	}
	
	//Remove and return an item from the head of the circular array
	public String removeItem() {
		String item = buffer[indexFirst];
		indexFirst++;
		if(indexFirst == buffer.length) {
			indexFirst = 0;
		}
		return item;
	}
	
	//If the full semaphore has no permits, the buffer is empty
	public boolean isEmpty() {
		return full.availablePermits() == 0;
	}
	
	//Just mutators for the semaphores. Preserving encapsulation or whatever
	public void acquireEmpty() throws InterruptedException {
		empty.acquire();
	}
	
	public void releaseEmpty() {
		empty.release();
	}
	
	public void acquireFull() throws InterruptedException {
		full.acquire();
	}
	
	public void releaseFull() {
		full.release();
	}
	
	public void acquireMutex() throws InterruptedException {
		mutex.acquire();
	}
	
	public void releaseMutex() {
		mutex.release();
	}
	
}
