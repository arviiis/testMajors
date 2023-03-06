package agents;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import data.Data;
import jade.core.Agent;

public class testAgent extends Agent{
	
	protected int [] randomArr() {
		// initialize array
		int[] Array = new int[10];
		
		// populate array with random integers
		for (int i = 0; i < Array.length; i++) {
			Array[i] = ThreadLocalRandom.current().nextInt(0, 10 + 1);
		}
		return Array;
	}
	
   protected void setup(){
		
		// Printout a welcome message
		System.out.println("Hallo! Agent "+getAID().getName()+" is ready.");
		try {
			// gets the data from PLC and sends an array with the random values to the PLC
			info();
		}
		catch (Exception e){
			System.out.println("error"+ e);
		}
    }
   
   protected void info() throws Exception {
	   
		int [] dataFrom;
		int [] dataTo = randomArr();
	   
		Data exchangeData = new Data();
		
		// Receive data from PLC
		dataFrom = exchangeData.getData();
		System.out.println(Arrays.toString(dataFrom));
		
		// Send data to PLC
		exchangeData.setData(dataTo);
   }
   
	    
	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Agent "+getAID().getName()+" terminating.");
	}

}

