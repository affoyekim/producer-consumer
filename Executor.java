package project2;

import java.util.ArrayList;

public class Executor {
	
	private static ArrayList<ProducerThread> producerThreads;
	private static ArrayList<ConsumerThread> consumerThreads;
	
	//This class simply contains the main method for the program
	//It didn't really seem like it belonged in bounded buffer and it
	//  was easier to manage over here, so there you go
	public static void main(String[] args) throws InterruptedException {
		//Parse the arguments
		int numProducers = Integer.parseInt(args[0]);
		int numConsumers = Integer.parseInt(args[1]);
		int bufferSize = Integer.parseInt(args[2]);
		int boxSize = Integer.parseInt(args[3]);
		int amountToProduce = Integer.parseInt(args[4]);
		//Create a new buffer of the appropriate size
		BoundedBuffer buffer = new BoundedBuffer(bufferSize);
		//Create and start the producers
		producerThreads = new ArrayList<ProducerThread>(numProducers);
		for(int i = 0; i < numProducers; i++) {
			ProducerThread producer = new ProducerThread(buffer, amountToProduce, i);
			producerThreads.add(producer);
			producer.start();
		}
		//Create and start the consumers
		consumerThreads = new ArrayList<ConsumerThread>(numConsumers);
		for(int i = 0; i < numConsumers; i++) {
			ConsumerThread consumer = new ConsumerThread(buffer, i, boxSize);
			consumerThreads.add(consumer);
			consumer.start();
		}
		//Wait for all producers to finish
		for(ProducerThread pThread : producerThreads) {
			pThread.join();
		}
		//Now that the producers have finished, continuously check for an empty buffer
		while(true) {
			//When the buffer has been depleted, interrupt all the consumer threads
			if(buffer.isEmpty()) {
				for(ConsumerThread cThread : consumerThreads) {
					cThread.interrupt();
				}
				break;
			}
		}
	}
	
}
