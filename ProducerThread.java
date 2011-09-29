package project2;

public class ProducerThread extends Thread {
	
	private String color;
	private int amountToProduce;
	private int productCount = 1;
	private BoundedBuffer buffer;
	
	//Initialize in constructor
	public ProducerThread(BoundedBuffer buffer, int amountToProduce, int producerNumber) {
		this.buffer = buffer;
		this.amountToProduce = amountToProduce;
		color = pickColor(producerNumber);
	}
	
	public void run() {
		while(!isDone()) {
			//Produce the next item
			String item = produce();
			try {
				//Wait until the buffer is not full
				buffer.acquireEmpty();
				//Wait until no one else is using it and prevent other access
				buffer.acquireMutex();
				//Add the produced item
				buffer.addItem(item);
				//Release semaphores
				buffer.releaseMutex();
				buffer.releaseFull();
			} catch (InterruptedException e) {
				//Should never happen. Prints out if it does.
				System.out.println("Producer thread interrupted!");
			}
		}
	}
	
	//Produces an item as a string stating the unique color and what number item
	public String produce() {
		return color + " " + productCount++;
	}
	
	public boolean isDone() {
		return amountToProduce < productCount;
	}
	
	//Assigns the producer a unique, hard-coded color based on the given criteria
	public String pickColor(int producerNumber) {
		switch(producerNumber) {
			case 0:
				return "pink";
			case 1:
				return "maroon";
			case 2:
				return "red";
			case 3:
				return "orange";
			case 4:
				return "yellow";
			case 5:
				return "green";
			case 6:
				return "blue";
			case 7:
				return "purple";
			case 8:
				return "brown";
			case 9:
				return "black";
			default:
				return "TOO MANY PRODUCERS!!!";
		}
	}
	
}
