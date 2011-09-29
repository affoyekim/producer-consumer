package project2;

import java.util.ArrayList;

public class ConsumerThread extends Thread {
	
	private int consumerNumber;
	private int boxSize;
	private ArrayList<String> box;
	private BoundedBuffer buffer;
	
	//Initialize variables in constructor
	public ConsumerThread(BoundedBuffer buffer, int consumerNumber, int boxSize) {
		this.buffer = buffer;
		this.consumerNumber = consumerNumber;
		this.boxSize = boxSize;
		box = new ArrayList<String>(boxSize);
	}
	
	public void run() {
		//Until the thread is interrupted...
		while(true) {
			try {
				//Wait until the buffer is not empty
				buffer.acquireFull();
				//Wait until no one else is using it and prevent other access
				buffer.acquireMutex();
				//Remove the next item and put it in the box
				String item = buffer.removeItem();
				addItem(item);
				//Release semaphores
				buffer.releaseMutex();
				buffer.releaseEmpty();
			} catch (InterruptedException e) {
				//Exit notice, print anything remaining in the box, die
				System.out.println("Thread " + consumerNumber + " exit");
				printAndClear(false);
				break;
			}
		}
	}
	
	//Adds an item to the box; if it's full, print and clear
	public void addItem(String item) {
		box.add(item);
		if(isFull()) {
			printAndClear(true);
		}
	}
	
	public boolean isFull() {
		return box.size() >= boxSize;
	}
	
	//Appends to a string, then prints it out at once so that other threads don't interrupt it
	//Used for both full and partial boxes, boolean argument changes preceding message
	public void printAndClear(boolean fullBox) {
		String output = "";
		if(!fullBox) {
			if(box.size() == 0) {
				output += "  Box empty";
			}
			else {
				output += "  Partial box remaining: ";
			}
		}
		else {
			output += "Thread " + consumerNumber + " filled new box of candy containing: ";
		}
		int itemsInBox = box.size();
		for(int i = 0; i < itemsInBox; i++) {
			output += box.remove(box.size()-1) + " ";
		}
		System.out.println(output);
	}
}
